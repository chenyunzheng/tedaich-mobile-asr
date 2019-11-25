package com.tedaich.mobile.asr.ui.recorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tedaich.mobile.asr.dao.AudioDao;
import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.model.Audio;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class RecorderViewModel extends ViewModel {

    private MutableLiveData<List<Audio>> mAudioList;
    private DaoSession daoSession;

    public RecorderViewModel() {
        mAudioList = new MutableLiveData<>();
    }

    public LiveData<List<Audio>> getAudioList(int userId, int count) {
        List<Audio> audioList = new ArrayList<>();
        if (daoSession != null){
            QueryBuilder<Audio> queryBuilder = daoSession.queryBuilder(Audio.class)
                    .where(AudioDao.Properties.UserId.eq(userId))
                    .limit(count)
                    .orderDesc(AudioDao.Properties.RecordTime);
            audioList = queryBuilder.list();
        }
        mAudioList.setValue(audioList);
        return mAudioList;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

}