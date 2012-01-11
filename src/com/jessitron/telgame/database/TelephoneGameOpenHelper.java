package com.jessitron.telgame.database;

import static com.jessitron.telgame.TelephoneGameActivity.LOG_PREFIX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import com.jessitron.telgame.TelephoneGameActivity;
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

    public void backupToSDCard() {
        close();
        // http://stackoverflow.com/questions/1995320/how-to-backup-database-file-to-sdcard-on-android
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();

        if (sd.canWrite()) {
            String currentDBPath = "data/com.jessitron.telgame/databases/" + DATABASE_NAME;
            String backupDBPath = DATABASE_NAME;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            Log.i(LOG_PREFIX, "Backing up database to: " + backupDB.getAbsolutePath());

            if (currentDB.exists()) {
                if (backupDB.exists()) {
                    Log.d(LOG_PREFIX, "Deleting old backup");
                    backupDB.delete();
                    backupDB =  new File(sd, backupDBPath);
                }
                FileChannel src = null;
                FileChannel dst = null;
                try {
                    src = new FileInputStream(currentDB).getChannel();
                    dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        src.close();
                        dst.close();
                    } catch (Exception e) {
                    }
                }
                Toast.makeText(context, "Successfully backed up to " + backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();    // TODO: doesn't work
            } else {
                Log.e(LOG_PREFIX, "Unable to find current database at: "+ currentDB.getAbsolutePath());
            }
        } else {
            Log.e(LOG_PREFIX, "Unable to back up - card not available")  ;
            Toast.makeText(context, "SD card not available", Toast.LENGTH_LONG).show();
        }
    }
}
