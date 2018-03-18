package com.johanlund.calc_score_classes;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.TagPoint;

import java.util.List;
import java.util.Map;

import com.johanlund.stat_classes.TagPointBmHandler;

/**
 * Created by Johan on 2017-06-26.
 */

public class BristolScoreWrapper extends ScoreWrapper {

    public BristolScoreWrapper(int furthest_distance_hours_before_bm_limit, int shortest_distance_hours_before_bm_limit, int
            quantLimit) {
        super(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getAvgBristol();
    }

    //this is essentially same as for CompleteScoreWrapper, they are calculated in same method.
    @Override
    public Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints) {
        /*notice that furthest_distance_hours_before_bm_limit == startHoursAfterEvent,
        and shortest_distance_hours_before_bm_limit == stopHoursAfterEvent*/
        return TagPointBmHandler.addBmScore(chunks,
                tagPoints, startHoursAfterEvent, stopHoursAfterEvent);
    }

    @Override
    protected double getQuantityOfTagPoint(TagPoint tp) {
        return tp.getNrOfBMs();
    }

    @Override
    public int getQuantityLimit() {
        return quantLimit;
    }
}
