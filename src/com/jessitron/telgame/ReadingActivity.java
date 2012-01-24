package com.jessitron.telgame;

import java.util.ArrayList;
import com.jessitron.telgame.database.ReadingTable;
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
    private long gameId;
    private TextView promptView ;
    private TextView instructionView;
    
    private int sameCount = 0;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading);

        instructionView = ((TextView) findViewById(R.id.instructions));

        setPrompt(getIntent().getStringExtra(Intent.EXTRA_TEXT));
        gameId = getIntent().getLongExtra(TelephoneGameActivity.EXTRA_GAME_ID, -1);
        validateInput();

    }

    private void setPrompt(String input) {
        prompt = input;
        adjustInstruction(input);
    }

    private void adjustInstruction(String input) {
       String instructions = "Push the button and then read the text to the device.";
        if (input.length() < 10  || !input.contains(" "))     {
            instructions += " (Please embellish.)";
        }
        if (sameCount > 1)  {
            instructions += " Use a funny accent.";
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
            startActivityForResult(speechToTextIntent, 4);
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
            case 4:
                Log.d(TelephoneGameActivity.LOG_PREFIX, "result code: " + resultCode);
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
        if (resultList.size() == 0) {
             toast("Nothing in results!");
        }  else {
            final String result = selectResult(resultList);
            measureSameness(prompt, result);
            ReadingTable.insertNewReading(gameId, prompt, result, this);  // TODO: move to async task
            setPrompt(result);
        }
    }

    private void measureSameness(String prompt, String result) {
        if (prompt.equals(result)) {
            sameCount++;
        }
        else sameCount = 0;
    }

    private String selectResult(ArrayList<String> resultList) {
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
        // TODO
    }
}