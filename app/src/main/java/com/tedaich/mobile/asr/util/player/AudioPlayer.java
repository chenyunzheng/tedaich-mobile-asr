package com.tedaich.mobile.asr.util.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.tedaich.mobile.asr.service.ProgressAudioService;

import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final String LOG_TAG = "AudioPlayer";
    public static final int CREATED = 0;
    public static final int INITIALIZED = 1;
    public static final int PREPARED = 2;
    public static final int PLAYING = 3;
    public static final int PAUSED = 4;
    public static final int COMPLETED = 5;
    public static final int RELEASED = 6;
    public static final int ERROR = -1;

    private int status;
    private MediaPlayer mediaPlayer;
    private long seekPosInMilliSec;

    private PlayerCallback playerCallback;
    private ProgressAudioService progressAudioService;

    public AudioPlayer() {
        this.status = AudioPlayer.CREATED;
    }

    public void initialize(){
        if (mediaPlayer != null){
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }
        progressAudioService = new ProgressAudioService();
        status = AudioPlayer.INITIALIZED;
    }

    public void prepare(String audioPath){
        if (status != AudioPlayer.INITIALIZED){
            Log.i(LOG_TAG, "initialize audio player first");
        } else {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()){
                try (FileInputStream fis = new FileInputStream(audioPath)){
                    mediaPlayer.setDataSource(fis.getFD());
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.prepareAsync();
                    if (progressAudioService != null){
                        progressAudioService.setProgressCallback((progressInMilliSec) -> {
                            if (playerCallback != null){
                                playerCallback.onProgress(progressInMilliSec);
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, "read file error, audioPath = " + audioPath, e);
                } catch (IllegalStateException e) {
                    Log.e(LOG_TAG, "audio player called in an invalid state", e);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "error in prepare()", e);
                }
            }
        }
    }

    public void play(){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()) {
                status = AudioPlayer.PLAYING;
                return;
            }
            if (status == AudioPlayer.PREPARED || status == AudioPlayer.PAUSED){
                try {
                    mediaPlayer.start();
                    mediaPlayer.seekTo((int)seekPosInMilliSec);
                    if (progressAudioService != null){
                        progressAudioService.start();
                    }
                    if (playerCallback != null){
                        playerCallback.onStart();
                    }
                    status = AudioPlayer.PLAYING;
                } catch (IllegalStateException e){
                    Log.e(LOG_TAG, "error in play()", e);
                }
            } else {
                Log.i(LOG_TAG, "can't play without prepared/pause status");
            }
        }
    }

    public void pause(){
        if (mediaPlayer != null){
            if (status == AudioPlayer.PLAYING && mediaPlayer.isPlaying()){
                try{
                    mediaPlayer.pause();
                    seekPosInMilliSec = mediaPlayer.getCurrentPosition();
                    if (progressAudioService != null){
                        progressAudioService.pause();
                    }
                    if (playerCallback != null){
                        playerCallback.onPause();
                    }
                    status = AudioPlayer.PAUSED;
                } catch (IllegalStateException e){
                    Log.e(LOG_TAG, "error in pause()", e);
                }
            } else {
                Log.i(LOG_TAG, "can't pause without playing status");
            }
        }
    }

    public void seekTo(long milliSec){
        if (mediaPlayer != null){
            if (status == AudioPlayer.PLAYING && mediaPlayer.isPlaying()){
                seekPosInMilliSec = milliSec;
                try {
                    mediaPlayer.seekTo((int) seekPosInMilliSec);
                } catch (IllegalStateException e){
                    Log.e(LOG_TAG, "error state in seekTo()", e);
                }
                if (progressAudioService != null){
                    progressAudioService.setProgressInMilliSec(seekPosInMilliSec);
                }
            } else {
                if (playerCallback != null){
                    playerCallback.onSeek(seekPosInMilliSec);
                }
                Log.i(LOG_TAG, "can't seek without playing status");
            }
        }
    }

    public void release(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (progressAudioService != null){
            progressAudioService.stop();
            progressAudioService = null;
        }
        playerCallback = null;
        status = AudioPlayer.RELEASED;
    }

    public int getStatus() {
        return status;
    }

    public long getSeekPosInMilliSec() {
        return seekPosInMilliSec;
    }

    public void setPlayerCallback(PlayerCallback playerCallback){
        this.playerCallback = playerCallback;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mediaPlayer != mp) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = mp;
        }
        status = AudioPlayer.PREPARED;
        try {
            mediaPlayer.start();
            mediaPlayer.seekTo((int) seekPosInMilliSec);
            if (progressAudioService != null){
                progressAudioService.start();
            }
            if (playerCallback != null){
                playerCallback.onStart();
            }
            status = AudioPlayer.PLAYING;
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, "error state in onPrepared()", e);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayer != mp) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = mp;
        }
        mediaPlayer.stop();
        seekPosInMilliSec = 0;
        if (progressAudioService != null){
            progressAudioService.stop();
        }
        if (playerCallback != null){
            playerCallback.onComplete();
        }
        status = AudioPlayer.COMPLETED;
    }
}
