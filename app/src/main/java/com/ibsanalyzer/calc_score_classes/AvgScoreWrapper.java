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
    public AvgScoreWrapper(int hoursAheadForAvg) {
        super(hoursAheadForAvg);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getOrigAvgScore();
    }

    @Override
    public Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints) {
        return TagPointMaker.doAvgScore(chunks, hoursAhead, tagPoints);
    }
}