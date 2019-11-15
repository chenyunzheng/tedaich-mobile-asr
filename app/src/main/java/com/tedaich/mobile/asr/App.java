package com.tedaich.mobile.asr;

import android.app.Application;

import com.tedaich.mobile.asr.dao.DaoMaster;
import com.tedaich.mobile.asr.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        String dbName = getResources().getString(R.string.default_db_name);
        //for dev only
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, dbName);
        Database database = devOpenHelper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
