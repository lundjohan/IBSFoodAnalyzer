package com.ibsanalyzer.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ibsanalyzer.adapters.BmStatAdapter;
import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.calc_score_classes.BristolScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;
import com.ibsanalyzer.diary.R;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BRISTOL;

public class BristolStatActivity extends StatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int wait_hours_after_event = preferences.getInt(getResources().getString(R.string.avg_bm_pref_start_key),0);
        int hours_ahead = preferences.getInt(getResources().getString(R.string.avg_bm_pref_stop_key),
                HOURS_AHEAD_FOR_BRISTOL);
        int quantLimit = preferences.getInt(getResources().getString(R.string.avg_bm_pref_quant_key),0);
        return new BristolScoreWrapper(hours_ahead);
    }

    @Override
    public StatAdapter getStatAdapter() {
        return new BmStatAdapter(getScoreWrapper());
    }

    @Override
    public String getStringForTitle() {
        return "Bristol Score";
    }
}
