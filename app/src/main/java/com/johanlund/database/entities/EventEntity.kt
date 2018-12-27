package com.johanlund.database.entities

/*
TABLE_EVENTS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_DATETIME + " TEXT NOT NULL, " +
           // COLUMN_DATE + " TEXT NOT NULL, " +
            COLUMN_TYPE_OF_EVENT + " INTEGER NOT NULL, " +
            //this column is only used when event is inside a EventsTemplate, otherwise null
            COLUMN_EVENTSTEMPLATE + " INTEGER, " +
            COLUMN_COMMENT + " TEXT, " +
            /*SQLite does not have a separate Boolean storage class. Instead, Boolean values are
            stored as integers 0 (false) and 1 (true).
            this column should always be 0 (false) inside an EventsTemplate*/
            COLUMN_HAS_BREAK + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENTSTEMPLATE + " ) REFERENCES " + TABLE_EVENTSTEMPLATES
            + " ( " + COLUMN_ID + ") ON DELETE CASCADE " +
            ");";
 */
open class EventEntity(
        val id: Int,
        val dateTime: String,
        val typeOfEvent: Int,
        //we don't use eventstemplate for now
        val comment: String,
        val hasBreak: Boolean = false
) : EntityBase()
