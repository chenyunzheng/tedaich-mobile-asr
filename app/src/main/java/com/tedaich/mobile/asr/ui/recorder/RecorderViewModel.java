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

    private MutableLiveData<List<Audio>> mAudioList;

    public RecorderViewModel() {
        mAudioList = new MutableLiveData<>();
        new Thread(() -> {
            List<Audio> audioList = new ArrayList<>();
            int times = 5;
            while (times-- > 0){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Audio audio2 = new Audio();
                audio2.setName("demo-" + times);
                audio2.setCreateTime(new Date());
                audio2.setDuration(100000L);
                audioList.add(audio2);
                mAudioList.postValue(audioList);
            }
        }).start();

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