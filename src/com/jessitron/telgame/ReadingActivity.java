package com.jessitron.telgame;

import java.util.ArrayList;
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
    private String prompt;
    private int gameId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading);

        prompt = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        gameId = getIntent().getIntExtra(TelephoneGame.EXTRA_GAME_ID, -1);
        validateInput();

        setPrompt(prompt);

    }

    private void setPrompt(String prompt) {
        ((TextView) findViewById(R.id.readThis)).setText(prompt);
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
            startActivityForResult(speechToTextIntent, 4);
        } catch (ActivityNotFoundException e) {
            toast("Your device does not support speech recognition. Poop'n'scoop.");
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 4:
                Log.d(TelephoneGame.LOG_PREFIX, "result code: " + resultCode);
                if (resultCode == RESULT_OK) {
                    dealWithResult(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
                } else {
                    dealWithFailure();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    private void dealWithResult(ArrayList<String> resultList) {

        for (String s : resultList) {
            Log.d(TelephoneGame.LOG_PREFIX, "Result: " + s);
        }
        if (resultList.size() == 0) {
             toast("Nothing in results!");
        }  else {
            // TODO: store the Reading
            setPrompt(resultList.get(0));
        }
    }

    private void dealWithFailure() {
        // TODO
    }
}