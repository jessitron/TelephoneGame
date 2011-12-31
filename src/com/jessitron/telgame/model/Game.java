package com.jessitron.telgame.model;

import java.util.LinkedList;
import java.util.List;

public class Game {
    
    private int id;
    
    private List<Reading> turns;
    
    private String nextPrompt;

    public Game(String startingText) {
        id = 1; // TODO: store in db and get real ID
        turns = new LinkedList<Reading>();
        nextPrompt = startingText;
    }


    public String getNextPrompt() {
        return nextPrompt;
    }

    public int getId() {
        return id;
    }
}
