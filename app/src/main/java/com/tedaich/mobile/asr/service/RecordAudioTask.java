package com.tedaich.mobile.asr.service;

import android.media.AudioRecord;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RecordAudioTask extends AsyncTask<Object, Object, Object> {

    private AudioRecord audioRecord;
    private int recBufSize;
    private View view;
    private AtomicBoolean isRecording;
    private AtomicBoolean isDelete;
    private AtomicBoolean isSave;


    public RecordAudioTask(AudioRecord audioRecord, int recBufSize, View view){
        this.audioRecord = audioRecord;
        this.recBufSize = recBufSize;
        this.view = view;
        this.isRecording = new AtomicBoolean(true);
        this.isDelete = new AtomicBoolean(false);
        this.isSave = new AtomicBoolean(false);
    }

    public AtomicBoolean getIsRecording() {
        return isRecording;
    }

    public AtomicBoolean getIsDelete() {
        return isDelete;
    }

    public AtomicBoolean getIsSave() {
        return isSave;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(view.getContext(), "Start Recording", Toast.LENGTH_SHORT).show();
        super.onPreExecute();
        new Thread(new WriteAudioService(this)).start();//开线程写文件
    }

    @Override
    protected Object doInBackground(Object... objects) {
        while (!isDelete.get() && !isSave.get()){
            if (isRecording.get()){
                audioRecord.startRecording();


            } else {

            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onCancelled(Object o) {
        //set view = null; to avoid leak
//        super.onCancelled(o);
    }

    static volatile AtomicInteger num = new AtomicInteger(0);

    static void  printNum(){
        System.out.println(num.incrementAndGet());
    }

    public static void main(String[] args) {

        for (int i=0; i<10000; i++){
            new Thread(() -> {
                printNum();
            }).start();
        }
    }
}
