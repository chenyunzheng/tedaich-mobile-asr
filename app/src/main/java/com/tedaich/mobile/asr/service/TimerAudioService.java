package com.tedaich.mobile.asr.service;

import android.os.Handler;
import android.widget.TextView;

import com.tedaich.mobile.asr.util.AudioUtils;

import java.lang.ref.WeakReference;

public class TimerAudioService implements Runnable {

    private static final long INTERVAL = 1000;//milliseconds

    private WeakReference<TextView> timer;
    private RecordAudioTask recordAudioTask;
    private Handler handler;
    private int count;

    public TimerAudioService(TextView recorderTimer, RecordAudioTask recordAudioTask) {
        this.timer = new WeakReference<>(recorderTimer);
        this.recordAudioTask = recordAudioTask;
        this.handler = new Handler();
        this.count = -1;
    }

    @Override
    public void run() {
        if (recordAudioTask.getIsRecording().get()){
            count += 1;
            String value = AudioUtils.getTimerValue(count * INTERVAL);
            timer.get().setText(value);
        }
        handler.postDelayed(this, INTERVAL);
    }

    public void start(){
        handler.postDelayed(this, 0);
    }

    public void stop(){
        handler.removeCallbacks(this);
    }


}
