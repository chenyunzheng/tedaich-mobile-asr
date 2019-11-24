package com.tedaich.mobile.asr.service;

import android.util.Log;

import com.tedaich.mobile.asr.util.AudioUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

class WriteAudioService implements Runnable {

    private static final String LOG_TAG = "WriteAudioService";

    public interface WriteAudioCallback{
        void execute(String wavAudioPath);
    }

    private RecordAudioTask recordAudioTask;
    private WriteAudioCallback writeAudioCallback;

    public WriteAudioService(RecordAudioTask recordAudioTask) {
        this.recordAudioTask = recordAudioTask;
    }

    public void setWriteAudioCallback(WriteAudioCallback writeAudioCallback){
        this.writeAudioCallback = writeAudioCallback;
    }

    @Override
    public void run() {
        String audioPath = recordAudioTask.getAudioPath();
        String pcmAudioPath = audioPath + ".pcm";
        String wavAudioPath = audioPath + ".wav";
        File pcmAudioFile = new File(pcmAudioPath);
        if (pcmAudioFile.exists()){
            pcmAudioFile.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(pcmAudioFile)){
            while (!recordAudioTask.getIsDelete().get() && !recordAudioTask.getIsSave().get()){
                //write audio data into file
                if (recordAudioTask.getIsRecording().get()){
                    CopyOnWriteArrayList<byte[]> audioData = recordAudioTask.getAudioData();
                    int size = audioData.size();
                    byte[][] dataArray = new byte[size][];
                    while (size-- > 0){
                        byte[] data = audioData.remove(size);
                        dataArray[size] = data;
                    }
                    for (byte[] data : dataArray){
                        fos.write(data);
                    }
                    fos.flush();
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "error in writing audio data to pcm file", e);
        }
        if (recordAudioTask.getIsDelete().get()){
            if (pcmAudioFile.exists()){
                pcmAudioFile.delete();
            }
        }
        if (recordAudioTask.getIsSave().get()){
            try {
                AudioUtils.convertPCMToWAV(pcmAudioPath, wavAudioPath, false);
                if (writeAudioCallback != null){
                    writeAudioCallback.execute(wavAudioPath);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "error in converting pcm to wav file", e);
            }
        }
    }

}
