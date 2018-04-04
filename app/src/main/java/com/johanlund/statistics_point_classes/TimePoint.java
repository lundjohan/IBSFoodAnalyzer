package com.johanlund.statistics_point_classes;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

/**
 * Created by Johan on 2018-03-18.
 * <p>
 * Holder for values of TimeStats
 * <p>
 * stopTime could also be "duration" with long type, for example, but I think this solution will
 * end up easier.
 */

public class TimePoint implements PointBase{
    LocalDateTime startTime;
    LocalDateTime stopTime;

    public TimePoint(LocalDateTime startTime, LocalDateTime stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public long getDurationInHours() {
        long secDiff = stopTime.atZone(ZoneId.systemDefault()).toEpochSecond() - startTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        //some truncation is allowed
        return secDiff/(60*60);
    }
}
