package com.jessitron.telgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jessitron.telephonegame.dao.Game;
import com.jessitron.telephonegame.dao.Reading;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ReadingListActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reading_list);

        final long gameId = getIntent().getLongExtra(TelephoneGameActivity.EXTRA_GAME_ID, -1);


        final Game game = ((TelephoneGameApplication) getApplicationContext()).getDBSession().getGameDao().load(gameId);
        populateHeaderFields(game);
        final List<Reading> readingList = game.getReadingList();

        setListAdapter(new SimpleAdapter(this, convertItemsToMap(readingList), R.layout.reading_row,
                new String[]{"endingText"},
                new int[]{R.id.endingText2}));

    }

    private void populateHeaderFields(Game game) {
        ( (TextView) findViewById(R.id.desc)).setText("Game " + game.getId() + " at " + game.getStartTimestamp());
        ( (TextView) findViewById(R.id.startingText)).setText(game.getStartingText());
    }

    protected List<? extends Map<String, ?>> convertItemsToMap(List<Reading> allReadings) {
        List<Map<String, ?>> itemMapList = new ArrayList<Map<String, ?>>();
        for (Reading reading : allReadings) {
            itemMapList.add(mapObjectToFields(reading));
        }

        return itemMapList;
    }

    private Map<String, Object> mapObjectToFields(Reading object) {
        Map<String, Object> nameToValueMap = new HashMap<String, Object>();

        nameToValueMap.put("endingText", object.getEndingText());

        return nameToValueMap;
    }
}