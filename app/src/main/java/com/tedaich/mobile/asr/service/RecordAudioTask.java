package com.tedaich.mobile.asr.service;

import android.media.AudioRecord;
import android.os.AsyncTask;

public class RecordAudioTask extends AsyncTask<Void, Integer, Void> {

    private AudioRecord audioRecord;


    public RecordAudioTask(AudioRecord audioRecord){

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
