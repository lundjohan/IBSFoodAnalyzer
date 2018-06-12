package com.johanlund.model;

import android.content.Context;

import com.johanlund.base_classes.Tag;
import com.johanlund.database.DBHandler;

import org.threeten.bp.LocalDateTime;

import java.util.List;


public abstract class EventModel {
    public static boolean eventTypeAtSameTimeAlreadyExists(int type, LocalDateTime ldt, Context context) {
        DBHandler dbHandler = new DBHandler(context);
        return dbHandler.eventDoesExistOutsideOfEventsTemplate(type, ldt);
    }
}