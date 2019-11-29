package com.tedaich.mobile.asr.ui.cloud;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tedaich.mobile.asr.dao.AudioDao;
import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.model.Audio;
import com.tedaich.mobile.asr.util.AndroidUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class CloudViewModel extends ViewModel {

    private static final int THREAD_NUM = 5;
    private static final String THREAD_POOL_NAME = "audio_search";
    private static final ExecutorService executorService = AndroidUtils.newFixedThreadPool(THREAD_NUM, THREAD_POOL_NAME, 0);

    private MutableLiveData<List<Audio>> mAudioList;
    private DaoSession daoSession;

    public CloudViewModel() {
        mAudioList = new MutableLiveData<>();
    }

    public LiveData<List<Audio>> getAudioList(int userId) {
        List<Audio> audioList = new ArrayList<>();
        if (daoSession != null){
            QueryBuilder<Audio> queryBuilder = daoSession.queryBuilder(Audio.class)
                    .where(AudioDao.Properties.UserId.eq(userId))
                    .orderDesc(AudioDao.Properties.RecordTime);
            audioList = queryBuilder.list();
        }
        mAudioList.setValue(audioList);
        return mAudioList;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public void executeSearch(String newText, int userId) {
        executorService.submit(() -> {
            if (daoSession != null){
                QueryBuilder<Audio> audioQueryBuilder;
                if ("".equals(newText)){
                    audioQueryBuilder = daoSession.queryBuilder(Audio.class).where(AudioDao.Properties.UserId.eq(userId))
                            .orderDesc(AudioDao.Properties.RecordTime);
                } else {
                    audioQueryBuilder = daoSession.queryBuilder(Audio.class).where(AudioDao.Properties.UserId.eq(userId),
                            AudioDao.Properties.Name.like("%" + newText + "%")).orderDesc(AudioDao.Properties.RecordTime);
                }
                mAudioList.postValue(audioQueryBuilder.list());
            }
        });
    }
}