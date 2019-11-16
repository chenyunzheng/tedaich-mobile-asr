package com.tedaich.mobile.asr.service;

import android.media.AudioRecord;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.util.AndroidUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordAudioTask extends AsyncTask<Object, List, Object> {

    private AudioRecord audioRecord;
    private int recBufSize;
    private View view;
    private AtomicBoolean isRecording;
    private AtomicBoolean isDelete;
    private AtomicBoolean isSave;
//    private String
    private CopyOnWriteArrayList<byte[]> audioData;
    private int frameTakeRate;


    public RecordAudioTask(AudioRecord audioRecord, int recBufSize, View view){
        this.audioRecord = audioRecord;
        this.recBufSize = recBufSize;
        this.view = view;
        this.isRecording = new AtomicBoolean(true);
        this.isDelete = new AtomicBoolean(false);
        this.isSave = new AtomicBoolean(false);

        this.audioData = new CopyOnWriteArrayList<>();
        this.frameTakeRate = view.getResources().getInteger(R.integer.audio_frame_take_rate);
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

    @Override
    protected void onPreExecute() {
        Toast.makeText(view.getContext(), "Start Recording", Toast.LENGTH_SHORT).show();
        super.onPreExecute();
        new Thread(new WriteAudioService(this)).start();//开线程写文件
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
                System.out.println("start recording...");
                while (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
                    int readSize = audioRecord.read(buffer, 0, recBufSize);
                    if (audioWaveValues.isEmpty()){
                        for (int i = 0; i < readSize; i += frameTakeRate) {
                            audioWaveValues.add(buffer[i]);
                        }
                        publishProgress(audioWaveValues);
                    }
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
        if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
            audioRecord.stop();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(List... values) {
        if (values != null && values.length > 0){
            CopyOnWriteArrayList audioWaveValues = (CopyOnWriteArrayList)values[0];
            //UI work

            audioWaveValues.clear();
//            drawAudioWave(drawValues);
        }

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onCancelled(Object o) {
        //set view = null; to avoid leak
//        super.onCancelled(o);
    }

    public static void main(String[] args) {

        short a = 123;
        byte[] bytes = AndroidUtils.convertShortToByteArray(a);
        byte[] bytes_1 = new byte[2];
        bytes_1[0] = (byte) (a & 0x00ff);
        bytes_1[1] = (byte) ((a>>8) & 0x00ff);
        byte[] bytes_2 = new byte[2];
        bytes_2[0] = (byte) ((a & 0xff00) >> 8);
        bytes_2[1] = (byte) (a & 0x00ff);
        System.out.println(bytes);
        System.out.println(bytes_1);
        System.out.println(bytes_2);

    }
}
