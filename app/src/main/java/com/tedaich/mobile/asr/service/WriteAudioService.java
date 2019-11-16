package com.tedaich.mobile.asr.service;

import android.util.Log;

import com.tedaich.mobile.asr.util.AndroidUtils;
import com.tedaich.mobile.asr.util.AudioUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

class WriteAudioService implements Runnable {

    private static final String LOG_TAG = "WriteAudioService";

    private RecordAudioTask recordAudioTask;

    public WriteAudioService(RecordAudioTask recordAudioTask) {
        this.recordAudioTask = recordAudioTask;
    }

    @Override
    public void run() {
        String defaultAudioName = "语音_" + AndroidUtils.formatDate(new Date());
        String audioPath = AudioUtils.getAudioDirectory() + File.separatorChar + defaultAudioName;
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
            AudioUtils.convertPCMToWAV(pcmAudioPath, wavAudioPath, true);
        } catch (Exception e) {
            Log.e(LOG_TAG, "error in writing audio data to file", e);
        }
    }

}
