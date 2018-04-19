package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Chunk;
import com.johanlund.stat_classes.TimePointMaker;
import com.johanlund.statistics_point_classes.TimePoint;

import java.util.ArrayList;
import java.util.List;

public class CompleteTimeScoreWrapper extends TimeScoreWrapper{
    public CompleteTimeScoreWrapper(int ratingStart, int ratingEnd) {
        super(ratingStart, ratingEnd, 0);
    }

    //copied from RatingTimeScoreWrapper
    @Override
    public List<TimePoint> calcPoints(List<Chunk> chunks) {
        List<TimePoint> points = new ArrayList<>();
        for (Chunk c : chunks) {
            points.addAll(calcPoints(c));
        }
        return points;
    }
    protected List<TimePoint> calcPoints(Chunk c) {
        List<Bm> bms = c.getBMs();
        return calcBmPoints(bms);
    }

    private List<TimePoint> calcBmPoints(List<Bm> bms) {
        return TimePointMaker.doBMTimePoints(bms, scoreStart, scoreEnd);
    }
}
