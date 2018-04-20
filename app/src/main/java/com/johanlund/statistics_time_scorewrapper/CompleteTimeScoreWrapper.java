package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.stat_classes.TimePointMaker;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.CompleteTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompleteTimeScoreWrapper  extends TimeScoreWrapper{
    //incl
    int scoreStart;
    //incl
    int scoreEnd;

    public CompleteTimeScoreWrapper(int scoreStart, int scoreEnd) {
        this.scoreStart = scoreStart;
        this.scoreEnd = scoreEnd;
    }

    //copied from RatingTimeScoreWrapper
    public List<TimePoint> calcPoints(List<List<CompleteTime>> ctsList) {
        List<TimePoint> points = new ArrayList<>();
        for (List<CompleteTime> cts : ctsList) {
            points.addAll(calcBmPoints(cts));
        }
        return points;
    }

    private List<TimePoint> calcBmPoints(List<CompleteTime> cts) {
        return TimePointMaker.doBMTimePoints(cts, scoreStart, scoreEnd);
    }


    public List<TimePoint> toSortedList(List<TimePoint> timePoints){
        Collections.sort(timePoints, new Comparator<TimePoint>()
                {
                    @Override
                    public int compare(TimePoint t1, TimePoint t2)
                    {
                        return (int)((t1.getDurationInHours()- t2.getDurationInHours()));
                    }
                }
        );
        return timePoints;
    }
}
