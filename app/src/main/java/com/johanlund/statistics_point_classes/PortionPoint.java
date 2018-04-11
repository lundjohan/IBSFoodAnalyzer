package com.johanlund.statistics_point_classes;

import com.johanlund.statistics_settings_portions.PortionStatRange;

/**
 * Created by Johan on 2018-04-03.
 *
 *
 * Look in stat view:
 *
 * Ranges           Avg Rating/Completeness     Quantity (nr of meals in calculation)
 * 1.0 - 2.0        5.6                         34
 * ...              ...                         ...
 */

public class PortionPoint implements PointBase{
    //from portions - to Portions
    PortionStatRange range;

    //one decimal
    Double score;

    double quant;

    public PortionPoint(PortionStatRange range, Double score, double quant) {
        this.range = range;
        this.score = score;
        this.quant = quant;
    }

    public double getQuant() {
        return quant;
    }

    public PortionStatRange getRange() {
        return range;
    }

    public Double getScore() {
        return score;
    }
}
