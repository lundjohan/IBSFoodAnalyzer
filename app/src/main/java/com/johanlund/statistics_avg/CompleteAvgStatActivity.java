package com.johanlund.statistics_avg;

import android.os.Bundle;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_avg_scorewrapper.BristolAvgScoreWrapper;
import com.johanlund.statistics_avg_scorewrapper.CompleteAvgScoreWrapper;

public class CompleteAvgStatActivity extends BristolAvgStatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected BristolAvgScoreWrapper getBMScoreWrapper(int furthest_distance_hours_before_bm_limit, int shortest_distance_hours_before_bm_limit, int quantLimit) {
        return new CompleteAvgScoreWrapper(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
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
