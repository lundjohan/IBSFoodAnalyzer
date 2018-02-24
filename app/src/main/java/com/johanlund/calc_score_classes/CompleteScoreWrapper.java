package com.johanlund.calc_score_classes;

import com.johanlund.tagpoint_classes.TagPoint;

/**
 * Created by Johan on 2017-06-26.
 */

public class CompleteScoreWrapper extends BristolScoreWrapper {
    public CompleteScoreWrapper(int start_hours_before_bm, int stop_hours_before_bm, int
            quantLimit) {
        super(start_hours_before_bm, stop_hours_before_bm, quantLimit);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getCompleteAvg();
    }
}
