package com.ibsanalyzer.calc_score_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.model.TagTemplate;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.List;

/**
 * Created by Johan on 2017-06-25.
 */

public class AvgScore extends CalcScore{
    int hours;
    public AvgScore(int hours) {
        this.hours = hours;
    }

    @Override
    public TagPoint doCalc(List<Chunk> chunks, TagTemplate tt) {
        return null;
    }
}
