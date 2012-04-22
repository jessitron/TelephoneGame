package com.jessitron.telgame.data;

import com.j256.ormlite.field.DatabaseField;

public class Reading {
	@SuppressWarnings("unused")
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(index = true)
	private long gameId;

	@DatabaseField
	private String startingText;

	@DatabaseField
	private String endingText;

	public Reading() {
		// Required
	}

	public Reading(long gameId, String startingText, String endingText) {
		this.gameId = gameId;
		this.startingText = startingText;
		this.endingText = endingText;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
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

}
