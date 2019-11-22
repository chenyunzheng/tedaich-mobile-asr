package com.tedaich.mobile.asr.service;

import android.os.Handler;

public class ProgressAudioService implements Runnable {

    private static final long INTERVAL = 1000/25;

    public interface ProgressCallback{
        void execute(long progressInMilliSec);
    }

    private long progressInMilliSec;
    private Handler handler;
    private ProgressCallback progressCallback;
    private boolean start;

    public ProgressAudioService(){
        progressInMilliSec = 0;
        handler = new Handler();
    }

    public void setProgressInMilliSec(long progressInMilliSec) {
        this.progressInMilliSec = progressInMilliSec;
    }

    public void setProgressCallback(ProgressCallback progressCallback){
        this.progressCallback = progressCallback;
    }

    @Override
    public void run() {
        if (start) {
            if (progressCallback != null){
                progressCallback.execute(progressInMilliSec);
            }
            progressInMilliSec += INTERVAL;
        }
        handler.postDelayed(this, INTERVAL);
    }

    public void start(){
        start = true;
        if (!handler.hasCallbacks(this)){
            handler.postDelayed(this, 0);
        }
    }

    public void pause(){
        start = false;
    }

    public void stop(){
        start = false;
        handler.removeCallbacks(this);
    }
}
