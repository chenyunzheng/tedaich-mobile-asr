package com.tedaich.mobile.asr.ui.file;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}