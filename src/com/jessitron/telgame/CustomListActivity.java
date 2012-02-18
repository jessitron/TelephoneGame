package com.jessitron.telgame;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;

public class CustomListActivity extends ListActivity {
	protected <T> List<? extends Map<String, ?>> convertItemsToMap(Class<T> clazz, List<T> allGames) {
		List<Map<String, ?>> itemMapList = new ArrayList<Map<String, ?>>();
		for (T game : allGames) {
			itemMapList.add(mapObjectToFields(clazz, game));
		}

		return itemMapList;
	}

	private <T> Map<String, ?> mapObjectToFields(Class<T> clazz, T object) {
		Map<String, String> nameToValueMap = new HashMap<String, String>();

		for (Method method : clazz.getMethods()) {
			String methodName = method.getName();
			if (methodName.startsWith("get") && !methodName.equals("getClass") && !methodName.equals("getId")) {
				try {
					Object value = method.invoke(object);
					nameToValueMap.put(methodName, "" + value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return nameToValueMap;
	}
}
