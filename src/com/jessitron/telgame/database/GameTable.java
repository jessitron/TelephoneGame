package com.jessitron.telgame.database;

import com.jessitron.telgame.TelephoneGameActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GameTable {

    public static final String TABLE_NAME = "GAME";
    public static final String ID = "_id";
    public static final String START_TIMESTAMP = "start_timestamp";
    public static final String STARTING_TEXT = "starting_text";
    public static final String ENDING_TEXT = "ending_text";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + ID + " integer primary key autoincrement , " +
                " " + START_TIMESTAMP + " text default current_timestamp , " +
                " " + STARTING_TEXT + " text not null ," +
                " " + ENDING_TEXT + " text )");
    }
    
    public static long insertNewGame(SQLiteDatabase db, String startingText) {
        ContentValues cv = new ContentValues();
        cv.put(STARTING_TEXT, startingText);

        long id = db.insert(TABLE_NAME, null, cv);

        Log.d(TelephoneGameActivity.LOG_PREFIX, "Created new game with ID: " + id);

        return id;
    }
}
