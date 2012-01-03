package com.jessitron.telgame.database;

import com.jessitron.telgame.TelephoneGameActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GameTable {

    public static final String TABLE_NAME = "GAME";
    public static final String ID = "_id";
    public static final String START_TIMESTAMP = "start_timestamp";
    public static final String STARTING_TEXT = "starting_text";
    public static final String ENDING_TEXT = "ending_text";   // TODO: remove this column?
    public static final String READING_COUNT = "countie";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + ID + " integer primary key autoincrement , " +
                " " + START_TIMESTAMP + " text default current_timestamp , " +
                " " + STARTING_TEXT + " text not null ," +
                " " + ENDING_TEXT + " text )");
    }
    
    public static long insertNewGame(String startingText, Context context) {
        // This should happen on an asynchronous task

        final TelephoneGameOpenHelper openHelper = new TelephoneGameOpenHelper(context);

        long id;                                  // todo: transactions
        try {
            ContentValues cv = new ContentValues();
            cv.put(STARTING_TEXT, startingText);

            id = openHelper.getWritableDatabase().insert(TABLE_NAME, null, cv);

            Log.d(TelephoneGameActivity.LOG_PREFIX, "Created new game with ID: " + id);
        } finally {
            openHelper.close();
        }

        return id;
    }
    
    public static Cursor listGames(SQLiteDatabase db) {

        final String sql = "select " + "g." + ID + ", g." + START_TIMESTAMP + ", g." + STARTING_TEXT + "" + ", r2.ending_text, " + READING_COUNT +
                " from " + TABLE_NAME + " g join (select " + ReadingTable.GAME_ID + ", count(_id) " + READING_COUNT +
                " , max(_id) maxid " +
                " from " + ReadingTable.TABLE_NAME + " r group by game_id) r " +
                " on r." + ReadingTable.GAME_ID + " = " + " g." + ID +
                " join reading r2 on r2.game_id = g._id "+
               " order by g." + START_TIMESTAMP + " desc";
        Log.d(TelephoneGameActivity.LOG_PREFIX, "Reading games: " + sql);
        Cursor c = db.rawQuery(sql, null) ;
        return c;
    }
}
