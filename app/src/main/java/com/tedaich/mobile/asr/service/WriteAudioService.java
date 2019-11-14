package com.tedaich.mobile.asr.service;

import android.util.Log;

import com.tedaich.mobile.asr.util.AndroidUtils;
import com.tedaich.mobile.asr.util.AudioUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Vector;

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
                    Vector<byte[]> audioData = recordAudioTask.getAudioData();
                    for (byte[] data : audioData) {
                        fos.write(data);
                    }
                    fos.flush();
                }
            }
            AudioUtils.convertPCMToWAV(pcmAudioPath, wavAudioPath);
        } catch (Exception e) {
            Log.e(LOG_TAG, "error in writing audio data to file", e);
        }


    }

    public static void main(String[] args) {
        System.out.println(new Date().toString());
        System.out.println(AndroidUtils.formatDate(new Date()));
    }
}
