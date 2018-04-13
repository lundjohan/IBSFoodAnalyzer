package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Rating;
import com.johanlund.stat_classes.TimePointMaker;
import com.johanlund.statistics_point_classes.TimePoint;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-03-20.
 */

public class RatingTimeScoreWrapper extends TimeScoreWrapper {
    public RatingTimeScoreWrapper(int scoreStart, int ratingEnd, int durationLimit) {
        super(scoreStart, ratingEnd, durationLimit);
    }

    @Override
    public List<TimePoint> calcPoints(List<Chunk> chunks) {
        List<TimePoint> points = new ArrayList<>();
        for (Chunk c : chunks) {
            points.addAll(calcPoints(c));
        }
        return points;
    }

    protected List<TimePoint> calcPoints(Chunk c) {
        List<Rating> ratings = c.getRatings();
        return calcPoints(ratings, c.getLastTime());
    }

    private List<TimePoint> calcPoints(List<Rating> ratings, LocalDateTime lastTimeInChunk) {
        return TimePointMaker.doTimePoint(ratings, lastTimeInChunk, scoreStart, scoreEnd);
    }
}
