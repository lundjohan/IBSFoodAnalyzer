package com.johanlund.stat_backend.portion_scorewrapper;

import com.johanlund.screens.statistics.common.ScoreWrapperBase;
import com.johanlund.stat_backend.point_classes.PortionPoint;
import com.johanlund.screens.statistics.portions_settings.PortionStatRange;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Johan on 2018-04-03.
 */

public abstract class PortionScoreWrapper extends ScoreWrapperBase<PortionPoint>{
    List<PortionStatRange> ranges;
    int waitHoursAfterMeal;
    int stopHoursAfterMeal;
    int minHoursBetweenMeals;

    public PortionScoreWrapper(List<PortionStatRange> ranges, int waitHoursAfterMeal, int
            stopHoursAfterMeal, int minHoursBetweenMeals) {
        super();
        this.ranges = ranges;
        this.waitHoursAfterMeal = waitHoursAfterMeal;
        this.stopHoursAfterMeal = stopHoursAfterMeal;
        this.minHoursBetweenMeals = minHoursBetweenMeals;
    }

    @Override
    public List<PortionPoint> toSortedList(List<PortionPoint> points) {
        Collections.sort(points, new Comparator<PortionPoint>() {
                    @Override
                    public int compare(PortionPoint p1, PortionPoint p2) {
                        return (int) ((p1.getQuant() - p2.getQuant()));
                    }
                }
        );
        return points;
    }

    //returning false, as this is not implemented for portions (at least not yet).
    // It just doesn't seem needed, when ranges are hardly being that many.
    @Override
    protected boolean quantIsOverLimit(PortionPoint point) {
        return true;
    }
}
