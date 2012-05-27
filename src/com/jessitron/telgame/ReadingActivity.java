package com.jessitron.telgame;

import java.util.ArrayList;
import java.util.Date;

import com.jessitron.telephonegame.dao.DaoSession;
import com.jessitron.telephonegame.dao.Game;
import com.jessitron.telephonegame.dao.Reading;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ReadingActivity extends Activity {
    private static final int LISTEN_CODE = 42;
    private static final String EXTRA_SAME_COUNT = "SAME_COUNT";

    private String prompt;
    private long gameId;
    private TextView instructionView;

    private int sameCount = 0;
    private static final String NO_RESULT = "ain't got nuthin";
    private String heard;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading);

        instructionView = ((TextView) findViewById(R.id.instructions));

        setPrompt(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        sameCount = getIntent().getIntExtra(EXTRA_SAME_COUNT, 0);
        gameId = getIntent().getLongExtra(TelephoneGameActivity.EXTRA_GAME_ID, -1);
        validateInput();
        adjustInstruction();

    }

    private void setPrompt(String input) {
        prompt = input;
    }

    private void adjustInstruction() {
        String instructions = "Push the button and then read the text to the device.";
        if (prompt.length() < 10 || !prompt.contains(" ")) {
            instructions += " (Please embellish.)";
        }
        if (sameCount > 1) {
            instructions += " Use a funny voice.";
        }
        instructionView.setText(instructions);
    }

    private void validateInput() {
        if (prompt == null) {
            throw new RuntimeException("Need something to read in EXTRA_TEXT");
        }
        if (gameId == -1) {
            throw new RuntimeException("Need a game ID in EXTRA_GAME_ID");
        }
    }

    public void go(View v) {
        Intent speechToTextIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechToTextIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechToTextIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);

        try {
            startActivityForResult(speechToTextIntent, LISTEN_CODE);
        } catch (ActivityNotFoundException e) {
            toast("Your device does not support speech recognition. Poop'n'scoop.");
        }
    }

    public void endGame(View v) {
        Intent intent = new Intent(this, ReadingListActivity.class);
        intent.putExtra(TelephoneGameActivity.EXTRA_GAME_ID, gameId);

        startActivity(intent);

        finish();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LISTEN_CODE:
                Log.d(TelephoneGameActivity.LOG_PREFIX, "result code: " + resultCode);
                if (resultCode == RESULT_OK) {
                    dealWithResult(selectResult(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)));
                } else {
                    dealWithFailure();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    private void dealWithResult(String result) {
        if (NO_RESULT.equals(result)) {
            pleaseTryAgain();
        } else {
            this.heard = result;
            insertReadingRecord();
            startNextReadingActivity();
        }
    }


    private void startNextReadingActivity() {
        Intent readingIntent = new Intent(this, ReadingActivity.class);
        readingIntent.putExtra(Intent.EXTRA_TEXT, heard);
        readingIntent.putExtra(TelephoneGameActivity.EXTRA_GAME_ID, gameId);
        readingIntent.putExtra(EXTRA_SAME_COUNT, prompt.equals(heard) ? sameCount + 1 : 0);
        startActivity(readingIntent);
        finish();
    }

    private void insertReadingRecord() {
        final DaoSession dbSession = ((TelephoneGameApplication) getApplicationContext()).getDBSession();
        dbSession.runInTx(new Runnable() {
            @Override
            public void run() {
                // TODO: there is a way to get the game to have an updated reading list and this isn't it
                final long insertResult = dbSession.insert(new Reading(System.currentTimeMillis(), gameId, new Date(), prompt, heard));
                final Game game = dbSession.load(Game.class, gameId);
                game.setEndingText(heard);
                dbSession.update(game);
            }
        });
        Log.d("jessiTRON","debug point here");
    }

    private String selectResult(ArrayList<String> resultList) {
        if (resultList.size() == 0) {
            return NO_RESULT;
        }
        // man, I wish I was in a functional language right about now.
        String longestResult = "";
        for (String s : resultList) {
            Log.d(TelephoneGameActivity.LOG_PREFIX, "Result: " + s);
            if (s.length() > longestResult.length()) {
                longestResult = s;
            }
        }
        return longestResult;
    }

    private void dealWithFailure() {
        pleaseTryAgain();

        // TODO: how to know whether I'm on the emulator? man, I wish this flight had internet
        dealWithResult(prompt + " banana");
    }

    private void pleaseTryAgain() {
        Toast.makeText(this, "Nothing recorded. Please try again", Toast.LENGTH_SHORT).show();
    }
}