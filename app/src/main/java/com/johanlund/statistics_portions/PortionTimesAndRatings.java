package com.johanlund.statistics_portions;

import com.johanlund.base_classes.Rating;

import java.util.List;

/**
 * Created by Johan on 2018-04-05.
 */

public class PortionTimesAndRatings {
    List<PortionTime>portionTimes;
    List<Rating>ratings;

    public PortionTimesAndRatings(List<PortionTime> portionTimes, List<Rating> ratings) {
        this.portionTimes = portionTimes;
        this.ratings = ratings;
    }

    public List<PortionTime> getPortionTimes() {
        return portionTimes;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setPortionTimes(List<PortionTime> portionTimes) {
        this.portionTimes = portionTimes;
    }
}
