package com.johanlund.stat_backend.avg_scorewrapper;

import com.johanlund.stat_backend.makers.DeltaPointMaker;
import com.johanlund.stat_backend.point_classes.TagPoint;
import com.johanlund.util.TagsWrapperBase;

import java.util.ArrayList;
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
    public List<TagPoint> calcScore(List<TagsWrapperBase> chunks, Map<String, TagPoint> tagPoints) {
        return new ArrayList<>(DeltaPointMaker.doDeltaScore(chunks, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints).values());
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