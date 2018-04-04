package com.johanlund.statistics_portion_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_general.ScoreWrapperBase;
import com.johanlund.statistics_point_classes.PortionPoint;

import java.util.List;

/**
 * Created by Johan on 2018-04-03.
 */

public class PortionScoreWrapper extends ScoreWrapperBase<PortionPoint>{
    @Override
    protected List<PortionPoint> calcPoints(Chunk c) {
        return null;
    }

    @Override
    public List<PortionPoint> toSortedList(List<PortionPoint> points) {
        return null;
    }

    @Override
    protected boolean quantIsUnderLimit(PortionPoint point) {
        return false;
    }
}
