package com.tedaich.mobile.asr.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.dao.UserDao;
import com.tedaich.mobile.asr.model.Audio;
import com.tedaich.mobile.asr.model.User;
import com.tedaich.mobile.asr.util.AndroidUtils;
import com.tedaich.mobile.asr.util.AudioUtils;
import com.tedaich.mobile.asr.util.Constants;
import com.tedaich.mobile.asr.widget.AudioWaveView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordAudioTask extends AsyncTask<Object, List, Object> {

    private static final String LOG_TAG = "RecordAudioTask";

    public interface AudioSaveCompletedListener{
        void onAudioSaveCompleted(Audio audio);
    }

    private AudioRecord audioRecord;
    private int recBufSize;
    private WeakReference<AudioWaveView> audioWaveView;
    private String audioPath;
    private Date recordTime;
    private DaoSession daoSession;
    private AtomicBoolean isRecording;
    private AtomicBoolean isDelete;
    private AtomicBoolean isSave;
    private AudioSaveCompletedListener audioSaveCompletedListener;

    private CopyOnWriteArrayList<byte[]> audioData;
    private Short[] preAudioWaveValues;
    private int frameTakeRate;
    private long lastUpdateWaveTime = 0L;

    public RecordAudioTask(AudioRecord audioRecord, int recBufSize,
                           AudioWaveView audioWaveView, String audioPath, DaoSession daoSession){
        this.audioRecord = audioRecord;
        this.recBufSize = recBufSize;
        this.audioWaveView = new WeakReference<>(audioWaveView);
        this.audioPath = audioPath;
        this.daoSession = daoSession;
        this.isRecording = new AtomicBoolean(true);
        this.isDelete = new AtomicBoolean(false);
        this.isSave = new AtomicBoolean(false);
        this.audioData = new CopyOnWriteArrayList<>();
        this.preAudioWaveValues = new Short[0];
        this.frameTakeRate = audioWaveView.getResources().getInteger(R.integer.audio_frame_take_rate);
    }

    public String getAudioPath() {
        return audioPath;
    }

    public AtomicBoolean getIsRecording() {
        return isRecording;
    }

    public AtomicBoolean getIsDelete() {
        return isDelete;
    }

    public AtomicBoolean getIsSave() {
        return isSave;
    }

    public CopyOnWriteArrayList<byte[]> getAudioData() {
        return audioData;
    }

    public void setAudioSaveCompletedListener(AudioSaveCompletedListener audioSaveCompletedListener){
        this.audioSaveCompletedListener = audioSaveCompletedListener;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(audioWaveView.get().getContext(), "Start Recording", Toast.LENGTH_SHORT).show();
        super.onPreExecute();
        final Resources resources = audioWaveView.get().getResources();
        final SharedPreferences sharedPreferences = audioWaveView.get().getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        WriteAudioService writeAudioService = new WriteAudioService(this);
        writeAudioService.setWriteAudioCallback(wavAudioPath -> {
            File wavFile = new File(wavAudioPath);
            String name = resources.getString(R.string.default_audio_name);
            String fileName = wavFile.getName();
            long duration = AudioUtils.getAudioDuration(wavFile) / 1000;//milliseconds
            float fileSize = (float)wavFile.length() / 1024;//KB
            String storePath = wavFile.getParent();
            //add audio metadata into db
            long gUserId = sharedPreferences.getLong("G_USER_ID", -1);
            long userId = -1;
            if (gUserId != -1){
                User currentUser = daoSession.queryBuilder(User.class).where(UserDao.Properties.GUserId.eq(gUserId)).unique();
                userId = currentUser.getId();
            }
            Audio audio = new Audio(userId, name, fileName, recordTime, duration, fileSize, storePath);
            daoSession.getAudioDao().insert(audio);
            Log.i(LOG_TAG, "insert audio metadata into db and audio id = " + audio.getId());
            if (audioSaveCompletedListener != null){
                audioSaveCompletedListener.onAudioSaveCompleted(audio);
            }
        });
        new Thread(writeAudioService).start();//开线程写文件
    }

    @Override
    protected Object doInBackground(Object... objects) {
        short[] buffer = new short[recBufSize];
        CopyOnWriteArrayList<Short> audioWaveValues = new CopyOnWriteArrayList<>();
        long duration = 0L, startRecordTime = 0L, stopRecordTime = 0L;
        while (!isDelete.get() && !isSave.get()){
            if (isRecording.get()){
                audioRecord.startRecording();
                startRecordTime = System.currentTimeMillis();
                if (recordTime == null){
                    recordTime = new Date();
                }
                Log.i(LOG_TAG,"start recording...");
                while (isRecording.get() && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
                    int readSize = audioRecord.read(buffer, 0, recBufSize);
                    for (int i = 0; i < readSize; i += frameTakeRate) {
                        audioWaveValues.add(buffer[i]);
                    }
                    publishProgress(audioWaveValues);
                    if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                        byte[] data = new byte[2 * readSize];
                        for (int i = 0; i < readSize; i++) {
                            byte[] shortBytes = AndroidUtils.convertShortToByteArray(buffer[i]);
                            data[2*i] = shortBytes[0];
                            data[2*i+1] = shortBytes[1];
                        }
                        audioData.add(data);
                    }
                }
            } else {
                if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
                    audioRecord.stop();
                    stopRecordTime = System.currentTimeMillis();
                    duration += (stopRecordTime - startRecordTime);
                }
            }
        }
        Log.i(LOG_TAG, "record audio completed, duration = " + duration);
        if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
            audioRecord.stop();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(List... values) {
        super.onProgressUpdate(values);
        if (values != null && values.length > 0){
            long nowTime = new Date().getTime();
            int updateWaveInterval = 1000 / 20;
            if (nowTime - lastUpdateWaveTime >= updateWaveInterval){
                CopyOnWriteArrayList audioWaveValues = (CopyOnWriteArrayList)values[0];
                //wave view update
                int valueCount = audioWaveView.get().getMaxWaveCount();
                Short[] waveValues = mergeNeighbourWaveValues(audioWaveValues, preAudioWaveValues, valueCount);
                audioWaveValues.clear();
                drawAudioWave(waveValues);
                preAudioWaveValues = waveValues;
                lastUpdateWaveTime = new Date().getTime();
            }
        }
    }

    private Short[] mergeNeighbourWaveValues(CopyOnWriteArrayList<Short> audioWaveValues, Short[] preAudioWaveValues, int valueCount) {
        Short[] result = new Short[valueCount];
        int size = audioWaveValues.size();
        Short[] audioWaveArray = new Short[size];
        audioWaveArray = audioWaveValues.toArray(audioWaveArray);
        if (size >= valueCount){
            System.arraycopy(audioWaveArray, size-valueCount, result, 0, valueCount);
        } else {
            int preSize = preAudioWaveValues.length;
            int anotherSize = preSize > (valueCount-size) ? valueCount-size : preSize;
            System.arraycopy(audioWaveArray, 0, result, valueCount-size, size);
            System.arraycopy(preAudioWaveValues, preSize - anotherSize, result, valueCount-size-anotherSize, anotherSize);
        }
        return result;
    }

    private void drawAudioWave(Short[] waveValues) {
        audioWaveView.get().setBuf(waveValues);
        audioWaveView.get().invalidate();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (audioRecord != null){
            audioRecord.release();
        }
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled();
        if (audioRecord != null){
            audioRecord.release();
        }
    }

}
