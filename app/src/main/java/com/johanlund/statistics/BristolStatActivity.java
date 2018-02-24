package com.johanlund.statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.adapters.BmStatAdapter;
import com.johanlund.adapters.StatAdapter;
import com.johanlund.calc_score_classes.BristolScoreWrapper;
import com.johanlund.calc_score_classes.ScoreWrapper;
import com.johanlund.ibsfoodanalyzer.R;

import static com.johanlund.constants.Constants.HOURS_AHEAD_FOR_BM;

public class BristolStatActivity extends StatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int furthest_distance_hours_before_bm_limit = preferences.getInt(getResources().getString(R.string
                .hours_before_bm_furthest_distance_limit), 0);
        int shortest_distance_hours_before_bm_limit = preferences.getInt(getResources().getString(R.string
                        .hours_before_bm_closest_distance_limit),

                HOURS_AHEAD_FOR_BM);
        int quantLimit = preferences.getInt(getResources().getString(R.string
                .avg_bm_pref_quant_key), 0);
        return getBMScoreWrapper(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
    }

    /**
     * Perhaps overkill, but reduces code for CompleteStatActivity.
     */
    protected BristolScoreWrapper getBMScoreWrapper(int furthest_distance_hours_before_bm_limit, int shortest_distance_hours_before_bm_limit, int quantLimit) {
        return new BristolScoreWrapper(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
    }

    @Override
    public StatAdapter getStatAdapter() {
        return new BmStatAdapter(getScoreWrapper());
    }

    @Override
    public String getStringForTitle() {
        return "Bristol Type";
    }

    @Override
    protected String getInfoStr() {
        return getResources().getString(R.string.bristol_info_score);
    }
}
