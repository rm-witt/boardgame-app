package com.example.boardgameapp.db;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String ip = "62.108.32.150";
    private static final String database = "android_project";
    private static final String username = "Nutzername hier eintragen";
    private static final String password = "Passwort hier eintragen";
    private static final String port = "3306";

    private static final String url = "jdbc:mysql://" + ip + ":" + port + "/" + database;
    private static final String Classes = "com.mysql.jdbc.Driver";

    private Connection connection = null;

    private static Database INSTANCE;

    private Database() {
        // Private constructor, use getInstance() method instead

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }

        return INSTANCE;
    }

    public void update(String sql) {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet query(String sql) {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                return statement.executeQuery(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
