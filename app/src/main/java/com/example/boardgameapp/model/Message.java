package com.example.boardgameapp.model;

public class Message {
    private final String content, from, sent;

    public Message(String content, String from, String sent) {
        this.content = content;
        this.from = from;
        this.sent = sent;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }

    public String getSent() {
        return sent;
    }
}



