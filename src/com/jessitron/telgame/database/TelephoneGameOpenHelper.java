package com.jessitron.telgame.database;

import static com.jessitron.telgame.TelephoneGameActivity.LOG_PREFIX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jessitron.telgame.data.GameInfo;
import com.jessitron.telgame.data.Reading;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

class TelephoneGameOpenHelper extends OrmLiteSqliteOpenHelper {
	public static final int VERSION = 2;
	public static final String DATABASE_NAME = "TELEPHONEGAME";

	private static List<Class<?>> persistedClasses = new ArrayList<Class<?>>();

	private static Map<Class<?>, Dao<Class<?>, Integer>> cachedDaoMap = new HashMap<Class<?>, Dao<Class<?>, Integer>>();

	private final Context context;

	static {
		persistedClasses.add(GameInfo.class);
		persistedClasses.add(Reading.class);
	}

	public TelephoneGameOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);

		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
		try {
			for (Class<?> persistedClass : persistedClasses) {
				TableUtils.createTable(connectionSource, persistedClass);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		try {
			for (Class<?> persistedClass : persistedClasses) {
				TableUtils.dropTable(connectionSource, persistedClass, false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> Dao<T, Integer> getDaoFor(Class<T> clazz) {
		if (cachedDaoMap.get(clazz) == null) {
			try {
				Dao dao = super.getDao(clazz);
				cachedDaoMap.put(clazz, dao);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}

		return (Dao<T, Integer>) cachedDaoMap.get(clazz);
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
					backupDB = new File(sd, backupDBPath);
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
				Toast.makeText(context, "Successfully backed up to " + backupDB.getAbsolutePath(), Toast.LENGTH_LONG)
						.show(); // TODO: doesn't work
			} else {
				Log.e(LOG_PREFIX, "Unable to find current database at: " + currentDB.getAbsolutePath());
			}
		} else {
			Log.e(LOG_PREFIX, "Unable to back up - card not available");
			Toast.makeText(context, "SD card not available", Toast.LENGTH_LONG).show();
		}
	}

}
