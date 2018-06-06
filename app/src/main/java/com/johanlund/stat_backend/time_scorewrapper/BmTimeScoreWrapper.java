package com.johanlund.stat_backend.time_scorewrapper;

import com.johanlund.stat_backend.makers.TimePointMaker;
import com.johanlund.stat_backend.point_classes.TimePoint;
import com.johanlund.util.ScoreTimesBase;

import java.util.List;

public class BmTimeScoreWrapper extends TimeScoreWrapper{
    public BmTimeScoreWrapper(int scoreStart, int scoreEnd) {
        super(scoreStart, scoreEnd,0);
    }

    @Override
    protected List<TimePoint> doCalc(ScoreTimesBase stb) {
        return TimePointMaker.doBMTimePoints(stb.getScoreTimes(), scoreStart, scoreEnd);
    }
}
