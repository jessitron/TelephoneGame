package com.jessitron.telgame;

import com.jessitron.telephonegame.dao.DaoMaster;
import com.jessitron.telephonegame.dao.DaoSession;

import android.app.Application;

public class TelephoneGameApplication extends Application {

    private DaoMaster daoMaster;


    @Override
    public void onCreate() {
        daoMaster = new DaoMaster(new DaoMaster.DevOpenHelper(this, "TELEPHONEGAME", null).getWritableDatabase());
        super.onCreate();
    }

    public DaoSession getDBSession() {
        return daoMaster.newSession();
    }

}
