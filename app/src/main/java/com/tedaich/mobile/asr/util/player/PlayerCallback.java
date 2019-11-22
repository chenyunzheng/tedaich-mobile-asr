package com.tedaich.mobile.asr.util.player;

public interface PlayerCallback {
    void onStart();
    void onProgress(long mills);
    void onPause();
    void onSeek(long mills);
    void onComplete();
}
