package com.jessitron.telgame;

import static com.jessitron.telgame.TelephoneGameActivity.LOG_PREFIX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import com.jessitron.telgame.database.TelephoneGameOpenHelper;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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

    public void backupToSDCard() {
        writableDatabase.close();

        // http://stackoverflow.com/questions/1995320/how-to-backup-database-file-to-sdcard-on-android
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();

        if (sd.canWrite()) {
            String currentDBPath = "data/com.jessitron.telgame/databases/" + TelephoneGameOpenHelper.DATABASE_NAME;
            String backupDBPath = TelephoneGameOpenHelper.DATABASE_NAME;
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
                Toast.makeText(this, "Successfully backed up to " + backupDB.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } else {
                Log.e(LOG_PREFIX, "Unable to find current database at: "+ currentDB.getAbsolutePath());
            }
        } else {
            Log.e(LOG_PREFIX, "Unable to back up - card not available")  ;
            Toast.makeText(this, "SD card not available", Toast.LENGTH_LONG).show();
        }
        initializeDB();
    }
}
