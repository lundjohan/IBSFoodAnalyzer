package com.ibsanalyzer.database;

/**
 * Created by Johan on 2017-05-15.
 */

public class TablesAndStrings {
    //NO_INHERITANCE is used to say: "TagTemplate is not inheriting"
    public static final String NO_INHERITANCE = "0Null0";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tagnameDB.db";

    //Foreign key support
    public static final String ENABLE_FOREIGN_KEYS = "PRAGMA foreign_keys=ON;";
    //for all
    public static final String COLUMN_ID = "_id";

    //TagTemplate
    public static final String TABLE_TAGTEMPLATES = "tag_templates";
    public static final String COLUMN_TAGNAME = "_tagname"; //this should be unique
    public static final String COLUMN_IS_A = "_is_a1";      //make it point to parent TagName and not to id, it make it possible to display in listview after filtering.


    //Tag
    public static final String TABLE_TAGS = "tags";
    public static final String COLUMN_TAGTEMPLATE = "tagtemplate";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_EVENT = "event";

    //Event
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_TAGS = "tags";

    //Event => many-to-many =>  Tags
    public static final String TABLE_EVENTTAGS = "event_tags";
    public static final String COLUMN_TAG = "tag";

    //Meals
    public static final String TABLE_MEALS = "meals";
    public static final String COLUMN_PORTIONS = "portions";

    //Other
    public static final String TABLE_OTHERS = "others";

    //Exercise
    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_INTENSITY = "intensity";

    //BMs
    public static final String TABLE_BMS = "bms";
    public static final String COLUMN_COMPLETENESS = "completeness";
    public static final String COLUMN_BRISTOL = "bristol";

    //Ratings
    public static final String TABLE_RATINGS = "ratings";
    public static final String COLUMN_AFTER = "after";

    //EventsTemplates
    public static final String TABLE_EVENTSTEMPLATES = "event_templates";
    public static final String COLUMN_NAME = "eventtemplate_name";

    //EventsTemplate => many-to-many => Events
    public static final String TABLE_EVENTSTEMPLATEEVENTS = "event_template_events";
    public static final String COLUMN_EVENTSTEMPLATE = "events_template";   //denna => unknown column?

    // see https://sqlite.org/foreignkeys.html for creation of foreign keys.
    public static final String CREATE_TAGTEMPLATE_TABLE = "CREATE TABLE " +
            TABLE_TAGTEMPLATES + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_TAGNAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_IS_A + " TEXT CHECK( " + COLUMN_IS_A + " != " + COLUMN_TAGNAME + "),  " +
            " FOREIGN KEY( " + COLUMN_IS_A + ") REFERENCES " + TABLE_TAGTEMPLATES
            + " ( " + COLUMN_TAGNAME + ")" +
            ");";
     public static final String CREATE_TAG_TABLE = "CREATE TABLE " +
            TABLE_TAGS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TAGTEMPLATE + " INTEGER NOT NULL, " +
            COLUMN_SIZE + " REAL NOT NULL, " +
            COLUMN_DATE + " TEXT NOT NULL, " +
            COLUMN_EVENT       + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_TAGTEMPLATE + ") REFERENCES " + TABLE_TAGTEMPLATES
            + " ( " + COLUMN_ID + ")" +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" +
            ");";


    public static final String CREATE_EVENT_TABLE = "CREATE TABLE " +
            TABLE_EVENTS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_DATE + " TEXT NOT NULL " +
            ");";

    public static final String CREATE_MEAL_TABLE = "CREATE TABLE " +
            TABLE_MEALS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_PORTIONS + " REAL NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" +
            ");";

    public static final String CREATE_OTHER_TABLE = "CREATE TABLE " +
            TABLE_OTHERS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" +
            ");";

    public static final String CREATE_EXERCISE_TABLE = "CREATE TABLE " +
            TABLE_EXERCISES + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_INTENSITY + "INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" +
            ");";

    public static final String CREATE_BM_TABLE = "CREATE TABLE " +
            TABLE_BMS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_COMPLETENESS + "INTEGER NOT NULL, " +
            COLUMN_BRISTOL + "INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" +
            ");";
    public static final String CREATE_RATING_TABLE = "CREATE TABLE " +
            TABLE_RATINGS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_AFTER + "INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" +
            ");";
    public static final String CREATE_EVENTS_TEMPLATE_TABLE = "CREATE TABLE " +
            TABLE_EVENTSTEMPLATES + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME + " TEXT NOT NULL UNIQUE ," +
            " FOREIGN KEY( " + COLUMN_ID + ") REFERENCES " + TABLE_EVENTSTEMPLATEEVENTS
            + " ( " + COLUMN_EVENTSTEMPLATE + ")" +
            ");";
    public static final String CREATE_EVENTS_TEMPLATE_TO_EVENT_TABLE = "CREATE TABLE " +
            TABLE_EVENTSTEMPLATEEVENTS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_EVENTSTEMPLATE + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ") " +
            " FOREIGN KEY( " + COLUMN_EVENTSTEMPLATE + " ) REFERENCES " + TABLE_EVENTSTEMPLATES
            + " ( " + COLUMN_ID + ")" +
            ");";
}
