package com.ibsanalyzer.calc_score_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.model.TagTemplate;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2017-06-25.
 * This class is the Stratgy in Strategy Pattern
 */

public abstract class ScoreWrapper {
    int hoursAhead;

    public ScoreWrapper(int hoursAhead) {
        this.hoursAhead = hoursAhead;
    }

    public abstract double getScore(TagPoint tp);

    public abstract Map<String,TagPoint> calcScore(List<Chunk>chunks,Map<String, TagPoint>tagPoint);
}
