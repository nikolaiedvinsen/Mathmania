package com.example.nikol.mathmania;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btLaunchGame = findViewById(R.id.bt_launch_game);
        btLaunchGame.setOnClickListener(this);
        Button btStatistics = findViewById(R.id.bt_statistics);
        btStatistics.setOnClickListener(this);
        Button btPreferences = findViewById(R.id.bt_preferences);
        btPreferences.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_launch_game:
                launchGame();
                break;
            case R.id.bt_statistics:
                launchStatistics();
                break;
            case R.id.bt_preferences:
                launchPreferences();
                break;
        }
    }

    private void launchGame() {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    private void launchStatistics() {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
    }

    private void launchPreferences() {
        Intent intent = new Intent(this, Preferences.class);
        startActivity(intent);
    }

}
