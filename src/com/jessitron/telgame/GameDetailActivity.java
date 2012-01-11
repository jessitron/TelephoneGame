package com.jessitron.telgame;

import com.jessitron.telgame.database.ReadingTable;
import com.jessitron.telgame.database.TelephoneGameOpenHelper;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class GameDetailActivity extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TelephoneGameOpenHelper openHelper = new TelephoneGameOpenHelper(this);
        final long gameId = getIntent().getLongExtra(TelephoneGameActivity.EXTRA_GAME_ID, -1);
        Cursor c = ReadingTable.findReadingsForGame(gameId, openHelper.getReadableDatabase());
        startManagingCursor(c);
        
        setListAdapter(new SimpleCursorAdapter(this, R.layout.reading_row, c, new String[] { ReadingTable.ENDING_TEXT}, new int[] {R.id.endingText2}));
        
    }
}