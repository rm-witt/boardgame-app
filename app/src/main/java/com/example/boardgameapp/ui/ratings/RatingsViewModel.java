package com.example.boardgameapp.ui.ratings;

import androidx.lifecycle.ViewModel;

import com.example.boardgameapp.db.Database;
import com.example.boardgameapp.model.Rating;

import java.util.Locale;

public class RatingsViewModel extends ViewModel {
    private final Database db;

    public RatingsViewModel() {
        db = Database.getInstance();
    }

    public void sendRating(Rating rating) {
        String sql = String.format(
                Locale.ENGLISH,
                "INSERT INTO ratings (rating_id, from_user, for_user, comment, host_rating, food_rating, evening_rating) VALUES (NULL, '%d', '%d', '%s', '%f', '%f', '%f');",
                rating.getFromUser(),
                rating.getForUser(),
                rating.getComment(),
                rating.getHostRating(),
                rating.getFoodRating(),
                rating.getEveningRating()
        );
        db.update(sql);
    }
}