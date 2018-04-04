package com.johanlund.statistics_portion_scorewrapper;

import android.graphics.Point;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_general.ScoreWrapperBase;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.statistics_settings_portions.PortionStatRange;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Johan on 2018-04-03.
 */

public abstract class PortionScoreWrapper extends ScoreWrapperBase<PortionPoint>{
    List<PortionStatRange> ranges;
    int waitHoursAfterMeal;
    int validHours;
    int minHoursBetweenMeals;

    public PortionScoreWrapper(List<PortionStatRange> ranges, int waitHoursAfterMeal, int
            validHours, int minHoursBetweenMeals) {
        super();
        this.ranges = ranges;
        this.waitHoursAfterMeal = waitHoursAfterMeal;
        this.validHours = validHours;
        this.minHoursBetweenMeals = minHoursBetweenMeals;
    }

    @Override
    public List<PortionPoint> toSortedList(List<PortionPoint> points) {
        Collections.sort(points, new Comparator<PortionPoint>() {
                    @Override
                    public int compare(PortionPoint p1, PortionPoint p2) {
                        return (int) ((p1.getDuration() - p2.getDuration()));
                    }
                }
        );
        return points;
    }

    //returning false, as this is not implemented for portions (at least not yet).
    // It just doesn't seem needed, when ranges are hardly being that many.
    @Override
    protected boolean quantIsOverLimit(PortionPoint point) {
        return false;
    }
}
