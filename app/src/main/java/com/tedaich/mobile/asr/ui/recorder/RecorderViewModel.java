package com.tedaich.mobile.asr.ui.recorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.model.Audio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecorderViewModel extends ViewModel {

    private DaoSession daoSession;

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Audio>> mAudioList;

    public RecorderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");

        mAudioList = new MutableLiveData<>();
        List<Audio> audioList = new ArrayList<>();
        Audio audio = new Audio();
        audio.setName("demo");
        audio.setCreateTime(new Date());
        audio.setDuration("00:39");
        audioList.add(audio);
        mAudioList.setValue(audioList);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Audio>> getAudioList() {
        return mAudioList;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public void saveAudioMetadata() {
        Audio audio = new Audio();
    }
}