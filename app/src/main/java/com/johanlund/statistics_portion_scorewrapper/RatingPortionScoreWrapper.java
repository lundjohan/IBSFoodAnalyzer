package com.johanlund.statistics_portion_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_settings_portions.PortionStatRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-04-03.
 */

public class RatingPortionScoreWrapper extends PortionScoreWrapper{
    public RatingPortionScoreWrapper(List<PortionStatRange> ranges, int waitHoursAfterMeal, int
            validHours, int minHoursBetweenMeals) {
        super(ranges, waitHoursAfterMeal, validHours, minHoursBetweenMeals);
    }

    @Override
    public List<PortionPoint> calcPoints(List<Chunk> chunks) {
        List<PortionPoint> points = new ArrayList<>();
        for (Chunk c : chunks) {
            points.addAll(calcPoints(c));
        }
        return points;
    }

    private List<PortionPoint> calcPoints(Chunk c) {
        return null;
    }
}

