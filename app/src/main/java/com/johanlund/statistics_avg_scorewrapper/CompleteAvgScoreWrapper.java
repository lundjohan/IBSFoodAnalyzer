package com.johanlund.statistics_avg_scorewrapper;

import com.johanlund.statistics_point_classes.TagPoint;

/**
 * Created by Johan on 2017-06-26.
 */

public class CompleteAvgScoreWrapper extends BristolAvgScoreWrapper {
    public CompleteAvgScoreWrapper(int start_hours_before_bm, int stop_hours_before_bm, int
            quantLimit) {
        super(start_hours_before_bm, stop_hours_before_bm, quantLimit);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getCompleteAvg();
    }
}
