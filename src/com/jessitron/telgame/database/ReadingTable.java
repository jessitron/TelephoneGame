package com.jessitron.telgame.database;

import com.jessitron.telgame.TelephoneGameActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReadingTable {

    public static final String TABLE_NAME = "READING";
    public static final String ID = "_id";
    public static final String GAME_ID = "game_id";
    public static final String COMPLETE_TIMESTAMP = "complete_timestamp";
    public static final String STARTING_TEXT = "starting_text";
    public static final String ENDING_TEXT = "ending_text";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + ID + " integer primary key autoincrement , " +
                GAME_ID + " integer not null references " + GameTable.TABLE_NAME + " ( " + GameTable.ID + " ) , " +
                COMPLETE_TIMESTAMP + " text default current_timestamp , " +
                STARTING_TEXT + " text not null ," +
                ENDING_TEXT + " text not null)");
    }

    public static long insertNewReading(long gameId, String startingText, String endingText, Context context) {
        // This should happen on an asynchronous task

        final TelephoneGameOpenHelper openHelper = new TelephoneGameOpenHelper(context);

        long id;
        try {
            ContentValues cv = new ContentValues();
            cv.put(GAME_ID, gameId);
            cv.put(STARTING_TEXT, startingText);
            cv.put(ENDING_TEXT, endingText);

            id = openHelper.getWritableDatabase().insert(TABLE_NAME, null, cv);

            Log.d(TelephoneGameActivity.LOG_PREFIX, "Created new reading with ID: " + id);
        } finally {
            openHelper.close();
        }

        return id;
    }

    public static Cursor findReadingsForGame(long gameId, SQLiteDatabase db) {
        return db.query(TABLE_NAME, new String[]{ID, ENDING_TEXT}, GAME_ID + " = :id", new String[]{"" + gameId}, null, null, ID + " asc");
    }
}
