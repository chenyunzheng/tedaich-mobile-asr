package com.tedaich.mobile.asr.ui.recorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecorderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecorderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}