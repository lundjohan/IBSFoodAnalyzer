package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.TimePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-03-20.
 */

public class RatingTimeScoreWrapper extends TimeScoreWrapper {
    public RatingTimeScoreWrapper(int ratingStart, int ratingEnd, int durationLimit) {
        super(ratingStart,ratingEnd, durationLimit);
    }

    @Override
    public List<TimePoint> toSortedList(List<TimePoint> timePoints) {
        return null;
    }

    @Override
    public List<TimePoint> calcTimePeriods(List<Chunk> chunks) {
        return new ArrayList<TimePoint>();
    }
}
