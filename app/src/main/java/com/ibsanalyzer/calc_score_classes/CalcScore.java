package com.ibsanalyzer.calc_score_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.model.TagTemplate;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.List;

/**
 * Created by Johan on 2017-06-25.
 * This class is the Stratgy in Strategy Pattern
 */

public abstract class CalcScore {

    public abstract TagPoint doCalc(List<Chunk> chunks, TagTemplate tt);
}
