package com.ibsanalyzer.inputday;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ibsanalyzer.calc_score_classes.BristolScoreWrapper;
import com.ibsanalyzer.calc_score_classes.CompleteScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BRISTOL;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_COMPLETE;

public class BristolStatActivity extends StatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int hours_ahead = preferences.getInt("hours_ahead_bristol",
                HOURS_AHEAD_FOR_BRISTOL);
        return new BristolScoreWrapper(hours_ahead);
    }

    @Override
    public String getStringForTitle() {
        return "Bristol Score";
    }
}
