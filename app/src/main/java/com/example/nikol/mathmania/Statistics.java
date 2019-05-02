package com.example.nikol.mathmania;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {

    private int mNumOfCorrect;
    private int mNumOfWrong;
    private int mNumOfTimesPlayed;

    private TextView mTotalView;
    private TextView mCorrectView;
    private TextView mWrongView;
    private TextView mPercentageView;
    private TextView mTimesPlayedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        TextView toolbarTitle = findViewById(R.id.text_toolbar_title);
        toolbarTitle.setText(R.string.bt_statistics);

        mTotalView = findViewById(R.id.text_total);
        mCorrectView = findViewById(R.id.text_correct);
        mWrongView = findViewById(R.id.text_wrong);
        mPercentageView = findViewById(R.id.text_percentage);
        mTimesPlayedView = findViewById(R.id.text_times_played);

        getSavedStatistics();
        populateStatistics();
    }

    private void getSavedStatistics() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mNumOfCorrect = preferences.getInt("num_of_correct", 0);
        mNumOfWrong = preferences.getInt("num_of_wrong", 0);
        mNumOfTimesPlayed = preferences.getInt("num_of_times_played", 0);
    }

    private void populateStatistics() {
        mTotalView.setText(Integer.toString(mNumOfCorrect + mNumOfWrong));
        mCorrectView.setText(Integer.toString(mNumOfCorrect));
        mWrongView.setText(Integer.toString(mNumOfWrong));
        double percentage = (double) mNumOfCorrect/(mNumOfCorrect+mNumOfWrong)*100;
        int percent = (int) percentage;
        mPercentageView.setText(Integer.toString(percent) + " %");
        mTimesPlayedView.setText(Integer.toString(mNumOfTimesPlayed));
    }

    private void deleteStatistics() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteStatistics();
                getSavedStatistics();
                populateStatistics();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
