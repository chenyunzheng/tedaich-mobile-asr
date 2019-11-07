package com.tedaich.mobile.asr.ui.cloud;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CloudViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CloudViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}