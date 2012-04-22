package com.jessitron.telgame;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.j256.ormlite.dao.Dao;
import com.jessitron.telgame.data.GameInfo;
import com.jessitron.telgame.database.DatabaseManager;

public class ViewGamesActivity extends CustomListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Dao<GameInfo, Integer> gameInfoDao = DatabaseManager.getInstance().getDao(GameInfo.class);

		List<GameInfo> allGames = new ArrayList<GameInfo>();
		try {
			allGames.addAll(gameInfoDao.queryForAll());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, convertItemsToMap(GameInfo.class, allGames),
				R.layout.game_row, new String[] { "getStartTimestamp", "getStartingText", "getReadingCount",
						"getEndingText" }, new int[] { R.id.datetime, R.id.startingText, R.id.numberOfReadings,
						R.id.endingText });

		setListAdapter(simpleAdapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ReadingListActivity.class);
		intent.putExtra(TelephoneGameActivity.EXTRA_GAME_ID, id + 1);

		startActivity(intent);
	}
}