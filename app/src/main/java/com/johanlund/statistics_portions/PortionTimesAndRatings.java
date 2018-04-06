package com.johanlund.statistics_portions;

import com.johanlund.base_classes.Rating;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by Johan on 2018-04-05.
 */

public class PortionTimesAndRatings {
    List<PortionTime>portionTimes;
    List<Rating>ratings;
    //this is equivalent to last event in chunk.
    LocalDateTime lastTimeInChunk;

    public PortionTimesAndRatings(List<PortionTime> portionTimes, List<Rating> ratings,
                                  LocalDateTime lastTimeInChunk) {
        this.portionTimes = portionTimes;
        this.ratings = ratings;
        this.lastTimeInChunk = lastTimeInChunk;
    }

    public List<PortionTime> getPortionTimes() {
        return portionTimes;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public LocalDateTime getLastTimeInChunk() {
        return lastTimeInChunk;
    }

    public void setPortionTimes(List<PortionTime> portionTimes) {
        this.portionTimes = portionTimes;
    }
}
