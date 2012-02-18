package com.jessitron.telgame;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jessitron.telgame.data.GameInfo;
import com.jessitron.telgame.database.DatabaseManager;

public class TelephoneGameActivity extends Activity {
	public static final String LOG_PREFIX = "jessiTRON ";
	static final String EXTRA_GAME_ID = "com.jessitron.telgame.EXTRA_GAME_ID";

	private DatabaseManager databaseManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		DatabaseManager.init(this);
		databaseManager = DatabaseManager.getInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.backupMenuItem) {
			databaseManager.backupToSDCard();
		}

		return true;
	}

	public void startGame(View view) throws SQLException {
		// TODO: validate the starting text
		// create a game
		final String startingText = getStartingText();

		Dao<GameInfo, Integer> gameInfoDao = databaseManager.getDao(GameInfo.class);
		GameInfo gameInfo = new GameInfo(gameInfoDao.countOf() + 1, startingText);

		try {
			gameInfoDao.create(gameInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Log.d(LOG_PREFIX, "Starting new game with <" + startingText + ">");

		// start the reading activity with the starting text.
		Intent readingIntent = new Intent(this, ReadingActivity.class);
		readingIntent.putExtra(Intent.EXTRA_TEXT, startingText);
		readingIntent.putExtra(EXTRA_GAME_ID, gameInfo.getGameId());
		startActivity(readingIntent);
	}

	public String getStartingText() {
		return ((TextView) findViewById(R.id.startingText)).getText().toString();
	}

	public void viewPastGames(View v) {
		startActivity(new Intent(this, ViewGamesActivity.class));
	}
}
