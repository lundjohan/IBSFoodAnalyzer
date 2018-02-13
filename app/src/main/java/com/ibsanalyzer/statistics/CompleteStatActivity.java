package com.ibsanalyzer.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ibsanalyzer.adapters.BmStatAdapter;
import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.calc_score_classes.CompleteScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_COMPLETE;

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

    @Override
    public StatAdapter getStatAdapter() {
        return new BmStatAdapter(getScoreWrapper());
    }
}
