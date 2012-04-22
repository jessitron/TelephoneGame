package com.jessitron.telgame.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

public class DatabaseManager {
	static private DatabaseManager instance;

	static public void init(Context context) {
		if (instance == null) {
			instance = new DatabaseManager(context);
		}
	}

	private final TelephoneGameOpenHelper helper;

	static public DatabaseManager getInstance() {
		return instance;
	}

	public DatabaseManager(Context context) {
		helper = new TelephoneGameOpenHelper(context);
	}

	public <T> Dao<T, Integer> getDao(Class<T> clazz) {
		return helper.getDaoFor(clazz);
	}

	public void backupToSDCard() {
		helper.backupToSDCard();
	}
}
