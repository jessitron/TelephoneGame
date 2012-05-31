package com.jessitron.telgame.database;

import static com.jessitron.telgame.TelephoneGameActivity.LOG_PREFIX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import com.jessitron.telgame.TelephoneGameActivity;
import com.jessitron.telgame.TelephoneGameApplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class TelephoneGameOpenHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "TELEPHONEGAME";
    private final Context context;

    public TelephoneGameOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        GameTable.onCreate(sqLiteDatabase);
        ReadingTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // nothing yet.
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        logDatabaseVersion(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON");   // this is the do-things-right flag for foreign keys
        }
    }

    private void logDatabaseVersion(SQLiteDatabase db) {
        final Cursor cursor = db.rawQuery("select sqlite_version()", null);
        if (cursor.moveToFirst())  {
            Log.d(TelephoneGameActivity.LOG_PREFIX, "SQLite version: " + cursor.getString(0));
        }
        cursor.close();
    }


}
