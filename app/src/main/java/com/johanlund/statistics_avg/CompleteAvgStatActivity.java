package com.johanlund.statistics_avg;

import android.os.Bundle;

import com.johanlund.calc_score_classes.BristolScoreWrapper;
import com.johanlund.calc_score_classes.CompleteScoreWrapper;
import com.johanlund.ibsfoodanalyzer.R;

public class CompleteAvgStatActivity extends BristolAvgStatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected BristolScoreWrapper getBMScoreWrapper(int furthest_distance_hours_before_bm_limit, int shortest_distance_hours_before_bm_limit, int quantLimit) {
        return new CompleteScoreWrapper(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
    }

    @Override
    public String getStringForTitle() {
        return "Complete Score";
    }

    @Override
    protected String getInfoStr() {
        return getResources().getString(R.string.complete_info_score);
    }
}
