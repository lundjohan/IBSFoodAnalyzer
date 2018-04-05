package com.johanlund.statistics_point_classes;

import com.johanlund.statistics_settings_portions.PortionStatRange;

/**
 * Created by Johan on 2018-04-03.
 *
 *
 * Look in stat view:
 *
 * Ranges           Avg Rating/Completeness     Duration Hours
 * 1.0 - 2.0        5.6                         34
 * ...              ...                         ...
 */

public class PortionPoint implements PointBase{
    //from portions - to Portions
    PortionStatRange range;

    //one decimal
    Double score;

    //in hours
    double duration;

    public PortionPoint(PortionStatRange range, Double score, double duration) {
        this.range = range;
        this.score = score;
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public PortionStatRange getRange() {
        return range;
    }

    public Double getScore() {
        return score;
    }
}
