package com.jessitron.telgame;

import com.jessitron.telgame.database.GameTable;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewGamesActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set row layout

        // set cursor.
        final SQLiteDatabase db = ((TelephoneGameApplication) getApplicationContext()).getWritableDatabase();
        final Cursor c = GameTable.listGames(db);
        startManagingCursor(c);

        setListAdapter(new SimpleCursorAdapter(this, R.layout.game_row, c,
                new String[]{GameTable.START_TIMESTAMP,
                        GameTable.STARTING_TEXT,
                        GameTable.READING_COUNT,
                        GameTable.ENDING_TEXT},
                new int[]{R.id.datetime,
                        R.id.startingText,
                        R.id.numberOfReadings,
                        R.id.endingText}
        ));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(this, ReadingListActivity.class);
        intent.putExtra(TelephoneGameActivity.EXTRA_GAME_ID, id);

        startActivity(intent);
    }
}