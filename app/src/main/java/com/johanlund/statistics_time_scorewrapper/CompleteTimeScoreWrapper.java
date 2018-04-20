package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.stat_classes.TimePointMaker;
import com.johanlund.statistics_point_classes.PointBase;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.CompleteTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompleteTimeScoreWrapper  <E extends TimePoint> extends TimeScoreWrapper{
    public CompleteTimeScoreWrapper(int scoreStart, int scoreEnd) {
        super(scoreStart, scoreEnd,0);
    }

    //copied from RatingTimeScoreWrapper
    public List<E> calcTimePoints(List<List<CompleteTime>> ctsList) {
        List<E> points = new ArrayList<>();
        for (List<CompleteTime> cts : ctsList) {
            List<TimePoint> tps = calcBmPoints(cts);
            for (TimePoint tp: tps) {
                points.add((E)tp);
            }
        }
        return points;
    }

    private List<TimePoint> calcBmPoints(List<CompleteTime> cts) {
        return TimePointMaker.doBMTimePoints(cts, scoreStart, scoreEnd);
    }


    @Override
    public List<TimePoint> calcPoints(List<Chunk> chunks) {
        return null;
    }
}
