package com.jessitron.telgame;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jessitron.telgame.data.GameInfo;
import com.jessitron.telgame.data.Reading;
import com.jessitron.telgame.database.DatabaseManager;

public class ReadingListActivity extends CustomListActivity {
	private DatabaseManager databaseManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading_list);

		databaseManager = DatabaseManager.getInstance();
		Dao<Reading, Integer> readingsDao = databaseManager.getDao(Reading.class);

		final long gameId = getIntent().getLongExtra(TelephoneGameActivity.EXTRA_GAME_ID, -1);

		List<Reading> readingsForGame = new ArrayList<Reading>();
		try {
			populateHeaderFields(gameId);

			readingsForGame.addAll(readingsDao.queryForEq("gameId", gameId));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Log.e("DB", "Found: " + readingsForGame.size() + " items");

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, convertItemsToMap(Reading.class, readingsForGame),
				R.layout.reading_row, new String[] { "getEndingText" }, new int[] { R.id.endingText2 });

		setListAdapter(simpleAdapter);
	}

	private void populateHeaderFields(long gameId) throws SQLException {
		GameInfo gameInfo = databaseManager.getDao(GameInfo.class).queryForEq("gameId", gameId).get(0);

		((TextView) findViewById(R.id.desc)).setText("Game " + gameId + " at " + gameInfo.getStartTimestamp());
		((TextView) findViewById(R.id.startingText)).setText(gameInfo.getStartingText());
	}
}