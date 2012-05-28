package com.jessitron.telgame;

import com.jessitron.telgame.database.TelephoneGameOpenHelper;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class TelephoneGameApplication extends Application {

    private SQLiteDatabase writableDatabase;

    @Override
    public void onCreate() {
        initializeDB();
        super.onCreate();
    }

    public SQLiteDatabase getWritableDatabase()
    {
        return writableDatabase;
    }

    @Override
    public void onTerminate() {
        writableDatabase.close();
    }

    public void initializeDB() {
        TelephoneGameOpenHelper openHelper = new TelephoneGameOpenHelper(this);
        writableDatabase = openHelper.getWritableDatabase();
    }
}
