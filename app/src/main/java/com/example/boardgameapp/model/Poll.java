package com.example.boardgameapp.model;

public class Poll {
    private final String gameName;
    private final int gameVotes, totalVotes;

    public Poll(String gameName, int gameVotes, int totalVotes) {
        this.gameName = gameName;
        this.gameVotes = gameVotes;
        this.totalVotes = totalVotes;
    }

    public String getGameName() {
        return gameName;
    }

    public int getGameVotes() {
        return gameVotes;
    }

    public int getTotalVotes() {
        return totalVotes;
    }
}
