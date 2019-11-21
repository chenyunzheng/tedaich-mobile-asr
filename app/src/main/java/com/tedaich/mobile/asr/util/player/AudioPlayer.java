package com.tedaich.mobile.asr.util.player;

import android.media.MediaPlayer;
import android.util.Log;

import com.tedaich.mobile.asr.service.ProgressAudioService;

import java.io.IOException;

public class AudioPlayer {

    private static final String LOG_TAG = "AudioPlayer";

    private MediaPlayer mediaPlayer;
    private String audioPath;
    private int seekPosInMilliSec;

    private PlayerCallback playerCallback;
    private ProgressAudioService progressAudioService;

    private AudioPlayer() { }

    public void initialize(){
        if (mediaPlayer != null){
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }
        progressAudioService = new ProgressAudioService();
    }

    public void setAudioSource(String audioPath){
        try {
            if (mediaPlayer != null){
                mediaPlayer.setDataSource(audioPath);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "error in setAudioSource()", e);
        }
    }

    public void play(){
        if (mediaPlayer != null){
            mediaPlayer.start();
        }

    }

    public void pause(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            seekPosInMilliSec = mediaPlayer.getCurrentPosition();
            if (playerCallback != null){
                playerCallback.onPause();
            }
        }

    }

    public void seekTo(long milliSec){

    }

    public void release(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setPlayerCallback(PlayerCallback playerCallback){
        this.playerCallback = playerCallback;
    }

}
