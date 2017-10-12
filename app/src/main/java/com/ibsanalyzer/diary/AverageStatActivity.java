package com.ibsanalyzer.diary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ibsanalyzer.calc_score_classes.AvgScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_AVG;

public class AverageStatActivity extends StatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public ScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int hours_ahead_for_av = preferences.getInt("hours_ahead_avg", HOURS_AHEAD_FOR_AVG);
        return new AvgScoreWrapper(hours_ahead_for_av);
    }

    @Override
    public String getStringForTitle() {
        return "Average Score";
    }
}
