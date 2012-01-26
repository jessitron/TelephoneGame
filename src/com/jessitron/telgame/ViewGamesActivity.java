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
    private TelephoneGameOpenHelper dbHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set row layout

        // set cursor.
         dbHelper = new TelephoneGameOpenHelper(getApplicationContext());
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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(this, ReadingListActivity.class);
        intent.putExtra(TelephoneGameActivity.EXTRA_GAME_ID, id);

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}