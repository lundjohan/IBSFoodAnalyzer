package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ibsanalyzer.calc_score_classes.AvgScoreWrapper;
import com.ibsanalyzer.calc_score_classes.CompleteScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_AVG;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_COMPLETE;
import static com.ibsanalyzer.inputday.R.xml.preferences;

public class CompleteStatActivity extends StatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public ScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int hours_ahead_for_complete = preferences.getInt("hours_ahead_complete",
                HOURS_AHEAD_FOR_COMPLETE);
        return new CompleteScoreWrapper(hours_ahead_for_complete);
    }

    @Override
    public String getStringForTitle() {
        return "Complete Score";
    }
}
