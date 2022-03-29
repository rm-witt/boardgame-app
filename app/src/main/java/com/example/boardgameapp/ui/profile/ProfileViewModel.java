package com.example.boardgameapp.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.boardgameapp.db.Database;
import com.example.boardgameapp.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProfileViewModel extends ViewModel {
    private final Database db;

    public ProfileViewModel() {
        db = Database.getInstance();
    }

    public ArrayList<Rating> getRatingsFor(int userID) {
        ArrayList<Rating> ratings = new ArrayList<>();
        ResultSet resultSet = db.query(String.format(Locale.ENGLISH, "SELECT rating_id, from_user, for_user, comment, host_rating, food_rating, evening_rating, name FROM ratings, users WHERE for_user = '%d' AND from_user = user_id ORDER BY rating_id;", userID));
        try {
            while (resultSet.next()) {
                Rating rating = new Rating(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getDouble(5),
                        resultSet.getDouble(6),
                        resultSet.getDouble(7),
                        resultSet.getString(8)
                );
                ratings.add(rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public List<Double> getAverages(ArrayList<Rating> ratings) {
        int total = ratings.size();
        Double totalHostRating = 0.0;
        Double totalFoodRating = 0.0;
        Double totalEveningRating = 0.0;
        for (Rating rating : ratings) {
            totalHostRating += rating.getHostRating();
            totalFoodRating += rating.getFoodRating();
            totalEveningRating += rating.getEveningRating();
        }
        return Arrays.asList(totalHostRating / total, totalFoodRating / total, totalEveningRating / total);
    }
}