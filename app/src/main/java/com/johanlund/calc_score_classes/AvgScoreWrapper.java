package com.johanlund.calc_score_classes;

import com.johanlund.base_classes.Chunk;
import com.johanlund.tagpoint_classes.TagPoint;

import java.util.List;
import java.util.Map;

import com.johanlund.stat_classes.TagPointMaker;

/**
 * Created by Johan on 2017-06-25.
 * <p>
 * Statistics algorithms needs TimeZone that needs a device to run on, that's why I have placed
 * this test under AndroidTests
 */

public class AvgScoreWrapper extends ScoreWrapper {

    public AvgScoreWrapper(int waitHoursAfterEvent, int stopHoursAfterEvent, int quantLimit) {
        super(waitHoursAfterEvent, stopHoursAfterEvent, quantLimit);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getOrigAvgScore();
    }

    @Override
    public Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints) {
        return TagPointMaker.doAvgScore(chunks, startHoursAfterEvent, stopHoursAfterEvent,
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