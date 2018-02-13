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
    protected BristolScoreWrapper getBMScoreWrapper(int start_hours_before_bm, int stop_hours_before_bm, int quantLimit) {
        return new CompleteScoreWrapper(start_hours_before_bm, stop_hours_before_bm, quantLimit);
    }

    @Override
    public String getStringForTitle() {
        return "Complete Score";
    }
}
