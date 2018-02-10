package com.ibsanalyzer.calc_score_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.List;
import java.util.Map;

import stat_classes.TagPointScoreZonesHandler;

/**
 * Created by Johan on 2017-06-26.
 */

public class BlueScoreWrapper extends ScoreWrapper {
    double scoreAboveAreBluezones;

    public BlueScoreWrapper(int hoursAheadForBlueZones, double scoreAboveAreBluezones) {
        super(hoursAheadForBlueZones);
        this.scoreAboveAreBluezones = scoreAboveAreBluezones;
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getBlueZonesQuant();
    }

    @Override
    public Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints) {
        return TagPointScoreZonesHandler.addBlueZonesScore(chunks, tagPoints,
                scoreAboveAreBluezones, stopHoursAfterEvent);
    }
}
