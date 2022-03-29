package com.example.boardgameapp.model;

public class Game {
    private final int id;
    private final String name;

    public Game(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}