package com.jessitron.telgame;

import java.util.Date;

import com.jessitron.telephonegame.dao.Game;
import com.jessitron.telephonegame.dao.GameDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
                Toast.makeText(this, "Sorry - not implemented in greenDAO version", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startGame(View view) {
        // TODO: validate the starting text
         // create a game
        Game newGame = new Game(null, getStartingText(), null, new Date());
        final GameDao gameDao = ((TelephoneGameApplication) getApplicationContext()).getDBSession().getGameDao();
        final long newId = gameDao.insert(newGame);
        Log.d(LOG_PREFIX, "Starting new game with id <" + newId + ">");
        
        // start the reading activity with the starting text.
        Intent readingIntent = new Intent(this, ReadingActivity.class);
        readingIntent.putExtra(Intent.EXTRA_TEXT, getStartingText());
        readingIntent.putExtra(EXTRA_GAME_ID, newId) ;
        startActivity(readingIntent);
        finish();
    }


    public String getStartingText() {
        return ((TextView) findViewById(R.id.startingText)).getText().toString();
    }
    
    public void viewPastGames(View v) {
        startActivity(new Intent(this, GameListActivity.class));
    }

}
