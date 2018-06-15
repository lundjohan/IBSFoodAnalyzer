package com.johanlund.model;

import android.content.Context;

import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;

import org.threeten.bp.LocalDateTime;

/**
 * This class encapsulates the logic related to Event, including interactions with MVC model (The
 * model is (currently) parts of database).
 */
public class EventManager {
    private DBHandler dbHandler;

    public EventManager(Context context) {
        this.dbHandler = new DBHandler(context);
    }

    public boolean eventTypeAtSameTimeAlreadyExists(int type, LocalDateTime ldt) {
        return dbHandler.eventDoesExistOutsideOfEventsTemplate(type, ldt);
    }

    public Event fetchEventById(long id){
        return dbHandler.retrieveEvent(id);
    }
}
