package com.ibsanalyzer.calc_score_classes;

import com.ibsanalyzer.tagpoint_classes.TagPoint;

/**
 * Created by Johan on 2017-06-26.
 */

public class CompleteScoreWrapper extends BristolScoreWrapper {
    public CompleteScoreWrapper(int hoursAheadForBm) {
        super(hoursAheadForBm);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getCompleteAvg();
    }
}
