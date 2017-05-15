package com.ibsanalyzer.model;

import com.ibsanalyzer.base_classes.Event;

import java.util.List;

/**
 * Created by Johan on 2017-05-15.
 */

public class EventsTemplate {
    List<Event> events;

    public EventsTemplate(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
