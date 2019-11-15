package com.tedaich.mobile.asr.ui.cloud;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tedaich.mobile.asr.model.Audio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudViewModel extends ViewModel {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private MutableLiveData<String> mText;
    private MutableLiveData<List<Audio>> mAudioList;

    public CloudViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

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
}