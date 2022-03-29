package com.example.boardgameapp.ui.collection;

import androidx.lifecycle.ViewModel;

import com.example.boardgameapp.db.Database;
import com.example.boardgameapp.model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CollectionViewModel extends ViewModel {
    private final Database db;

    public CollectionViewModel() {
        db = Database.getInstance();
    }

    public ArrayList<Game> getGames() {
        ArrayList<Game> games = new ArrayList<>();
        ResultSet resultSet = db.query("SELECT game_id, name FROM games;");
        try {
            while (resultSet.next()) {
                int gameId = resultSet.getInt(1);
                String gameName = resultSet.getString(2);
                Game game = new Game(gameId, gameName);
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public void sendGame(String name) {
        String sql = String.format("INSERT INTO games (name) VALUES ('%s');", name);
        db.update(sql);
    }
}
