package com.example.boardgameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boardgameapp.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickLogin(View btnLogin) {
        String inputMail, inputPassword;
        Database db = Database.getInstance();

        // Eingaben aus EditText-Feldern holen
        EditText MailAdress = findViewById(R.id.emailInput);
        inputMail = MailAdress.getText().toString();
        EditText Passwort = findViewById(R.id.passwordInput);
        inputPassword = Passwort.getText().toString();

        if (inputMail.equals("") || inputPassword.equals("")) {
            Toast.makeText(this, "Eingabe unvollständig", Toast.LENGTH_SHORT).show();
            return;
        }

        // Datenbank abfragen
        ResultSet resultSet = db.query("SELECT user_id, name, password FROM users WHERE email = '" + inputMail + "';");
        int id;
        String name, dbPassword;
        try {
            resultSet.next();
            id = resultSet.getInt(1);
            name = resultSet.getString(2);
            dbPassword = resultSet.getString(3);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Toast.makeText(this, "Kein User mit dieser E-Mail vorhanden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Eingaben mit Datenbank vergleichen
        if (!dbPassword.equals(inputPassword)) {
            Toast.makeText(this, "Falsches Passwort für den User", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sonst Login erfolgreich
        Intent changeIntent = new Intent(this, MainActivity.class);
        changeIntent.putExtra("id", id);
        changeIntent.putExtra("name", name);
        changeIntent.putExtra("password", dbPassword);

        startActivity(changeIntent);

        Toast.makeText(this, String.format("Hallo %s, du bist angemeldet", name), Toast.LENGTH_SHORT).show();
        finish();
    }
}