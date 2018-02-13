package com.ibsanalyzer.calc_score_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.List;
import java.util.Map;

import stat_classes.TagPointBmHandler;

/**
 * Created by Johan on 2017-06-26.
 */

public class BristolScoreWrapper extends ScoreWrapper {

    public BristolScoreWrapper(int start_hours_before_bm, int stop_hours_before_bm, int
            quantLimit) {
        super(start_hours_before_bm, stop_hours_before_bm, quantLimit);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getAvgBristol();
    }

    //this is essentially same as for CompleteScoreWrapper, they are calculated in same method.
    @Override
    public Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints) {
        return TagPointBmHandler.addBmScore(chunks,
                tagPoints, startHoursAfterEvent, stopHoursAfterEvent);
    }

    @Override
    public int getQuantityLimit() {
        return 0;
    }
}
