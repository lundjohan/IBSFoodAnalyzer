package com.johanlund.database.entities

/*
TABLE_MEALS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_PORTIONS + " REAL NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            ");";
 */
class MealEntity(val portions: Double, id: Int, dateTime: String,
                 typeOfEvent: Int, comment: String = "", hasBreak: Boolean)
    : EventEntity(id, dateTime, typeOfEvent, comment, hasBreak)


