package com.tedaich.mobile.asr.service;

import com.tedaich.mobile.asr.util.AndroidUtils;
import com.tedaich.mobile.asr.util.AudioUtils;

import java.io.File;
import java.util.Date;
import java.util.Vector;

class WriteAudioService implements Runnable {

    private RecordAudioTask recordAudioTask;

    public WriteAudioService(RecordAudioTask recordAudioTask) {
        this.recordAudioTask = recordAudioTask;
    }

    @Override
    public void run() {
        String audioDir = AudioUtils.getAudioDirectory() + File.separatorChar + "语音" + new Date().toString();

        while (!recordAudioTask.getIsDelete().get() && !recordAudioTask.getIsSave().get()){
            //write audio data into file
            if (recordAudioTask.getIsRecording().get()){
                Vector<byte[]> audioData = recordAudioTask.getAudioData();

            }
        }
    }

    public static void main(String[] args) {
        System.out.println(new Date().toString());
        System.out.println(AndroidUtils.formatDate(new Date()));
    }
}
