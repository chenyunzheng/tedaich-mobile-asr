package com.tedaich.mobile.asr.ui.cloud;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.model.Audio;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudViewModel extends ViewModel {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private MutableLiveData<List<Audio>> mAudioList;
    private DaoSession daoSession;

    public CloudViewModel() {
        mAudioList = new MutableLiveData<>();
    }

    public LiveData<List<Audio>> getAudioList() {
        if (daoSession != null){

        }
        return mAudioList;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
}