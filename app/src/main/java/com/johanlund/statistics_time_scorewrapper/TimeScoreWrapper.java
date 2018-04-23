package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_general.ScoreWrapperBase;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.ScoreTimesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Johan on 2018-03-20.
 */

public abstract class TimeScoreWrapper extends ScoreWrapperBase<TimePoint>{
    //incl
    int scoreStart;
    //incl
    int scoreEnd;
    int durationLimitInHours;

    public TimeScoreWrapper(int scoreStart, int scoreEnd, int durationLimitInHours) {
        this.scoreStart = scoreStart;
        this.scoreEnd = scoreEnd;
        this.durationLimitInHours = durationLimitInHours;
    }

    @Override
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


    @Override
    protected boolean quantIsOverLimit(TimePoint tp){
        return tp.getDurationInHours()>=durationLimitInHours;
    }

    //copied from RatingTimeScoreWrapper
    public List<TimePoint> calcTimePoints(List<ScoreTimesBase> stsList) {
        List<TimePoint> points = new ArrayList<>();
        for (ScoreTimesBase sts : stsList) {
            List<TimePoint> tps = doCalc(sts);
            for (TimePoint tp: tps) {
                points.add(tp);
            }
        }
        return points;
    }

    protected abstract List<TimePoint> doCalc(ScoreTimesBase stb);
    @Override
    public List<TimePoint> calcPoints(List<Chunk> chunks) {
        return null;
    }

}

