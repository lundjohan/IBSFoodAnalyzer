package com.johanlund.statistics_point_classes;

import com.johanlund.statistics_settings_portions.PortionStatRange;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

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
    int duration;

    public PortionPoint(PortionStatRange range, Double score, int duration) {
        this.range = range;
        this.score = score;
        this.duration = duration;
    }
}
