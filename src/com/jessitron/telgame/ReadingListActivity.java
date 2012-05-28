package com.jessitron.telgame;

import com.jessitron.telgame.database.GameTable;
import com.jessitron.telgame.database.ReadingTable;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ReadingListActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading_list);

        final long gameId = getIntent().getLongExtra(TelephoneGameActivity.EXTRA_GAME_ID, -1);

        populateHeaderFields(gameId);

        Cursor c = ReadingTable.findReadingsForGame(gameId, getDatabase());
        startManagingCursor(c);

        setListAdapter(new SimpleCursorAdapter(this, R.layout.reading_row,
                c,
                new String[]{ReadingTable.ENDING_TEXT},
                new int[]{R.id.endingText2}));

    }

    private void populateHeaderFields(long gameId) {
        final GameTable.GameInfo gameInfo = GameTable.retrieveGameInfo(gameId, getDatabase());
        ( (TextView) findViewById(R.id.desc)).setText("Game " + gameId + " at " + gameInfo.getStartTimestamp());
        ( (TextView) findViewById(R.id.startingText)).setText(gameInfo.getStartingText());
    }

    public SQLiteDatabase getDatabase() {
        return ((TelephoneGameApplication) getApplicationContext()).getWritableDatabase();
    }
}