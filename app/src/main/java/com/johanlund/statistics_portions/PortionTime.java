package com.johanlund.statistics_portions;

import org.threeten.bp.LocalDateTime;

/**
 * Created by Johan on 2018-04-05.
 * <p>
 * Is replacing Meal in Portion Stats for performance reasons (the main gain is that it will be
 * smaller load for Cursor to have this in memory than meals).
 */

public class PortionTime {
    private double portionSize;
    private LocalDateTime dateTime;

    public PortionTime(double portionSize, LocalDateTime dateTime) {
        this.portionSize = portionSize;
        this.dateTime = dateTime;
    }

    //copy constructor
    public PortionTime (PortionTime ptOrig){
        this.portionSize = ptOrig.portionSize;

        //localdatetime is inmutable
        this.dateTime = ptOrig.getTime();
    }
    public double getPSize() {
        return portionSize;
    }

    public LocalDateTime getTime() {
        return dateTime;
    }

}

