package com.ibsanalyzer.statistics;

import android.os.Bundle;

import com.ibsanalyzer.adapters.BmStatAdapter;
import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.calc_score_classes.BristolScoreWrapper;
import com.ibsanalyzer.calc_score_classes.CompleteScoreWrapper;

public class CompleteStatActivity extends BristolStatActivity {
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
}
