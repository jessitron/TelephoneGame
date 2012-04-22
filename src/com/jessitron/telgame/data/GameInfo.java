package com.jessitron.telgame.data;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class GameInfo {
	@SuppressWarnings("unused")
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(index = true)
	private long gameId;

	@DatabaseField(index = true)
	private String startTimestamp;

	@DatabaseField
	private String startingText;

	@DatabaseField
	private int readingCount;

	@DatabaseField(canBeNull = true)
	private String endingText;

	public GameInfo() {
		// Required
	}

	public GameInfo(long gameId, String startingText) {
		this.gameId = gameId;
		this.startingText = startingText;
		this.startTimestamp = new Date().toString();
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public String getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(String startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public String getStartingText() {
		return startingText;
	}

	public void setStartingText(String startingText) {
		this.startingText = startingText;
	}

	public String getEndingText() {
		return endingText;
	}

	public void setEndingText(String endingText) {
		this.endingText = endingText;
	}

	public int getReadingCount() {
		return readingCount;
	}

	public void setReadingCount(int readingCount) {
		this.readingCount = readingCount;
	}
}