package com.jessitron.telgame;

import com.jessitron.telgame.database.GameTable;
import com.jessitron.telgame.database.TelephoneGameOpenHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TelephoneGameActivity extends Activity
{
    
    public static final String LOG_PREFIX = "jessiTRON ";
    static final String EXTRA_GAME_ID = "com.jessitron.telgame.EXTRA_GAME_ID";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.backupMenuItem:
                new TelephoneGameOpenHelper(this).backupToSDCard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startGame(View view) {
        // TODO: validate the starting text
         // create a game
        final String startingText = getStartingText();
        long gameId = GameTable.insertNewGame(getDb(), startingText);
        Log.d(LOG_PREFIX, "Starting new game with <" + startingText + ">");
        
        // start the reading activity with the starting text.
        Intent readingIntent = new Intent(this, ReadingActivity.class);
        readingIntent.putExtra(Intent.EXTRA_TEXT, startingText);
        readingIntent.putExtra(EXTRA_GAME_ID, gameId) ;
        startActivity(readingIntent);
    }

    private SQLiteDatabase getDb() {
        return new TelephoneGameOpenHelper(this).getWritableDatabase();
    }

    public String getStartingText() {
        return ((TextView) findViewById(R.id.startingText)).getText().toString();
    }

}
