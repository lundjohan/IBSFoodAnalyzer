package com.ibsanalyzer.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.calc_score_classes.AvgScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;
import com.ibsanalyzer.diary.R;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_AVG;

public class AvgStatActivity extends StatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public ScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int wait_hours_after_event = preferences.getInt(getResources().getString(R.string.avg_rating_pref_wait_key),0);
        int hours_ahead_for_av = preferences.getInt(getResources().getString(R.string.avg_rating_pref_stop_key), HOURS_AHEAD_FOR_AVG);
        int quantLimit = preferences.getInt(getResources().getString(R.string.avg_rating_pref_quant_key),0);
        return new AvgScoreWrapper(wait_hours_after_event,hours_ahead_for_av, quantLimit);
    }

    @Override
    public StatAdapter getStatAdapter() {
        return new StatAdapter(getScoreWrapper());
    }

    @Override
    public String getStringForTitle() {
        return "Average Score";
    }
}
