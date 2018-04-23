package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.stat_classes.TimePointMaker;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.ScoreTimesBase;

import java.util.List;

public class CompleteTimeScoreWrapper extends TimeScoreWrapper{
    public CompleteTimeScoreWrapper(int scoreStart, int scoreEnd) {
        super(scoreStart, scoreEnd,0);
    }

    @Override
    protected List<TimePoint> doCalc(ScoreTimesBase stb) {
        return TimePointMaker.doBMTimePoints(stb.getScoreTimes(), scoreStart, scoreEnd);
    }
}
