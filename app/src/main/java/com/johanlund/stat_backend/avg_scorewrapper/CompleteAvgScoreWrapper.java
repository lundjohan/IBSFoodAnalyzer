package com.johanlund.stat_backend.avg_scorewrapper;

import com.johanlund.stat_backend.point_classes.TagPoint;

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
        //change getAvgBristol to getAvgBmScore and remove getComplete.... Change in
        // TagPointBmHandler.doBmScore resulted in this
        return tp.getAvgBristol();
    }
}
