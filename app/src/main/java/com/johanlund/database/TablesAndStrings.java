package com.johanlund.database;

/**
 * Created by Johan on 2017-05-15.
 */

public class TablesAndStrings {
    public static final int DATABASE_VERSION = 41;
    public static final String DATABASE_NAME = "foodanalyzer.db";

    //Foreign key support
    public static final String ENABLE_FOREIGN_KEYS = "PRAGMA foreign_keys=ON;";
    //for all
    public static final String COLUMN_ID = "_id";

    //TagType
    public static final String TABLE_TAGTYPES = "tag_templates";
    public static final String COLUMN_TAGNAME = "_tagname"; //this should be unique
    //make it point to parent TagName and
    // not to id, it make it possible to display in listview after filtering.
    public static final String TYPE_OF = "_is_a1";

    //Tag
    public static final String TABLE_TAGS = "tags";
    public static final String COLUMN_TAGTYPE = "tagtemplate";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_DATETIME = "date";
    //public static final String COLUMN_DATE = "date_without_time";
    public static final String COLUMN_EVENT = "event";

    //Event
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_HAS_BREAK = "has_break";

    //Event => many-to-many =>  Tags
    public static final String COLUMN_TYPE_OF_EVENT = "type_of_event";

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
    public static final String TMP_TABLE_RATINGS = "tmp_ratings_table";
    public static final String TMP_TABLE_TAGS = "tmp_tags_table";
    public static final String TMP_TABLE_EVENTS = "tmp_events_table";
    public static final String COLUMN_AFTER = "after_score";

    //EventsTemplates
    public static final String TABLE_EVENTSTEMPLATES = "event_templates";
    public static final String COLUMN_NAME = "eventtemplate_name";

    public static final String COLUMN_EVENTSTEMPLATE = "events_template";   //denna => unknown
    // column?

    // is_a is set to null when parent tag template is deleted
    public static final String CREATE_TAGTYPE_TABLE = "CREATE TABLE " +
            TABLE_TAGTYPES + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_TAGNAME + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, " +
            TYPE_OF + " INTEGER CHECK( " + TYPE_OF + " != " + COLUMN_ID + ")," +
            "  " +
            " FOREIGN KEY( " + TYPE_OF + ") REFERENCES " + TABLE_TAGTYPES
            + " ( " + COLUMN_ID + ")" + " ON DELETE SET NULL " +
            ");";
    public static final String CREATE_TAG_TABLE = "CREATE TABLE " +
            TABLE_TAGS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TAGTYPE + " INTEGER NOT NULL, " +
            COLUMN_SIZE + " REAL NOT NULL, " +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_TAGTYPE + ") REFERENCES " + TABLE_TAGTYPES
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            ");";

    /*Important! Note that this variable is also used inside onUpdate.
    So if this is changed it also has to change there*/
    public static final String CREATE_EVENT_TABLE = "CREATE TABLE " +
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

    public static final String CREATE_MEAL_TABLE = " CREATE TABLE " +
            TABLE_MEALS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_PORTIONS + " REAL NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            ");";

    public static final String CREATE_OTHER_TABLE = " CREATE TABLE " +
            TABLE_OTHERS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            ");";

    public static final String CREATE_EXERCISE_TABLE = " CREATE TABLE " +
            TABLE_EXERCISES + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_INTENSITY + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            ");";

    public static final String CREATE_BM_TABLE = "CREATE TABLE " +
            TABLE_BMS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_COMPLETENESS + " INTEGER NOT NULL, " +
            COLUMN_BRISTOL + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            ");";
    public static final String CREATE_RATING_TABLE = "CREATE TABLE " +
            TABLE_RATINGS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_AFTER + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            ");";
    //This table refers to the Template class that stores one event or more for later copying.
    public static final String CREATE_EVENTS_TEMPLATE_TABLE = "CREATE TABLE " +
            TABLE_EVENTSTEMPLATES + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME + " TEXT NOT NULL UNIQUE " +
            ");";
}
