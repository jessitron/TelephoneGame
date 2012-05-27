package com.jessitron.telgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jessitron.telephonegame.dao.DaoSession;
import com.jessitron.telephonegame.dao.Game;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewGamesActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set row layout

        // set cursor.
        final DaoSession dbSession = ((TelephoneGameApplication) getApplicationContext()).getDBSession();
        final List<Game> games = dbSession.getGameDao().loadAll();
        Collections.reverse(games);

        // DANGIT! Why won't greenDAO give me the simpleCursorAdapter? arg

        setListAdapter(new SimpleAdapter(this, convertItemsToMap( games),
                R.layout.game_row,
                new String[]{
                        "startTimestamp",
                        "startingText",
                        "readingCount",
                        "endingText"

                }, new int[]    {
        R.id.datetime,
                R.id.startingText,
                R.id.numberOfReadings,
                R.id.endingText
    }

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
    }

    protected List<? extends Map<String, ?>> convertItemsToMap(List<Game> allGames) {
        List<Map<String, ?>> itemMapList = new ArrayList<Map<String, ?>>();
        for (Game game : allGames) {
            itemMapList.add(mapObjectToFields(game));
        }

        return itemMapList;
    }

    private Map<String, Object> mapObjectToFields(Game object) {
        Map<String, Object> nameToValueMap = new HashMap<String, Object>();

        nameToValueMap.put("startTimestamp", object.getStartTimestamp());
        nameToValueMap.put("startingText", object.getStartingText());
        nameToValueMap.put("readingCount", object.getReadingList().size());
        nameToValueMap.put("endingText", object.getEndingText());

        return nameToValueMap;
    }
}