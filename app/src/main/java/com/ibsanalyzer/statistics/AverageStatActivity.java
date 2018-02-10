package com.ibsanalyzer.statistics;

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
        int wait_hours_after_event = preferences.getInt("avg_rating_stat_wait_hours_after_event",0);
        int hours_ahead_for_av = preferences.getInt("avg_rating_stat_stop_hours_after_event", HOURS_AHEAD_FOR_AVG);
        return new AvgScoreWrapper(wait_hours_after_event,hours_ahead_for_av);
    }

    @Override
    public String getStringForTitle() {
        return "Average Score";
    }
}
