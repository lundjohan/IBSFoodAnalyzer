package com.johanlund.statistics_time_scorewrapper;

import android.graphics.Point;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_general.ScoreWrapperBase;
import com.johanlund.statistics_point_classes.TagPoint;
import com.johanlund.statistics_point_classes.TimePoint;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

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
}

