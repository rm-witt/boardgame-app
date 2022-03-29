package com.example.boardgameapp.model;

public class Rating {
    private final int ratingId, fromUser, forUser;
    private final String comment, fromUsername;
    private final Double hostRating, foodRating, eveningRating;

    public Rating(int ratingId, int fromUser, int forUser, String comment, Double hostRating, Double foodRating, Double eveningRating, String fromUsername) {
        this.ratingId = ratingId;
        this.fromUser = fromUser;
        this.forUser = forUser;
        this.comment = comment;
        this.hostRating = hostRating;
        this.foodRating = foodRating;
        this.eveningRating = eveningRating;
        this.fromUsername = fromUsername;
    }

    public int getRatingId() {
        return ratingId;
    }

    public int getFromUser() {
        return fromUser;
    }

    public int getForUser() {
        return forUser;
    }

    public String getComment() {
        return comment;
    }

    public Double getHostRating() {
        return hostRating;
    }

    public Double getFoodRating() {
        return foodRating;
    }

    public Double getEveningRating() {
        return eveningRating;
    }

    public String getFromUsername() {
        return fromUsername;
    }
}
