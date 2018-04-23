package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.stat_classes.TimePointMaker;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.RatingTimes;
import com.johanlund.util.ScoreTimesBase;

import java.util.List;

/**
 * Created by Johan on 2018-03-20.
 */

public class RatingTimeScoreWrapper extends TimeScoreWrapper {
    public RatingTimeScoreWrapper(int scoreStart, int ratingEnd, int durationLimit) {
        super(scoreStart, ratingEnd, durationLimit);
    }

    @Override
    protected List<TimePoint> doCalc(ScoreTimesBase stb) {
        RatingTimes rts = (RatingTimes)stb;
        return TimePointMaker.doRatingTimePoints(rts.getScoreTimes(), rts.getChunkEnd(), scoreStart, scoreEnd);
    }
}
