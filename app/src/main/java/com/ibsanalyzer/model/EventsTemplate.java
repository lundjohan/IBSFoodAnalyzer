package com.ibsanalyzer.model;

import com.ibsanalyzer.base_classes.Event;

import java.util.List;

/**
 * Created by Johan on 2017-05-15.
 */

public class EventsTemplate {
    String nameOfTemplate;
    List<Event> events;




    public EventsTemplate(List<Event> events, String name) {
        this.events = events;
        this.nameOfTemplate = name;
    }

    public List<Event> getEvents() {
        return events;
    }
    public String getNameOfTemplate() {
        return nameOfTemplate;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
