package com.example.nikol.mathmania;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Preferences extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences preferences;

    private ImageView mNorway;
    private ImageView mGermany;

    private Button btSetRounds;
    private int mNumOfRounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        TextView toolbarTitle = findViewById(R.id.text_toolbar_title);
        toolbarTitle.setText(R.string.bt_preferences);


        mNorway = findViewById(R.id.imageNorway);
        mNorway.setOnClickListener(this);
        mGermany = findViewById(R.id.imageGermany);
        mGermany.setOnClickListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mNumOfRounds = preferences.getInt("num_of_rounds", 5);

        btSetRounds = findViewById(R.id.bt_set_rounds);
        btSetRounds.setOnClickListener(this);
        btSetRounds.setText(Integer.toString(mNumOfRounds));


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.bt_set_rounds) {
            switch (btSetRounds.getText().toString()) {
                case "5":
                    mNumOfRounds = 10;
                    break;
                case "10":
                    mNumOfRounds = 25;
                    break;
                case "25":
                    mNumOfRounds = 5;
                    break;
            }
            btSetRounds.setText(Integer.toString(mNumOfRounds));
            System.out.println(mNumOfRounds);
        }
        else {
            String loc = "";
            switch (view.getId()) {
                case R.id.imageGermany:
                    loc = "de";
                    break;

                case R.id.imageNorway:
                    loc = "no";
                    recreate();
                    break;
            }
            LanguageHandler.changeLocale(getResources(), loc);
            recreate();
        }

    }



    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("num_of_rounds", mNumOfRounds);
        editor.apply();
    }
}
