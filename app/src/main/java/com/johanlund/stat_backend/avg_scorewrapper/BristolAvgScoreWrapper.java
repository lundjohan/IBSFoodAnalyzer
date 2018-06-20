package com.johanlund.stat_backend.avg_scorewrapper;

import com.johanlund.stat_backend.makers.TagPointBmHandler;
import com.johanlund.stat_backend.point_classes.TagPoint;
import com.johanlund.stat_backend.stat_util.TagsWrapperBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2017-06-26.
 */

public class BristolAvgScoreWrapper extends AvgScoreWrapper {

    public BristolAvgScoreWrapper(int furthest_distance_hours_before_bm_limit, int shortest_distance_hours_before_bm_limit, int
            quantLimit) {
        super(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
    }

    @Override
    public List<TagPoint> calcScore(List<TagsWrapperBase> chunks, Map<String, TagPoint> tagPoints) {
        /*notice that furthest_distance_hours_before_bm_limit == startHoursAfterEvent,
        and shortest_distance_hours_before_bm_limit == stopHoursAfterEvent*/


        return new ArrayList<>(TagPointBmHandler.doBmScore(chunks,
                tagPoints, startHoursAfterEvent, stopHoursAfterEvent).values());
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getAvgBristol();
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
