package com.ibsanalyzer.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.ibsanalyzer.adapters.BmStatAdapter;
import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.calc_score_classes.BristolScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;
import com.ibsanalyzer.diary.R;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BM;

public class BristolStatActivity extends StatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int start_hours_before_bm = preferences.getInt(getResources().getString(R.string
                .avg_bm_pref_start_key), 0);
        int stop_hours_before_bm = preferences.getInt(getResources().getString(R.string
                        .avg_bm_pref_stop_key),

                HOURS_AHEAD_FOR_BM);
        int quantLimit = preferences.getInt(getResources().getString(R.string
                .avg_bm_pref_quant_key), 0);
        return getBMScoreWrapper(start_hours_before_bm, stop_hours_before_bm, quantLimit);
    }

    /**
     * Perhaps overkill, but reduces code for CompleteStatActivity.
     */
    protected BristolScoreWrapper getBMScoreWrapper(int start_hours_before_bm, int stop_hours_before_bm, int quantLimit) {
        return new BristolScoreWrapper(start_hours_before_bm, stop_hours_before_bm, quantLimit);
    }

    @Override
    public StatAdapter getStatAdapter() {
        return new BmStatAdapter(getScoreWrapper());
    }

    @Override
    public String getStringForTitle() {
        return "Bristol Type";
    }
}
