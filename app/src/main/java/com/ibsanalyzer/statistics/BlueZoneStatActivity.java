package com.ibsanalyzer.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.calc_score_classes.BlueScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BLUEZONES;
import static com.ibsanalyzer.constants.Constants.SCORE_BLUEZONES_FROM;

public class BlueZoneStatActivity extends StatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public ScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int hours_ahead_for_blue = preferences.getInt("hours_ahead_bluezones", HOURS_AHEAD_FOR_BLUEZONES);
        return new BlueScoreWrapper(hours_ahead_for_blue,SCORE_BLUEZONES_FROM);
    }

    @Override
    public StatAdapter getStatAdapter() {
        return null;
    }

    @Override
    public String getStringForTitle() {
        return "Blue Zone Score";
    }
}
