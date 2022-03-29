package com.example.boardgameapp.ui.chat;

import androidx.lifecycle.ViewModel;

import com.example.boardgameapp.db.Database;
import com.example.boardgameapp.model.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatViewModel extends ViewModel {
    private final Database db;

    public ChatViewModel() {
        db = Database.getInstance();
    }

    public ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        ResultSet resultSet = db.query("SELECT message_id, content, name, sent FROM messages, users WHERE messages.from_user = users.user_id ORDER BY message_id;");
        try {
            while (resultSet.next()) {
                String content = resultSet.getString(2);
                String from = resultSet.getString(3);
                String sent = resultSet.getString(4);
                sent = convertDate(sent);
                Message msg = new Message(content, from, sent);
                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public void sendMessage(Message msg, int userID) {
        String content = msg.getContent();

        String sql = String.format(Locale.ENGLISH, "INSERT INTO messages (message_id, content, from_user, sent) VALUES (NULL, '%s', '%d', CURRENT_TIMESTAMP);",
                content,
                userID);

        db.update(sql);
    }

    public String convertDate(String sent) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            Date originalDate = originalFormat.parse(sent);
            return originalDate == null ? "" : targetFormat.format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}