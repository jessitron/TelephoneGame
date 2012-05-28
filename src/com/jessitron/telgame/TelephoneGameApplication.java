package com.jessitron.telgame;

import com.jessitron.telephonegame.dao.DaoMaster;
import com.jessitron.telephonegame.dao.DaoSession;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class TelephoneGameApplication extends Application {

    private DaoSession session;
    private SQLiteDatabase db;


    @Override
    public void onCreate() {
        db = new DaoMaster.DevOpenHelper(this, "TELEPHONEGAME", null).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        session = daoMaster.newSession();
        super.onCreate();
    }

    public DaoSession getDBSession() {
        return session;
    }

    @Override
    public void onTerminate() {
        db.close();
        super.onTerminate(); 
    }
}
