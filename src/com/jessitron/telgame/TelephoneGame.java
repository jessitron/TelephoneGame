package com.jessitron.telgame;

import com.jessitron.telgame.model.Game;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TelephoneGame extends Activity
{
    
    static final String LOG_PREFIX = "jessiTRON ";
     static final String EXTRA_GAME_ID = "com.jessitron.telgame.EXTRA_GAME_ID";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        

    }

    public void startGame(View view) {
         // create a game
        Game game = new Game(getStartingText());
        Log.d(LOG_PREFIX, "Starting new game with <" + game.getNextPrompt() + ">");
        
        // start the reading activity with the starting text.
        Intent readingIntent = new Intent(this, ReadingActivity.class);
        readingIntent.putExtra(Intent.EXTRA_TEXT, getStartingText());
        readingIntent.putExtra(EXTRA_GAME_ID, game.getId()) ;
        startActivity(readingIntent);
    }
    
    public String getStartingText() {
        return ((TextView) findViewById(R.id.startingText)).getText().toString();
    }

}
