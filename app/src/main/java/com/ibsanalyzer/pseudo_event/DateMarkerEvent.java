package com.ibsanalyzer.pseudo_event;

import com.ibsanalyzer.base_classes.Event;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

/**
 * Created by Johan on 2017-05-28.
 */

public class DateMarkerEvent extends Event {
    LocalDate date;

    public DateMarkerEvent(LocalDate date) {
        super(LocalDateTime.of(date, LocalTime.MAX));
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date.toString();
    }
}
