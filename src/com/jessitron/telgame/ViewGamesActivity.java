package com.jessitron.telgame;

import com.jessitron.telgame.database.GameTable;
import com.jessitron.telgame.database.TelephoneGameOpenHelper;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ViewGamesActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set row layout

        // set cursor.
        TelephoneGameOpenHelper dbHelper = new TelephoneGameOpenHelper(this);
        final Cursor c = GameTable.listGames(dbHelper.getReadableDatabase());
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

    // Questions. Does the cursor need to be on a db that stays open... how long?
    // When does the cursor get closed? and when should we be closing the database?
    // or does it matter so much for read queries?


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra(TelephoneGameActivity.EXTRA_GAME_ID, id);

        startActivity(intent);
    }
}