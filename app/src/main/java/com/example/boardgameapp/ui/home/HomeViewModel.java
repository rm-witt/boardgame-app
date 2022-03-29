package com.example.boardgameapp.ui.home;

import androidx.lifecycle.ViewModel;

import com.example.boardgameapp.db.Database;
import com.example.boardgameapp.model.GroupInfos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class HomeViewModel extends ViewModel {
    private final Database db;

    public HomeViewModel() {
        db = Database.getInstance();
    }

    public GroupInfos getGroupInfos() {
        ResultSet resultSet = db.query("SELECT * FROM group_infos;");
        try {
            resultSet.next();
            return new GroupInfos(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getBoolean(7)
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName(int id) {
        ResultSet resultSet = db.query(String.format(Locale.ENGLISH, "SELECT name FROM users WHERE user_id = '%d';", id));
        try {
            resultSet.next();
            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Integer getNumberUsers() {
        ResultSet resultSet = db.query("SELECT COUNT(*) FROM users;");
        try {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setNextMeeting(String date) {
        String sql = String.format("UPDATE group_infos SET next_meeting = '%s' WHERE group_infos.group_id = 1;", date);
        db.update(sql);
    }
}
