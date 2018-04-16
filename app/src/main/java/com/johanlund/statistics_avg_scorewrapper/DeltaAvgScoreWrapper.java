package com.johanlund.statistics_avg_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.stat_classes.DeltaPointMaker;
import com.johanlund.stat_classes.TagPointMaker;
import com.johanlund.statistics_point_classes.TagPoint;

import java.util.List;
import java.util.Map;

public class DeltaAvgScoreWrapper extends AvgScoreWrapper {

    public DeltaAvgScoreWrapper(int waitHoursAfterEvent, int stopHoursAfterEvent, int quantLimit) {
        super(waitHoursAfterEvent, stopHoursAfterEvent, quantLimit);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getOrigAvgScore();
    }

    @Override
    public Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints) {
        return DeltaPointMaker.doDeltaScore(chunks, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints);
    }

    @Override
    protected double getQuantityOfTagPoint(TagPoint tp) {
        return tp.getQuantity();
    }

    @Override
    public int getQuantityLimit() {
        return quantLimit;
    }


}