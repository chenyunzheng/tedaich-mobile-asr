package com.tedaich.mobile.asr.service;

class WriteAudioService implements Runnable {

    private RecordAudioTask recordAudioTask;

    public WriteAudioService(RecordAudioTask recordAudioTask) {
        this.recordAudioTask = recordAudioTask;
    }

    @Override
    public void run() {
        while (recordAudioTask.getIsRecording().get()){
            //write audio data into file
        }
    }
}
