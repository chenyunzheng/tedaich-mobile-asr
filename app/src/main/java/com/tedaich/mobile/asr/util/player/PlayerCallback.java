package com.tedaich.mobile.asr.util.player;

public interface PlayerCallback {
    void onPrepare();
    void onStart();
    void onProgress(long mills);
    void onStop();
    void onPause();
    void onSeek(long mills);
}
