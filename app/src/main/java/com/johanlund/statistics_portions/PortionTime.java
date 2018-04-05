package com.johanlund.statistics_portions;

import org.threeten.bp.LocalDateTime;

/**
 * Created by Johan on 2018-04-05.
 * <p>
 * Is replacing Meal in Portion Stats for performance reasons (the main gain is that it will be
 * smaller load for Cursor to have this in memory than meals).
 */

public class PortionTime {
    private double portions;
    private LocalDateTime dateTime;

    public PortionTime(double portions, LocalDateTime dateTime) {
        this.portions = portions;
        this.dateTime = dateTime;
    }

    public double getPortions() {
        return portions;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

