package com.ibsanalyzer.calc_score_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.List;
import java.util.Map;

import stat_classes.TagPointMaker;

/**
 * Created by Johan on 2017-06-25.
 */

public class AvgScoreWrapper extends ScoreWrapper {
    int quantLimit = 0;
    public AvgScoreWrapper(int waitHoursAfterEvent, int stopHoursAfterEvent, int quantLimit) {
        super(waitHoursAfterEvent, stopHoursAfterEvent);
        this.quantLimit = quantLimit;
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getOrigAvgScore();
    }

    @Override
    public Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints) {
        return TagPointMaker.doAvgScore(chunks, startHoursAfterEvent, stopHoursAfterEvent, tagPoints);
    }

    @Override
    public int getQuantityLimit() {
        return quantLimit;
    }

}