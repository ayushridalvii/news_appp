package com.example.techh;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        welcomeText = findViewById(R.id.welcomeText);

        // If you want to dynamically change the username, you can do:
        String username = "Ayushri"; // you can fetch this from intent extras or shared preferences
        welcomeText.setText("Welcome, " + username + ".");
    }
}
