package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.TimePoint;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-03-20.
 */

public abstract class TimeScoreWrapper {
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

    public abstract List<TimePoint> toSortedList(List<TimePoint> timePoints);


    public abstract List<TimePoint> calcTimePeriods(List<Chunk> chunks);

    public List<TimePoint> removeTimePointsWithTooLowQuant(List<TimePoint> sortedList){
        List<TimePoint>trimmedTimePointList = new ArrayList<>();
        for (TimePoint tp:sortedList){
            if(tp.getDurationInHours()>=durationLimitInHours){
                trimmedTimePointList.add(tp);
            }
        }
        return trimmedTimePointList;
    }

}

