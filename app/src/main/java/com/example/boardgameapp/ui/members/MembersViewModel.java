package com.example.boardgameapp.ui.members;

import androidx.lifecycle.ViewModel;

import com.example.boardgameapp.db.Database;
import com.example.boardgameapp.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MembersViewModel extends ViewModel {
    private final Database db;

    public MembersViewModel() {
        db = Database.getInstance();
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        ResultSet resultSet = db.query("SELECT user_id, name, password FROM users;");
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String password = resultSet.getString(3);
                User user = new User(id, name, password);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}