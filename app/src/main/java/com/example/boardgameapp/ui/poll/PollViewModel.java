package com.example.boardgameapp.ui.poll;

import androidx.lifecycle.ViewModel;

import com.example.boardgameapp.db.Database;
import com.example.boardgameapp.model.Poll;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class PollViewModel extends ViewModel {
    private final Database db;

    public PollViewModel() {
        db = Database.getInstance();
    }

    public void setPollActive() {
        String sql = "UPDATE group_infos SET is_poll_active = '1';";
        db.update(sql);
    }

    public void setPollInactive() {
        String sql = "UPDATE group_infos SET is_poll_active = '0';";
        db.update(sql);
    }

    public void setNextHost(int next_host) {
        String sql = String.format(Locale.ENGLISH, "UPDATE group_infos SET next_host = %d;", next_host);
        db.update(sql);
    }

    public ArrayList<Poll> getPolls() {
        ResultSet countTotalVotes = db.query("SELECT COUNT(*) FROM users WHERE voted_for IS NOT NULL;");
        int totalVotes = 0;
        try {
            countTotalVotes.next();
            totalVotes = countTotalVotes.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Poll> polls = new ArrayList<>();
        ResultSet resultSet = db.query("SELECT games.name, COUNT(*) FROM users, games WHERE users.voted_for=games.game_id GROUP BY game_id;");
        try {
            while (resultSet.next()) {
                String gameName = resultSet.getString(1);
                int gameVotes = resultSet.getInt(2);
                Poll poll = new Poll(gameName, gameVotes, totalVotes);
                polls.add(poll);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return polls;
    }

    public void voteForGame(int selectedGame, int userId) {
        String sql = String.format(Locale.ENGLISH, "UPDATE users SET voted_for = '%d' WHERE users.user_id = %d;", selectedGame, userId);
        db.update(sql);
    }

    public void resetVotes() {
        String sql = "UPDATE users SET voted_for = NULL";
        db.update(sql);
    }
}

