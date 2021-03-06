package com.johanlund.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.InputEvent;
import com.johanlund.base_classes.Rating;
import com.johanlund.base_classes.Tag;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.database.entities.BmEntity;
import com.johanlund.database.entities.ExerciseEntity;
import com.johanlund.database.entities.MealEntity;
import com.johanlund.database.entities.OtherEntity;
import com.johanlund.database.entities.RatingEntity;
import com.johanlund.database.entities.TagEntity;
import com.johanlund.database.entities.TagTypeEntity;
import com.johanlund.date_time.DateTimeFormat;
import com.johanlund.exceptions.InvalidEventType;
import com.johanlund.model.EventsTemplate;
import com.johanlund.model.TagType;
import com.johanlund.stat_backend.stat_util.ScoreTime;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.StringCompanionObject;

import static com.johanlund.constants.Constants.BM;
import static com.johanlund.constants.Constants.EXERCISE;
import static com.johanlund.constants.Constants.MEAL;
import static com.johanlund.constants.Constants.OTHER;
import static com.johanlund.constants.Constants.RATING;
import static com.johanlund.database.TablesAndStrings.COLUMN_AFTER;
import static com.johanlund.database.TablesAndStrings.COLUMN_BRISTOL;
import static com.johanlund.database.TablesAndStrings.COLUMN_COMMENT;
import static com.johanlund.database.TablesAndStrings.COLUMN_COMPLETENESS;
import static com.johanlund.database.TablesAndStrings.COLUMN_DATETIME;
import static com.johanlund.database.TablesAndStrings.COLUMN_EVENT;
import static com.johanlund.database.TablesAndStrings.COLUMN_EVENTSTEMPLATE;
import static com.johanlund.database.TablesAndStrings.COLUMN_HAS_BREAK;
import static com.johanlund.database.TablesAndStrings.COLUMN_ID;
import static com.johanlund.database.TablesAndStrings.COLUMN_INTENSITY;
import static com.johanlund.database.TablesAndStrings.COLUMN_NAME;
import static com.johanlund.database.TablesAndStrings.COLUMN_PORTIONS;
import static com.johanlund.database.TablesAndStrings.COLUMN_SIZE;
import static com.johanlund.database.TablesAndStrings.COLUMN_TAGNAME;
import static com.johanlund.database.TablesAndStrings.COLUMN_TAGTYPE;
import static com.johanlund.database.TablesAndStrings.COLUMN_TYPE_OF_EVENT;
import static com.johanlund.database.TablesAndStrings.CREATE_BM_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_EVENTS_TEMPLATE_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_EVENT_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_EXERCISE_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_MEAL_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_OTHER_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_RATING_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_TAGTYPE_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_TAG_TABLE;
import static com.johanlund.database.TablesAndStrings.DATABASE_NAME;
import static com.johanlund.database.TablesAndStrings.DATABASE_VERSION;
import static com.johanlund.database.TablesAndStrings.ENABLE_FOREIGN_KEYS;
import static com.johanlund.database.TablesAndStrings.TABLE_BMS;
import static com.johanlund.database.TablesAndStrings.TABLE_EVENTS;
import static com.johanlund.database.TablesAndStrings.TABLE_EVENTSTEMPLATES;
import static com.johanlund.database.TablesAndStrings.TABLE_EXERCISES;
import static com.johanlund.database.TablesAndStrings.TABLE_MEALS;
import static com.johanlund.database.TablesAndStrings.TABLE_OTHERS;
import static com.johanlund.database.TablesAndStrings.TABLE_RATINGS;
import static com.johanlund.database.TablesAndStrings.TABLE_TAGS;
import static com.johanlund.database.TablesAndStrings.TABLE_TAGTYPES;
import static com.johanlund.database.TablesAndStrings.TMP_TABLE_EVENTS;
import static com.johanlund.database.TablesAndStrings.TMP_TABLE_RATINGS;
import static com.johanlund.database.TablesAndStrings.TMP_TABLE_TAGS;
import static com.johanlund.database.TablesAndStrings.TYPE_OF;

/**
 * Created by Johan on 2017-05-06.
 * see p. 554
 * see https://sqlite.org/foreignkeys.html for creation of foreign keys.
 */

public class DBHandler extends SQLiteOpenHelper {
    private final String TAG = DBHandler.class.toString();
    private String CHANGE_TAG_TABLE_NAME = " ALTER TABLE " + TABLE_TAGS + " RENAME TO " +
            TMP_TABLE_TAGS + "; ";
    private String CREATE_NEW_TAG_TABLE = "CREATE TABLE " + TABLE_TAGS + " (  " +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TAGTYPE + " INTEGER NOT NULL, " +
            COLUMN_SIZE + " REAL NOT NULL, " +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            " FOREIGN KEY( " + COLUMN_TAGTYPE + ") REFERENCES " + TABLE_TAGTYPES
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE, " + " FOREIGN KEY( " + COLUMN_EVENT
            + ") REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " +
            ");";
    private String COPY_CONTENTS_TO_NEW_TAG_TABLE = " INSERT INTO " + TABLE_TAGS + " ( " +
            COLUMN_ID + ", " + COLUMN_TAGTYPE + ", " + COLUMN_SIZE + ", " + COLUMN_EVENT + " ) " +
            " SELECT " + COLUMN_ID + ", " + COLUMN_TAGTYPE + ", " + COLUMN_SIZE + ", " +
            COLUMN_EVENT + " FROM " + TMP_TABLE_TAGS;
    private String DROP_OLD_TAG_TABLE = "DROP TABLE " + TMP_TABLE_TAGS;
    //Before the column's name was 'after' which is a keyword in SQLITE syntax
    private String CHANGE_RATING_TABLE_NAME = " ALTER TABLE " + TABLE_RATINGS + " RENAME TO " +
            TMP_TABLE_RATINGS + "; ";
    private String CREATE_NEW_RATING_TABLE = "CREATE TABLE " + TABLE_RATINGS + " (  " + COLUMN_ID
            + " INTEGER PRIMARY KEY," + COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_AFTER + " INTEGER NOT NULL, " + " FOREIGN KEY( " + COLUMN_EVENT + ") " +
            "REFERENCES " + TABLE_EVENTS
            + " ( " + COLUMN_ID + ")" + " ON DELETE CASCADE " + ");";
    private String COPY_CONTENTS_TO_NEW_TABLE = " INSERT INTO " + TABLE_RATINGS + " ( " +
            COLUMN_ID + ", " + COLUMN_EVENT + ", " + COLUMN_AFTER + ") " +
            " SELECT " + COLUMN_ID + ", " + COLUMN_EVENT + ", after FROM " + TMP_TABLE_RATINGS;
    private String DROP_OLD_TABLE = "DROP TABLE " + TMP_TABLE_RATINGS;
    private String CHANGE_RATING_SCALE =
            "UPDATE " + TABLE_RATINGS + " SET " +
                    COLUMN_AFTER + " = " + COLUMN_AFTER + " -1 " + " WHERE " + COLUMN_AFTER + " " +
                    "!= '1' ";
    private String CHANGE_EVENT_TABLE_NAME = "ALTER TABLE "+ TABLE_EVENTS + " RENAME TO "+ TMP_TABLE_EVENTS + ";";
    private String CREATE_NEW_EVENT_TABLE_WITHOUT_DATE_COL = CREATE_EVENT_TABLE;
    private String INSERT_CONTENTS_TO_NEW_EVENT_TABLE = "INSERT INTO " +TABLE_EVENTS + " ( " +
            COLUMN_ID + ", " + COLUMN_DATETIME + ", " + COLUMN_TYPE_OF_EVENT + ", " + COLUMN_EVENTSTEMPLATE + ", " + COLUMN_COMMENT + ", " + COLUMN_HAS_BREAK +") " +
            " SELECT " + COLUMN_ID + ", " + COLUMN_DATETIME + ", " + COLUMN_TYPE_OF_EVENT + ", " + COLUMN_EVENTSTEMPLATE + ", " + COLUMN_COMMENT + ", " + COLUMN_HAS_BREAK +
            " FROM " + TMP_TABLE_EVENTS;
    private String DROP_OLD_EVENT_TABLE = "DROP TABLE " + TMP_TABLE_EVENTS;
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL(ENABLE_FOREIGN_KEYS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ENABLE_FOREIGN_KEYS);

        db.execSQL(CREATE_TAGTYPE_TABLE);
        db.execSQL(CREATE_TAG_TABLE);

        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_MEAL_TABLE);
        db.execSQL(CREATE_OTHER_TABLE);
        db.execSQL(CREATE_EXERCISE_TABLE);
        db.execSQL(CREATE_BM_TABLE);
        db.execSQL(CREATE_RATING_TABLE);

        db.execSQL(CREATE_EVENTS_TEMPLATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        //Table Events no longer contains row COLUMN_DATE
        if (oldVersion <= 40) {
            db.beginTransaction();
            try {
                // Changing name of column
                // Before the column's name was 'after' which is a keyword in SQLITE syntax
                db.execSQL(CHANGE_EVENT_TABLE_NAME);
                db.execSQL(CREATE_NEW_EVENT_TABLE_WITHOUT_DATE_COL);
                db.execSQL(INSERT_CONTENTS_TO_NEW_EVENT_TABLE);
                db.execSQL(DROP_OLD_EVENT_TABLE);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        //action done to remove time dependency of Tag Table
        if (oldVersion <= 39) {
            db.beginTransaction();
            try {
                // Changing name of column
                // Before the column's name was 'after' which is a keyword in SQLITE syntax
                db.execSQL(CHANGE_TAG_TABLE_NAME);
                db.execSQL(CREATE_NEW_TAG_TABLE);
                db.execSQL(COPY_CONTENTS_TO_NEW_TAG_TABLE);
                db.execSQL(DROP_OLD_TAG_TABLE);


                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        //this must be done before CHANGE_RATING_SCALE
        if (oldVersion <= 38) {
            db.beginTransaction();
            try {
                // Changing name of column
                // Before the column's name was 'after' which is a keyword in SQLITE syntax
                db.execSQL(CHANGE_RATING_TABLE_NAME);
                db.execSQL(CREATE_NEW_RATING_TABLE);
                db.execSQL(COPY_CONTENTS_TO_NEW_TABLE);
                db.execSQL(DROP_OLD_TABLE);


                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

        }

        if (oldVersion <= 37) {
            //since this version Rating scale has been reduced to 6 from 7. 'Abysmal' has
            // disappeared and will be joined with 'Awful'.
            db.beginTransaction();
            try {
                db.execSQL(CHANGE_RATING_SCALE);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    //-----------------------------------------------------------------------------------
    //get cursor methods
    //-----------------------------------------------------------------------------------
    public Cursor getCursorToTagTemplates() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT  * FROM " + TABLE_TAGTYPES, null);
    }

    public Cursor fetchTagTemplatesByName(String inputText) throws SQLException {
        Cursor mCursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        if (inputText == null || inputText.length() == 0) {

            mCursor = db.query(TABLE_TAGTYPES, new String[]{COLUMN_ID,
                            COLUMN_TAGNAME, TYPE_OF},
                    null, null, null, null, null);

        } else {
            mCursor = db.query(true, TABLE_TAGTYPES, new String[]{COLUMN_ID,
                            COLUMN_TAGNAME, TYPE_OF},
                    COLUMN_TAGNAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    //===================================================================================
    //for all
    //===================================================================================
    public void deleteAllTablesRows() {
        deleteAllTablesRowsExceptTagTypes();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGTYPES, null, null);
        db.close();
    }

    public void deleteAllTablesRowsExceptTagTypes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTSTEMPLATES, null, null);
        db.delete(TABLE_EVENTS, null, null);
        db.delete(TABLE_RATINGS, null, null);
        db.delete(TABLE_BMS, null, null);
        db.delete(TABLE_EXERCISES, null, null);
        db.delete(TABLE_OTHERS, null, null);
        db.delete(TABLE_MEALS, null, null);
        db.delete(TABLE_TAGS, null, null);
        db.close();
    }

    //==============================================================================================
    //EventsTemplate functions
    //==============================================================================================
    public void addEventsTemplate(EventsTemplate et) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, et.getNameOfTemplate());
        SQLiteDatabase db = this.getWritableDatabase();
        long template_id = db.insert(TABLE_EVENTSTEMPLATES, null, values);
        db.close();
        for (Event e : et.getEvents()) {
            int type = e.getType();
            long idOfEventAdded = addEventToRefEventsTemplate(e, type, template_id);
            addToChildrenOfEventTables(idOfEventAdded, e, type);
        }
    }

    /**
     * Call this method after event table has been added and Meal, Other etc Tables should be
     * added next
     */
    private void addToChildrenOfEventTables(long idOfEvent, Event e, int type) {
        //here: add Meal, Other etc based on above
        switch (type) {
            case MEAL: {
                addToMealTable(idOfEvent, (com.johanlund.base_classes.Meal) e);
                break;
            }
            case OTHER: {
                addToOtherTable(idOfEvent, (com.johanlund.base_classes.Other) e);
                break;
            }
            case EXERCISE: {
                addToExerciseTable(idOfEvent, (com.johanlund.base_classes.Exercise) e);
                break;
            }
            case BM: {
                addToBmTable(idOfEvent, (com.johanlund.base_classes.Bm) e);
                break;
            }
            case RATING: {
                addToRatingTable(idOfEvent, (Rating) e);
                break;
            }
        }
    }

    //if you want position in using list to remain the same, this have to be implemented differently
    public void editEventsTemplate(long id, EventsTemplate changedET) {
        deleteEventsTemplate(id);
        addEventsTemplate(changedET);
    }

    public void deleteEventsTemplate(long eventsTemplateId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTSTEMPLATES, COLUMN_ID + "=?", new String[]{String.valueOf
                (eventsTemplateId)});
        db.close();
    }

    public Cursor getCursorToEventsTemplates() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT  * FROM " + TABLE_EVENTSTEMPLATES, null);
    }

    public EventsTemplate getEventsTemplate(long id) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT " + COLUMN_NAME + " FROM " + TABLE_EVENTSTEMPLATES + " WHERE" +
                " " +
                COLUMN_ID + " = ?";
        Cursor c1 = db.rawQuery(QUERY, new String[]{String.valueOf(id)});
        if (c1 != null) {
            if (c1.moveToFirst()) {
                name = c1.getString(c1.getColumnIndex(COLUMN_NAME));
            }
        }
        db.close();
        EventsTemplate toReturn = getEventsTemplate(id, name);
        return toReturn;
    }

    private EventsTemplate getEventsTemplate(long eventsTemplateId, String name) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY_EVENTS = "SELECT * FROM " + TABLE_EVENTS + " WHERE " +
                COLUMN_EVENTSTEMPLATE + " = ?";
        Cursor c = db.rawQuery(QUERY_EVENTS, new String[]{String.valueOf(eventsTemplateId)});
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    try {
                        long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                        Event e = getEvent(eventId, c);
                        events.add(e); //här: e == null
                    } catch (InvalidEventType e) {
                        //Log.e("CorruptedEvent ", e.getMessage());
                    }
                    c.moveToNext();
                }
            }
        }
        c.close();
        db.close();
        EventsTemplate toReturn = new EventsTemplate(events, name);


        return toReturn;
    }


    //===================================================================================
    //Event
    //===================================================================================
    //-----------------------------------------------------------------------------------
    //add
    //-----------------------------------------------------------------------------------

    /**
     * Used for all cases when an Event of any type should be added.
     * NB. EventsTemplates should NOT use this method.
     *
     * @param e
     */
    public void addEvent(Event e) {
        int type = e.getType();
        switch (type) {
            case MEAL:
                addMeal((com.johanlund.base_classes.Meal) e);
                break;
            case OTHER:
                addOther((com.johanlund.base_classes.Other) e);
                break;
            case EXERCISE:
                addExercise((com.johanlund.base_classes.Exercise) e);
                break;
            case BM:
                addBm((com.johanlund.base_classes.Bm) e);
                break;
            case RATING:
                addRating((Rating) e);
                break;
        }
    }

    public void addEvents(List<Event> events) {
        for (Event e : events) {
            addEvent(e);
        }
    }

    /**
     * This is used when creating Events inside an EventsTemplate
     * <p>
     * returns id of event added
     */
    private long addEventToRefEventsTemplate(Event event, long typeOfEvent, long
            idOfEventsTemplate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENTSTEMPLATE, idOfEventsTemplate);
        //break should always be 0 (false) inside a EventsTemplate
        values.put(COLUMN_HAS_BREAK, 0);
        return addEventHelper1(event, values, typeOfEvent);
    }

    /**
     * This is used when creating Events NOT inside an EventsTemplate.
     * This is the "normal" use case.
     */
    private long addEventBase(Event e, long typeOfEvent) {
        ContentValues values = new ContentValues();
        //conditional/ ternary operator. "If e hasBreak, store a 1 (true), else store a 0(false)"
        values.put(COLUMN_HAS_BREAK, e.hasBreak() ? 1 : 0);
        return addEventHelper1(e, values, typeOfEvent);
    }

    private long addEventHelper1(Event e, ContentValues values, long typeOfEvent) {
        values.put(COLUMN_DATETIME, DateTimeFormat.toSqLiteFormat(e.getTime()));
        //values.put(COLUMN_DATE, DateTimeFormat.dateToSqLiteFormat(e.getTime().toLocalDate())); deprecated since databaseversion 41
        values.put(COLUMN_TYPE_OF_EVENT, typeOfEvent);
        values.put(COLUMN_COMMENT, e.getComment());
        return addEventHelper2(e, values);
    }

    private long addEventHelper2(Event event, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long eventId = db.insert(TABLE_EVENTS, DATABASE_NAME, values);
        //if inputEvent (Meal, Other)=> add tags
        if (event instanceof InputEvent) {
            InputEvent ie = (InputEvent) event;
            for (TagWithoutTime t : ie.getTagsWithoutTime()) {
                addTagAndPossiblyTagTemplate(t, eventId);

            }
        }
        //Exercise is does not inherit InputEvent but has one tag
        else if (event instanceof com.johanlund.base_classes.Exercise) {
            TagWithoutTime t = ((com.johanlund.base_classes.Exercise) event).getTypeOfExercise();
            addTagAndPossiblyTagTemplate(t, eventId);
        }
        db.close();
        return eventId;
    }

    //-----------------------------------------------------------------------------------
    //delete
    //-----------------------------------------------------------------------------------
    public void deleteEvent(Event event) {
        long eventId = getEventIdOutsideEventsTemplate(event);
        if (eventId < 0) {
            throw new RuntimeException("Trying to delete event with invalid eventId");
        }
        deleteEvent(getEventIdOutsideEventsTemplate(event));
    }

    /**
     * @param eventId should exist in database!
     */
    private void deleteEvent(long eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, COLUMN_ID + "=?", new String[]{String.valueOf(eventId)});
        db.close();
    }

    //-----------------------------------------------------------------------------------
    //gets
    //-----------------------------------------------------------------------------------

    /**
     * This method is only used in specific case , and it does not take into account comments or
     * breaks.
     *
     * @param typeOfEvent
     * @param ldt
     * @return true if it exists like a normal event inside diary.
     * false if event does not exist at all, and return false if event exists in EventsTemplate.
     */
    public boolean eventDoesExistOutsideOfEventsTemplate(int typeOfEvent, LocalDateTime ldt) {
        long eventId = getEventIdOutsideEventsTemplate(typeOfEvent, ldt);
        if (eventId == -1) {
            return false;
        }
        return true;
    }

    /**
     * This method uses datetime of event and type of event to retrieve id.
     * An eventsTemplate can have same type and datetime as a "real" event
     *
     * @param event
     * @return
     */
    public long getEventIdOutsideEventsTemplate(Event event) {
        int type = event.getType();
        String time = DateTimeFormat.toSqLiteFormat(event.getTime());
        return getEventIdOutsideEventsTemplate(type, time);
    }

    //returns -1 if no such event exists
    private long getEventIdOutsideEventsTemplate(int typeOfEvent, LocalDateTime ldt) {
        return getEventIdOutsideEventsTemplate(typeOfEvent, DateTimeFormat.toSqLiteFormat(ldt));
    }

    /**
     * Note that there can exist several events in database with same typeofEvent and time.
     * (Because of EventsTemplate) This must be handled here (eventstemplate IS NULL).
     *
     * @param typeOfEvent
     * @param time
     * @return returns -1 if no such event exists
     */
    private long getEventIdOutsideEventsTemplate(int typeOfEvent, String time) {
        long eventId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT " + COLUMN_ID + " FROM " + TABLE_EVENTS + " WHERE " +
                COLUMN_TYPE_OF_EVENT +
                " = ? AND " + COLUMN_DATETIME + " = ? AND " + COLUMN_EVENTSTEMPLATE + " IS NULL";
        Cursor c = db.rawQuery(QUERY, new String[]{String.valueOf(typeOfEvent), time});
        if (c != null) {
            if (c.moveToFirst()) {
                eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
            }
        }
        db.close();
        return eventId;
    }


    public void changeEventExcludingEventsTemplates(Event e) {
        long eventId = getEventIdOutsideEventsTemplate(e);
        changeEvent(eventId, e);
    }

    /**
     * The id is important initially, because the datetime might have changed.
     * But the event after the change has occurred doesn't have to have the same id.
     *
     * @param e
     */
    public void changeEvent(long eventId, Event e) {
        int type = e.getType();
        switch (type) {
            case MEAL:
                changeMeal(eventId, (com.johanlund.base_classes.Meal) e);
                break;
            case OTHER:
                changeOther(eventId, (com.johanlund.base_classes.Other) e);
                break;
            case EXERCISE:
                changeExercise(eventId, (com.johanlund.base_classes.Exercise) e);
                break;
            case BM:
                changeBm(eventId, (com.johanlund.base_classes.Bm) e);
                break;
            case RATING:
                changeRating(eventId, (Rating) e);
                break;
        }
    }

    /**
     * Changes a Meal in database to toMeal (id:s doesn't have to be the same afterwards).
     *
     * @param eventId
     * @param toMeal
     * @return
     */
    private void changeMeal(long eventId, com.johanlund.base_classes.Meal toMeal) {
        deleteEvent(eventId);
        addMeal(toMeal);
    }

    private void changeOther(long eventId, com.johanlund.base_classes.Other toOther) {
        deleteEvent(eventId);
        addOther(toOther);
    }

    private void changeExercise(long eventId, com.johanlund.base_classes.Exercise toExercise) {
        deleteEvent(eventId);
        addExercise(toExercise);
    }

    private void changeBm(long eventId, com.johanlund.base_classes.Bm toBm) {
        deleteEvent(eventId);
        addBm(toBm);
    }

    private void changeRating(long eventId, Rating toRating) {
        deleteEvent(eventId);
        addRating(toRating);
    }

    /*
         2018-04-04
         Introducing new method for retrieveing events.

         WindowLeaked problem made me come to this solution.
         The original problem was that the Query became to large after a couple of months use of
         the diary.

         A cursor can only hold 1 mb of data.

         After a couples of months use I am up to 2 mb.

         It is still a major issue, since the statistics results are shortened with repeated use.

         There is a solution! You don't use comments!
         */
    public List<Event> getEventsForStatistics() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String ONLY_RELEVANT_COLUMNS = COLUMN_ID + ", " + COLUMN_DATETIME + ", " +
                COLUMN_TYPE_OF_EVENT + ", " + COLUMN_HAS_BREAK;
        final String QUERY = "SELECT " + ONLY_RELEVANT_COLUMNS + " FROM " + TABLE_EVENTS + " " +
                "WHERE " + COLUMN_EVENTSTEMPLATE +
                " IS NULL " + " ORDER BY " + COLUMN_DATETIME + "" + " ASC";
        Cursor c = null;
        List<Event> events;
        try {
            c = db.rawQuery(QUERY, null);
            events = getStatisticsEventsRetrieverHelper(c);
        } finally {
            // this gets called even if there is an exception somewhere above
            if (c != null || !c.isClosed())
                c.close();
        }
        return events;
    }

    //notice how this method use null in select statement to avoid retrieving events from
    // eventstemplates
    public List<Event> getAllEventsMinusEventsTemplatesSorted() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + COLUMN_EVENTSTEMPLATE +
                " IS NULL " + " ORDER BY " + COLUMN_DATETIME + "" + " ASC";
        Cursor c = null;
        List<Event> events;


        try {
            c = db.rawQuery(QUERY, null);
            events = getEventsRetrieverHelper(c);
        } finally {
            // this gets called even if there is an exception somewhere above
            if (c != null || !c.isClosed())
                c.close();
        }
        return events;
    }

    /**
     * @param currentDate
     * @return an empty List<Event> in case the there exists no events that day
     */
    public List<Event> getAllEventsMinusEventsTemplateSortedFromDay(LocalDate currentDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        //where column_datetime starts with currentDate
        String dateStr = DateTimeFormat.dateToSqLiteFormat(currentDate);
        final String QUERY = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + COLUMN_DATETIME + " LIKE ? AND " +
                COLUMN_EVENTSTEMPLATE + " IS NULL " +
                " ORDER BY " + COLUMN_DATETIME + "" +
                " ASC;";
        Cursor c = db.rawQuery(QUERY, new String[]{dateStr + "%"});
        return getEventsRetrieverHelper(c);
    }

    public Event retrieveEvent(long id) {
        Event toReturn = null;
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT " + COLUMN_DATETIME + ", " + COLUMN_COMMENT + ", " +
                COLUMN_HAS_BREAK + ", " + COLUMN_TYPE_OF_EVENT + " FROM " + TABLE_EVENTS + " " +
                "WHERE " + COLUMN_ID + " = ?";
        Cursor c = db.rawQuery(QUERY, new String[]{String.valueOf(id)});
        if (c != null) {
            if (c.moveToFirst()) {
                try {
                    toReturn = getEvent(id, c);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    c.close();
                    db.close();
                }
            }
        }
        return toReturn;
    }

    private Event getEvent(long eventId, Cursor c) throws InvalidEventType {
        String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME)); //denna kraschar
        int type = c.getInt(c.getColumnIndex(COLUMN_TYPE_OF_EVENT));
        String comment = c.getString(c.getColumnIndex(COLUMN_COMMENT));
        boolean hasBreak = getHasBreak(c);
        return getEvent(eventId, DateTimeFormat.fromSqLiteFormat(datetime), comment, hasBreak,
                type);
    }

    /**
     * For statistics, comments only create a big Heap of no use
     *
     * @param eventId
     * @param c
     * @return
     * @throws InvalidEventType
     */
    private Event getStatisticsEventWithNoComment(long eventId, Cursor c) throws InvalidEventType {
        String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME));
        int type = c.getInt(c.getColumnIndex(COLUMN_TYPE_OF_EVENT));
        String comment = "";
        boolean hasBreak = getHasBreak(c);
        return getEvent(eventId, DateTimeFormat.fromSqLiteFormat(datetime), comment, hasBreak,
                type);
    }

    /**
     * 0 (false), 1 (true)
     *
     * @param c
     * @return
     */
    private boolean getHasBreak(Cursor c) {
        return c.getInt(c.getColumnIndex(COLUMN_HAS_BREAK)) == 1;
    }


    private Event getEvent(long eventId, LocalDateTime ldt, String comment, boolean hasBreak, int
            typeOfEvent) throws
            InvalidEventType {
        Event event = null;
        switch (typeOfEvent) {
            case MEAL:
                event = retrieveMeal(eventId, ldt);
                break;
            case OTHER:
                event = retrieveOther(eventId, ldt);
                break;
            case EXERCISE:
                event = retrieveExercise(eventId, ldt);
                break;
            case BM:
                event = retrieveBm(eventId, ldt);
                break;
            case RATING:
                event = retrieveRating(eventId, ldt);
                break;
        }
        if (event == null) {
            throw new InvalidEventType("Event should not be null here");
        }
        event.setComment(comment);
        event.setHasBreak(hasBreak);
        return event;
    }

    private Event retrieveMeal(long eventId, LocalDateTime ldt) throws InvalidEventType {
        Cursor c = retrieveHelper(eventId, TABLE_MEALS);
        com.johanlund.base_classes.Meal meal = null;
        if (c != null) {
            if (c.moveToFirst()) {  //hoppar här
                double portions = c.getDouble(c.getColumnIndex(COLUMN_PORTIONS));
                List<TagWithoutTime> tags = getTagsWithEventId(eventId);
                meal = new com.johanlund.base_classes.Meal(ldt, tags, portions);
            }
        }
        c.close();
        this.close();
        if (meal == null) {
            throw new InvalidEventType("Meal event should not be null here");
        }
        return meal;
    }

    private Event retrieveOther(long eventId, LocalDateTime ldt) throws InvalidEventType {
        Cursor c = retrieveHelper(eventId, TABLE_OTHERS);
        com.johanlund.base_classes.Other other = null;
        if (c != null) {
            if (c.moveToFirst()) {  //k
                List<TagWithoutTime> tags = getTagsWithEventId(eventId);
                other = new com.johanlund.base_classes.Other(ldt, tags);
            }
        }
        c.close();
        this.close();
        if (other == null) {
            throw new InvalidEventType("Other event should not be null here");
        }
        return other;
    }

    private Event retrieveExercise(long eventId, LocalDateTime ldt) throws InvalidEventType {
        Cursor c = retrieveHelper(eventId, TABLE_EXERCISES);
        com.johanlund.base_classes.Exercise exercise = null;
        if (c != null) {
            if (c.moveToFirst()) {
                int intensity = c.getInt(c.getColumnIndex(COLUMN_INTENSITY));
                TagWithoutTime tag = getTagsWithEventId(eventId).get(0);
                exercise = new com.johanlund.base_classes.Exercise(ldt, tag, intensity);
            }
        }
        c.close();
        this.close();
        if (exercise == null) {
            throw new InvalidEventType("Exercise event should not be null here");
        }
        return exercise;
    }

    private Event retrieveBm(long eventId, LocalDateTime ldt) throws InvalidEventType {
        Cursor c = retrieveHelper(eventId, TABLE_BMS);
        com.johanlund.base_classes.Bm bm = null;
        if (c != null) {
            if (c.moveToFirst()) {
                int complete = c.getInt(c.getColumnIndex(COLUMN_COMPLETENESS));
                int bristol = c.getInt(c.getColumnIndex(COLUMN_BRISTOL));
                bm = new com.johanlund.base_classes.Bm(ldt, complete, bristol);
            }
        }
        c.close();
        this.close();
        if (bm == null) {
            throw new InvalidEventType("Bm event should not be null here");
        }
        return bm;
    }

    private Event retrieveRating(long eventId, LocalDateTime ldt) throws InvalidEventType {
        Cursor c = retrieveHelper(eventId, TABLE_RATINGS);
        Rating rating = null;
        if (c != null) {
            if (c.moveToFirst()) {
                int after = c.getInt(c.getColumnIndex(COLUMN_AFTER));
                rating = new Rating(ldt, after);
            }
        }
        c.close();
        this.close();
        if (rating == null) {
            throw new InvalidEventType("Rating event should not be null here");
        }
        return rating;
    }

    //db must be closed elsewhere, if closed here using methods will fail using cursor
    private Cursor retrieveHelper(long eventId, String nameOfEventTable) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + nameOfEventTable + " WHERE " + COLUMN_EVENT + " =" +
                " ?";
        Cursor c = db.rawQuery(QUERY, new String[]{String.valueOf(eventId)});  //c blir null när
        // laddas från eventstemplate, varför?
        return c;
    }

    LocalDateTime getDateFromEvent(long eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_DATETIME + " FROM " + TABLE_EVENTS + " WHERE " + COLUMN_ID
                + " = ?";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(eventId)});
        LocalDateTime ldt = null;
        if (c != null) {
            if (c.moveToFirst()) {
                String date = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                ldt = DateTimeFormat.fromSqLiteFormat(date);


            }
        }
        c.close();
        db.close();
        return ldt;
    }

    //==============================================================================================
    // Meal methods
    //==============================================================================================
    List<com.johanlund.base_classes.Meal> getAllMeals() {

        List<com.johanlund.base_classes.Meal> meals = new ArrayList<>();
        final String QUERY = "SELECT * FROM " + TABLE_MEALS;

        //retrieve portions and event_id
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(QUERY, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            double portions = c.getDouble(c.getColumnIndex(COLUMN_PORTIONS));
            long eventId = c.getLong(c.getColumnIndex(COLUMN_EVENT));
            LocalDateTime ldt = getDateFromEvent(eventId);

            List<TagWithoutTime> tags = getTagsWithEventId(eventId);
            com.johanlund.base_classes.Meal meal = new com.johanlund.base_classes.Meal(ldt, tags,
                    portions);
            meals.add(meal);
            c.moveToNext();
        }
        c.close();
        db.close();
        return meals;
    }


    public void addMeal(com.johanlund.base_classes.Meal meal) {
        //first create event and obtain its id
        long eventId = addEventBase(meal, MEAL);
        addToMealTable(eventId, meal);

    }

    private void addToMealTable(long eventId, com.johanlund.base_classes.Meal meal) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_PORTIONS, meal.getPortions());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_MEALS, DATABASE_NAME, values);
        db.close();
    }

    //There can be several events one time, but only one meal.
    //apart from testing, is this method useless??? (use getEvent(type, ldt) instead???)
    // => depends on how statistics classes will be formed and how events will be retrieved from
    // diary list.

    /**
     * @param ldt
     * @return null if no meal is found at time
     */
    public com.johanlund.base_classes.Meal retrieveMealByTime(LocalDateTime ldt) {

        //select from meals where its event has time ...
        final String QUERY = "SELECT " + "a." + COLUMN_PORTIONS + ", a."
                + COLUMN_EVENT + " FROM " +
                TABLE_MEALS + " a "
                + " INNER JOIN " + TABLE_EVENTS + " b ON " + " a." + COLUMN_EVENT + " = b." +
                COLUMN_ID; //+
        //  " WHERE " + "b."+ COLUMN_DATETIME + " =?";

        //retrieve portions and event_id
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(QUERY, null);

        double portions = -1.;
        long eventId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            portions = cursor.getDouble(cursor.getColumnIndex(COLUMN_PORTIONS));
            eventId = cursor.getLong(cursor.getColumnIndex(COLUMN_EVENT));
        }
        cursor.close();
        db.close();
        if (eventId == -1) {
            //this would mean that cursor don't have a row with meal.
            return null;
        }
        List<TagWithoutTime> tags = getTagsWithEventId(eventId);
        return new com.johanlund.base_classes.Meal(ldt, tags, portions);
    }

    //==============================================================================================
    // Other
    //==============================================================================================
    public void addOther(com.johanlund.base_classes.Other other) {
        //first create event and obtain its id
        long eventId = addEventBase(other, OTHER);
        addToOtherTable(eventId, other);
    }

    private void addToOtherTable(long eventId, com.johanlund.base_classes.Other other) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_OTHERS, DATABASE_NAME, values);
        db.close();
    }

    //==============================================================================================
    // Exercise
    //==============================================================================================
    public void addExercise(com.johanlund.base_classes.Exercise exercise) {
        //first create event and obtain its id
        long eventId = addEventBase(exercise, EXERCISE);
        addTagAndPossiblyTagTemplate(exercise.getTypeOfExercise(), eventId);
        addToExerciseTable(eventId, exercise);
    }

    private void addToExerciseTable(long eventId, com.johanlund.base_classes.Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_INTENSITY, (long) exercise.getIntensity());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_EXERCISES, DATABASE_NAME, values);
        db.close();
    }

    //==============================================================================================
    // BM
    //==============================================================================================
    public void addBm(com.johanlund.base_classes.Bm bm) {
        //first create event and obtain its id
        long eventId = addEventBase(bm, BM);
        addToBmTable(eventId, bm);
    }

    private void addToBmTable(long eventId, com.johanlund.base_classes.Bm bm) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_BRISTOL, bm.getBristol());
        values.put(COLUMN_COMPLETENESS, bm.getComplete());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_BMS, DATABASE_NAME, values);
        db.close();
    }

    //==============================================================================================
    // RATING
    //==============================================================================================
    public void addRating(Rating rating) {
        long eventId = addEventBase(rating, RATING);
        addToRatingTable(eventId, rating);
    }

    private void addToRatingTable(long eventId, Rating rating) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_AFTER, (long) rating.getAfter());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_RATINGS, DATABASE_NAME, values);
        db.close();
    }

    //==================================================================================================
    //TAGS
    //==================================================================================================
    private List<TagWithoutTime> getTagsWithEventId(long event_id) {
        final String TAG_QUERY = "SELECT * FROM " + TABLE_TAGS + " WHERE " + COLUMN_EVENT + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(TAG_QUERY, new String[]{String.valueOf(event_id)});
        List<TagWithoutTime> tags = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {
                    double size = cursor.getDouble(cursor.getColumnIndex(COLUMN_SIZE));
                    long tagTemplateId = cursor.getLong(cursor.getColumnIndex(COLUMN_TAGTYPE));
                    String tagname = getTagname(tagTemplateId);
                    TagWithoutTime t = new TagWithoutTime(tagname, size);
                    tags.add(t);
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        db.close();
        return tags;
    }

    /**
     * Note that this method also adds TagType in case it is missing
     *
     * @param t
     * @param eventId
     */
    private void addTagAndPossiblyTagTemplate(TagWithoutTime t, long eventId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_SIZE, t.getSize());

        long tagTemplateId = getTagTemplateId(t.getName());
        if (tagTemplateId == -1) {
            TagType tt = new TagType(t.getName(), null);
            tagTemplateId = addTagTemplate(tt);
        }
        values.put(COLUMN_TAGTYPE, tagTemplateId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TAGS, null, values);
        db.close();
    }

    //===================================================================================
    //TagType
    //===================================================================================
    public long addTagTemplate(TagType tagType) {
        ContentValues values = makeTagTemplateContentValues(tagType);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_TAGTYPES, null, values);
        db.close();
        return id;
        //Log.d("Debug", "addTagTemplate completed! TagType " + tagType.get_tagname() + "
        // with id nr: " + findTagTemplate(tagType.get_tagname()).get_id() + " inserted!");
    }

    public void deleteTagTemplate(long idOfTagTemplate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGTYPES, "_id=" + idOfTagTemplate, null);
        db.close();
    }

    public String getTagTemplateName(long idOfTagTemplate) {
        String toReturn = "";
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + TABLE_TAGTYPES + " WHERE " + COLUMN_ID + " =?";
        Cursor c = db.rawQuery(QUERY, new String[]{Long.toString(idOfTagTemplate)});
        if (c != null) {
            if (c.moveToFirst()) {
                toReturn = c.getString(c.getColumnIndex(COLUMN_TAGNAME));
            }
        }
        c.close();
        db.close();
        return toReturn;
    }


    public void editTagTemplate(TagType tagType, long idOfTagTemplate) {
        ContentValues values = makeTagTemplateContentValues(tagType);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_TAGTYPES, values, "_id=" + idOfTagTemplate, null);
        db.close();
    }

    public void removeExercisesWithTagTemplate(long idOfTagTemplate) {
        //1. get all eventId where exercises has a tag with tagtemplate_id == idOfTagTemplate
        final String QUERY = "SELECT " + "a." + COLUMN_EVENT + " FROM " +
                TABLE_EXERCISES + " a "
                + " INNER JOIN " + TABLE_TAGS + " b ON " + " a." + COLUMN_EVENT + " = b." +
                COLUMN_EVENT + " WHERE b." + COLUMN_TAGTYPE + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(QUERY, new String[]{Long.toString(idOfTagTemplate)});

        //2. For all these use deleteEvent (eventId). Cascading will delete the Exercises.
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    long eventId = c.getLong(c.getColumnIndex(COLUMN_EVENT));
                    deleteEvent(eventId);
                    c.moveToNext();
                }
            }
        }
        c.close();
        db.close();
    }

    private ContentValues makeTagTemplateContentValues(TagType tagType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAGNAME, tagType.get_tagname());
        actOnTagTemplateChild(values, tagType.get_type_of(), TYPE_OF);
        return values;
    }

    private void actOnTagTemplateChild(ContentValues values, TagType child, String
            childColumn) {
        if (child == null) {
            values.putNull(childColumn);
        } else {
            values.put(childColumn, getTagTemplateId(child.get_tagname()));
        }
    }

    public List<TagType> getAllTagTemplates() {
        List<TagType> tagTypes = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TAGTYPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TagType tt = findTagTemplateHelper2(cursor);
                tagTypes.add(tt);
                cursor.moveToNext();
            }
        }
        return tagTypes;
    }

    public List<TagTypeEntity>getAllTagTypeEntities(){
        List<TagTypeEntity> tagTypeEntities = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TAGTYPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String tagName = cursor.getString(cursor.getColumnIndex(COLUMN_TAGNAME));
                int typeOf = cursor.getInt(cursor.getColumnIndex(TYPE_OF));
                TagTypeEntity tte = new TagTypeEntity(id, tagName,typeOf);
                tagTypeEntities.add(tte);
                cursor.moveToNext();
            }
        }
        return tagTypeEntities;
    }

    public TagType findTagTemplate(String tagName) {
        String query = "SELECT * FROM " + TABLE_TAGTYPES + " WHERE " + COLUMN_TAGNAME + " = " +
                "\"" + tagName + "\"";
        return findTagTemplateHelper(query);
    }

    public TagType findTagTemplate(long id) {
        String query = "SELECT * FROM " + TABLE_TAGTYPES + " WHERE " + COLUMN_ID + " = \"" +
                String.valueOf(id) + "\"";
        return findTagTemplateHelper(query);
    }

    //returns null if there is no such TagType in database
    private TagType findTagTemplateHelper(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        TagType tt = null;
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            tt = findTagTemplateHelper2(cursor);
        }
        return tt;
    }

    private TagType findTagTemplateHelper2(Cursor c) {
        TagType tt = new TagType();
        tt.set_tagname(c.getString(c.getColumnIndex(COLUMN_TAGNAME)));

        //sets nulls or parent tagtemplates
        tt.set_is_a1(retrieveOneParentToTagTemplate(c, TYPE_OF));
        return tt;
    }

    /**
     * @param c
     * @param columnName
     * @return null if parent doesn't exist, otherwise return the parent
     */
    private TagType retrieveOneParentToTagTemplate(Cursor c, String columnName) {
        TagType parent;
        if (c.isNull(c.getColumnIndex(columnName))) {
            parent = null;
        } else {
            parent = findTagTemplate(c.getColumnIndex(columnName));
        }
        return parent;
    }

    public String getTagname(long id) {
        String query = "SELECT * FROM " + TABLE_TAGTYPES + " WHERE " + COLUMN_ID + " = \"" +
                id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String tagname = "";
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            tagname = cursor.getString(cursor.getColumnIndex(COLUMN_TAGNAME));
        }
        cursor.close();
        db.close();
        return tagname;
    }

    public long getTagTemplateId(String name) {
        String query = "SELECT * FROM " + TABLE_TAGTYPES + " WHERE " + COLUMN_TAGNAME + " = " +
                "\"" + name + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        long tagId = -1;
        if (cursor.moveToFirst()) {
            tagId = cursor.getInt(0);
        }
        db.close();
        return tagId;
    }


    //inspired by p. 557
    public List<TagType> retrieveTagNames() {
        String query = "SELECT * FROM " + TABLE_TAGTYPES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<TagType> tagTypes = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TagType tagType = new TagType("");
                tagType.set_tagname(cursor.getString(1));
                tagTypes.add(tagType);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return tagTypes;
    }

    //almost copy-pasted from getEventsRetrieverHelper
    private List<Rating> getRatingsRetrieverHelper(Cursor c) {
        List<Rating> ratingList = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    try {

                        long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                        Event event = getEvent(eventId, c);

                        ratingList.add((Rating) event);
                        c.moveToNext();
                    } catch (Exception e) {
/*                        Log.e(TAG, "Something went wrong reading an event, jumping to next");
                        Log.e(TAG, "exception", e);*/
                        c.moveToNext();
                    }
                }
            }
        }
        c.close();
        //this.close();
        return ratingList;
    }

    private List<Event> getEventsRetrieverHelper(Cursor c) {
        List<Event> eventList = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    try {

                        long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                        Event event = getEvent(eventId, c);

                        eventList.add(event);
                        c.moveToNext();
                    } catch (Exception e) {
/*                        Log.e(TAG, "Something went wrong reading an event, jumping to next");
                        Log.e(TAG, "exception", e);*/
                        c.moveToNext();
                    }
                }
            }
        }
        c.close();
        this.close();
        return eventList;
    }

    //mostly copied from above
    private List<Event> getStatisticsEventsRetrieverHelper(Cursor c) {
        List<Event> eventList = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    try {

                        long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                        Event event = getStatisticsEventWithNoComment(eventId, c);

                        eventList.add(event);
                        c.moveToNext();
                    } catch (Exception e) {
/*                        Log.e(TAG, "Something went wrong reading an event, jumping to next");
                        Log.e(TAG, "exception", e);*/
                        c.moveToNext();
                    }
                }
            }
        }
        c.close();
        this.close();
        return eventList;
    }

    //returns null on failure
    public LocalDate getDateOfLastEvent() {
        LocalDate ld = null;
        LocalDateTime ldt = getTimeOfLastEvent();
        if (ldt != null) {
            ld = ldt.toLocalDate();
        }
        return ld;
    }

    //returns null on failure
    public LocalDateTime getTimeOfLastEvent() {
        SQLiteDatabase db = this.getReadableDatabase();
        //must use datetime and not date since datetime can be used with MAX in sqlite
        final String QUERY = "Select MAX (" + COLUMN_DATETIME + ") FROM " + TABLE_EVENTS;
        Cursor c = db.rawQuery(QUERY, null);
        LocalDateTime ldt = null;
        if (c != null) {
            c.moveToFirst();
            try {
                String dateTimeStr = c.getString(0);
                ldt = DateTimeFormat.fromSqLiteFormat(dateTimeStr);
            } catch (Exception e) {
                ldt = null;
            } finally {
                c.close();
                db.close();
            }
        }
        return ldt;
    }

    public boolean diaryIsEmpty() {
        return tableIsEmpty(TABLE_EVENTS);
    }

    public boolean tagTypesTableIsEmpty() {
        return tableIsEmpty(TABLE_TAGTYPES);
    }

    private boolean tableIsEmpty(String tablename) {
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM " + tablename;
        Cursor c = null;
        try {
            c = db.rawQuery(count, null);
            c.moveToFirst();
            if (c.getInt(0) > 0) {
                return false;
            }
        } finally {
            c.close();
            db.close();
        }
        return true;
    }

    /**
     * Cursor safe
     *
     * @return
     */
    public List<LocalDateTime> getManualBreaks() {
        List<LocalDateTime> toReturn = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "Select " + COLUMN_DATETIME + " FROM " + TABLE_EVENTS + " WHERE " +
                COLUMN_HAS_BREAK + " = 1 " + " ORDER BY " + COLUMN_DATETIME + " ASC ";
        Cursor c = null;
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                            LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(datetime);
                            toReturn.add(ldt);
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;
    }

    /**
     * Cursor safe
     *
     * @return autoBreaks sorted in in ascending time order
     */
    public List<LocalDateTime> getAutoBreaks(int hoursAheadBreak) {

        //1. Get all event times in asc order
        List<LocalDateTime> eTimes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "Select " + COLUMN_DATETIME + " FROM " + TABLE_EVENTS + " ORDER BY "
                + COLUMN_DATETIME + " ASC ";

        Cursor c = null;
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                            LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(datetime);
                            eTimes.add(ldt);
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        //non-db operations
        //2. iterate eTimes and check closest laying elements time diff
        List<LocalDateTime> aBreaks = new ArrayList<>();
        for (int i = 0; i < eTimes.size() - 1; i++) {
            if (eTimes.get(i).plusHours(hoursAheadBreak).isBefore(eTimes.get(i + 1))) {
                aBreaks.add(eTimes.get(i));
            }
        }
        return aBreaks;
    }

    public List<ScoreTime> getRatingTimes() {
        return getScoreTimes(TABLE_RATINGS, COLUMN_AFTER);
    }

    /**
     * Cursor safe
     *
     * @return
     */
    public List<ScoreTime> getCompleteTimes() {
        return getScoreTimes(TABLE_BMS, COLUMN_COMPLETENESS);
    }

    /**
     * Cursor safe
     *
     * @return
     */
    public List<ScoreTime> getBristolTimes() {
        return getScoreTimes(TABLE_BMS, COLUMN_BRISTOL);
    }

    private List<ScoreTime> getScoreTimes(String eventTable, String scoreColumn) {
        List<ScoreTime> toReturn = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "Select " + " e." + COLUMN_DATETIME + ", b." + scoreColumn +
                " FROM " + TABLE_EVENTS + " e " + " JOIN " + eventTable + " b " + " ON e. " +
                COLUMN_ID + " = b." + COLUMN_EVENT + " ORDER BY " + COLUMN_DATETIME + " ASC ";

        Cursor c = null;
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                            LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(datetime);
                            int score = c.getInt(c.getColumnIndex(scoreColumn));
                            toReturn.add(new ScoreTime(ldt, score));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;
    }

    public List<Tag> getAllTagsWithTime() {
        List<Tag> toReturn = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT " + " tt." + COLUMN_TAGNAME + ", t." + COLUMN_SIZE + ", e."
                + COLUMN_DATETIME +
                " FROM " + TABLE_TAGS + " t " +
                " LEFT JOIN " + TABLE_TAGTYPES + " tt " + " ON tt. " + COLUMN_ID + " = t." +
                COLUMN_TAGTYPE +
                " LEFT JOIN " + TABLE_EVENTS + " e " + " ON e. " + COLUMN_ID + " = t." +
                COLUMN_EVENT;
        Cursor c = null;
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            String timeStr = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                            LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(timeStr);
                            String tagName = c.getString(c.getColumnIndex(COLUMN_TAGNAME));
                            double size = c.getDouble(c.getColumnIndex(COLUMN_SIZE));
                            toReturn.add(new Tag(ldt, tagName, size));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;
    }

    /**
     * @return tagsWithoutTime in no particular order
     */
    public List<TagWithoutTime> getAllTags() {
        List<TagWithoutTime> toReturn = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT " + " tt." + COLUMN_TAGNAME + ", t." + COLUMN_SIZE +
                " FROM " + TABLE_TAGTYPES + " tt " + " JOIN " + TABLE_TAGS + " t " + " ON tt. " +
                COLUMN_ID + " = t." + COLUMN_TAGTYPE;

        Cursor c = null;
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            String tagName = c.getString(c.getColumnIndex(COLUMN_TAGNAME));
                            double size = c.getDouble(c.getColumnIndex(COLUMN_SIZE));
                            toReturn.add(new TagWithoutTime(tagName, size));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;
    }

    /*
    Imports TagTypes from external database.
    Takes care to avoid id crash.
    Note that a tagtype that has same name as already existing tagtype will not be added (due to table definition).

    This works good when import is done to empty database. However there can be problems if internal_db already has some tagtypes that have same names as the TagTypes-to-be-imported.
    The problem is for _is_a1 which then can reference to wrong (or to non existant) tagtype.
     */
    public void insertTagTypesFromExternalDatabase(@NotNull String absolutePath) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        String maxId = getMaxIdFromInternalDBTable(TABLE_TAGTYPES, db1);
        Log.e(TAG, "maxId: "+maxId); //
        db1.close();
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e(TAG, "db.isOpen "+db.isOpen());   //true => ok!
        Log.e(TAG, "db.isReadOnly() "+db.isReadOnly()); //false => ok!
        Log.e(TAG, "db.inTransaction() "+db.inTransaction());   //false => ok!





        //attach and copy from external database
        //---------------------------------------
        try {
            //attaching database must be done outside of transaction
            db.execSQL("ATTACH DATABASE '" + absolutePath + "' as " +
                    "'external_db'");
            db.execSQL("PRAGMA foreign_keys = OFF;");

            //======loggging

            List<Pair<String, String>> databases = db.getAttachedDbs();

            for (Pair p: databases){
                Log.e(TAG, "databases (plus attached:) "+p.first.toString());   //main + external_db => ok!
            }

//===================================================
            db.beginTransaction();
            //db.execSQL("BEGIN;");   //krashar här, varför

            db.execSQL("INSERT INTO " + TABLE_TAGTYPES + " (_tagname, _is_a1) SELECT _tagname, _is_a1 FROM external_db.tag_templates;");

            makeParentTagTypePointToOwnDB(maxId, db);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.endTransaction();
                db.execSQL("PRAGMA foreign_keys = ON;");
                db.execSQL("DETACH 'external_db'");
                db.close();
            }
        }
    }
    //change the inserted TagTypes is_a1 to correct values.
    private void makeParentTagTypePointToOwnDB(String maxId, SQLiteDatabase internalDB){
        //change the inserted TagTypes is_a1 to correct values.
        final String QUERY =
                "SELECT t1."+ COLUMN_ID + ", t2."+ COLUMN_ID +" FROM tag_templates t1 " +
                        "INNER JOIN external_db.tag_templates te ON t1._is_a1 = te._id " +
                        "INNER JOIN tag_templates t2 ON te._tagname = t2._tagname" +
                        " WHERE t1."+ COLUMN_ID + " > "+maxId +";";
        Cursor c = null;
        try {
            c = internalDB.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {

                            //retrieve id (the Tagtype row that should )
                            long id = c.getLong(0);
                            long correct_parent_id = c.getLong(1);
                            Log.d("DBHandler", "---------------------------");
                            Log.d("DBHandler", "id of tagtype to change "+id);
                            Log.d("DBHandler", "parent id to change into "+correct_parent_id);
                            Log.d("DBHandler", "---------------------------");

                            internalDB.execSQL("UPDATE " + TABLE_TAGTYPES + " SET "+ TYPE_OF + " = " + correct_parent_id + " WHERE "+ COLUMN_ID + " = " + id + ";");
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
    }

    public void insertEventTemplatesFromExternalDatabase(@NotNull String absolutePath) {
        //1. Insert table eventsTemplates (only the ones with names that doesn't already exist -> this is handled internally by UNIQUE ON CONFLICT IGNORE. )

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("ATTACH DATABASE '" + absolutePath + "' as " +
                    "'external_db'");

            db.execSQL("PRAGMA foreign_keys = OFF;");
            db.execSQL("BEGIN;");

            /*1. Insert EventsTemplates that don't already exist (same name - ON CASCADE IGNORE takes care of that)
                  NB. That _id of EventsTemplates will likely be different from EventsTemplates._id in external_db
             */

            String eventTemplatesMaxId = getMaxIdFromInternalDBTable(TABLE_EVENTSTEMPLATES, db);

            db.execSQL("INSERT INTO " + TABLE_EVENTSTEMPLATES + " ("+COLUMN_NAME+") SELECT "+ COLUMN_NAME + " FROM external_db."+TABLE_EVENTSTEMPLATES);

            //2. For theses EventsTemplates - insert affiliated Events.
            String eventsMaxId = getMaxIdFromInternalDBTable(TABLE_EVENTS, db);

            db.execSQL("INSERT INTO " + TABLE_EVENTS  + " ( " + COLUMN_DATETIME+ ", " +COLUMN_TYPE_OF_EVENT +
                    ", " + COLUMN_EVENTSTEMPLATE+ ", " + COLUMN_COMMENT + ", " + COLUMN_HAS_BREAK + ") " +
                    " SELECT "+ COLUMN_DATETIME + ", " + COLUMN_TYPE_OF_EVENT + ", ite." + COLUMN_ID+ ", " + COLUMN_COMMENT + ", " + COLUMN_HAS_BREAK  +
                    " FROM  external_db."+TABLE_EVENTS + " ee" +
                    " INNER JOIN external_db."+ TABLE_EVENTSTEMPLATES + " ete " +
                    " ON ee. "+COLUMN_EVENTSTEMPLATE + " = ete."+COLUMN_ID +
                    " INNER JOIN "+ TABLE_EVENTSTEMPLATES + " ite " +
                    " ON ite. "+COLUMN_NAME + " = ete."+COLUMN_NAME +
                    " AND ite."+COLUMN_ID +" > "+ eventTemplatesMaxId + ";");

            //3. for the INSERTED EVENTS, insert meal, exercise, rating, bm, rating from external DB
            insertIntoEventTypeTableFromExternalDB(TABLE_MEALS, MEAL, ", "+COLUMN_PORTIONS, eventsMaxId, db, "external_db");
            insertIntoEventTypeTableFromExternalDB(TABLE_OTHERS, OTHER, "", eventsMaxId, db, "external_db");
            insertIntoEventTypeTableFromExternalDB(TABLE_EXERCISES, EXERCISE, ", " + COLUMN_INTENSITY, eventsMaxId, db, "external_db");
            insertIntoEventTypeTableFromExternalDB(TABLE_BMS, BM, ", " + COLUMN_COMPLETENESS + ", " + COLUMN_BRISTOL, eventsMaxId, db, "external_db");
            insertIntoEventTypeTableFromExternalDB(TABLE_RATINGS, RATING, ", " + COLUMN_AFTER, eventsMaxId, db, "external_db");

            //4. Insert Tags associated with some of the events (which, by themselves, are assoiciated with newly inserted eventstemplates)
            // After this the Tags.tagtype will reference tagtypes in EXTERNAL db (so this is temporary solution).
            String tagsMaxId = getMaxIdFromInternalDBTable(TABLE_TAGS, db);
            db.execSQL("INSERT INTO "+ TABLE_TAGS + "( " + COLUMN_TAGTYPE + ", " + COLUMN_SIZE + ", " + COLUMN_EVENT+ ") " +
                    "SELECT et."+COLUMN_TAGTYPE + ", et."+ COLUMN_SIZE + ", ie."+ COLUMN_ID +
                    " FROM "+ TABLE_EVENTS + " ie " +
                    " INNER JOIN external_db."+TABLE_EVENTS + " ee "+
                    " ON ie."+ COLUMN_DATETIME + " = ee."+COLUMN_DATETIME +
                    " AND ie."+ COLUMN_TYPE_OF_EVENT + "= ee."+COLUMN_TYPE_OF_EVENT +
                    " AND ee." +COLUMN_EVENTSTEMPLATE +" IS NOT NULL " +  //this is easy to miss.
                    " AND ie." +COLUMN_ID +" > "+ eventsMaxId +
                    " INNER JOIN external_db." + TABLE_TAGS + " et " +
                    " ON et."+COLUMN_EVENT + " = ee." + COLUMN_ID + ";"
            );

            //5 Insert TagTypes that are missing and are associated with Tags inserted above.
            String tagTypesMaxId = getMaxIdFromInternalDBTable(TABLE_TAGTYPES, db);
            db.execSQL("INSERT INTO " + TABLE_TAGTYPES + "( " + COLUMN_TAGNAME + ", " + TYPE_OF + " )" +
                    " SELECT "+COLUMN_TAGNAME + ", " + TYPE_OF +
                    " FROM " +TABLE_TAGS + " it "+
                    " INNER JOIN external_db."+TABLE_TAGTYPES +" et" +
                    " ON it."+COLUMN_TAGTYPE + " = et."+ COLUMN_ID +
                    " AND it."+COLUMN_ID +" > " + tagsMaxId + ";"
            );

            //6. Tags.tagtype now points to external db, but we want it to internal db
            String TAG_QUERY = "SELECT it."+ COLUMN_ID + ", itt."+ COLUMN_ID +
                    " FROM " +TABLE_TAGS + " it "+
                    " INNER JOIN external_db."+ TABLE_TAGTYPES +  " te "+
                    " ON it."+COLUMN_TAGTYPE + " = te." + COLUMN_ID +
                    " INNER JOIN " + TABLE_TAGTYPES + " itt " +
                    " ON itt." + COLUMN_TAGNAME +" = te."+COLUMN_TAGNAME +
                    " WHERE it."+ COLUMN_ID + " > "+tagsMaxId +";";

            Cursor cursor = null;
            try {
                cursor = db.rawQuery(TAG_QUERY, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast()) {

                            //retrieve id (the Tagtype row that should )
                            long tagToUpdateId = cursor.getLong(0);
                            long correctTagTypeRef = cursor.getLong(1);
                            Log.d("DBHandler", "---------------------------");
                            Log.d("DBHandler", "tagToUpdateId: "+tagToUpdateId);
                            Log.d("DBHandler", "correctTagTypeRef: "+correctTagTypeRef);
                            Log.d("DBHandler", "---------------------------");

                            db.execSQL("UPDATE " + TABLE_TAGS + " SET "+ COLUMN_TAGTYPE + " = " + correctTagTypeRef + " WHERE "+ COLUMN_ID + " = " + tagToUpdateId + ";");
                            cursor.moveToNext();
                        }
                    }
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            //7. The newly inserted tagtypes.is_a1 points to external => make them point to internal instead.
            makeParentTagTypePointToOwnDB(tagTypesMaxId, db);

            db.execSQL("END;");
        } catch (Exception e) {
            db.execSQL("ROLLBACK;");
            e.printStackTrace();
        }
            finally {
            if (db.isOpen()) {
                db.execSQL("PRAGMA foreign_keys = ON;");
                db.execSQL("DETACH 'external_db'");
                db.close();
            }
        }
    }

    private void insertIntoEventTypeTableFromExternalDB(String specTable, int eventType, String moreColsToInsert, String maxIdOfEvents, SQLiteDatabase db, String external_db_name){
        db.execSQL("INSERT INTO " + specTable + " ( " + COLUMN_EVENT +moreColsToInsert +") SELECT ie."+COLUMN_ID + moreColsToInsert +
                " FROM " + TABLE_EVENTS + " ie "+
                " INNER JOIN " +external_db_name + "."+TABLE_EVENTS + " ee " +
                " ON ie."+ COLUMN_DATETIME + " = ee."+COLUMN_DATETIME +
                " AND ie."+ COLUMN_TYPE_OF_EVENT + "= ee."+COLUMN_TYPE_OF_EVENT +
                " AND ie." +COLUMN_TYPE_OF_EVENT+" = "+eventType +
                " AND ie. " + COLUMN_ID + " > " +maxIdOfEvents +
                " INNER JOIN "+ external_db_name + "."+ specTable + " es " +
                " ON ee."+COLUMN_ID + " = es."+COLUMN_EVENT +";");
    }

    /**
     * IMPORTANT db is not closed in here!
     * @param table
     * @param db is not closed in method, must be closed outside
     * @return
     */
    private String getMaxIdFromInternalDBTable(String table, SQLiteDatabase db){

        //Default start value of id == 1. If it was zero, then I would have had to do: maxId++
        final String QUERY = "SELECT MAX(" + COLUMN_ID + ") AS 'max_id' FROM " + table;

        Cursor c = null;
        String maxId ="0";
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    maxId = c.getString(c.getColumnIndex("max_id"));
                    //have to check for null, since this will be returned if max (no_rows).
                    maxId = maxId == null ?"0":maxId;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return maxId;
    }

    public boolean checkIfTagTypeExist(@NotNull String string) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT COUNT( * ) FROM " + TABLE_TAGTYPES + " WHERE "+ COLUMN_NAME +" = " + string +";";
        Cursor c = null;
        boolean exists = false;
        try {
            c = db.rawQuery(QUERY, null);
            c.moveToFirst();
            int count= c.getInt(0);
            if (count > 0){
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()){
                db.close();
            }
        }
        return exists;
    }

    /*

        NEITHER of below do return tags referenced from EventsTemplates
     */
    @NotNull
    public List<TagEntity> getAllRealTagEntities() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT tt."+COLUMN_ID + ", "+COLUMN_TAGTYPE +", "+COLUMN_SIZE +", "+COLUMN_EVENT +" FROM " + TABLE_TAGS + " tt " +
                " INNER JOIN "+ TABLE_EVENTS  + " te " +
                " ON tt."+COLUMN_EVENT +" = te."+ COLUMN_ID +
                " WHERE te."+COLUMN_EVENTSTEMPLATE + " IS NULL;";
        Cursor c = null;
        List<TagEntity>toReturn = new ArrayList<>();
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                            int tagType = c.getInt(c.getColumnIndex(COLUMN_TAGTYPE));
                            double size = c.getDouble(c.getColumnIndex(COLUMN_SIZE));
                            int event = c.getInt(c.getColumnIndex(COLUMN_EVENT));
                            toReturn.add(new TagEntity(id, tagType, size, event));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;

    }
/*
        val id: Int,
        val dateTime: String,
        //this is unneccessary
        val typeOfEvent: Int,
        //we don't use eventstemplate for now
        val comment: String,
        val hasBreak: Boolean = false
        val portions: Double


 */
    @NotNull
    public List<MealEntity> getAllRealMealEntities() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT m."+COLUMN_PORTIONS + ", e."+COLUMN_ID +", "+COLUMN_DATETIME +", "+COLUMN_COMMENT +", "+COLUMN_HAS_BREAK +" FROM " + TABLE_EVENTS + " e " +
                " INNER JOIN "+ TABLE_MEALS  + " m " +
                " ON m."+COLUMN_EVENT +" = e."+ COLUMN_ID +
                " WHERE e."+COLUMN_TYPE_OF_EVENT + " =  "+ MEAL +";";
        Cursor c = null;
        List<MealEntity>toReturn = new ArrayList<>();
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            double portions = c.getDouble(c.getColumnIndex(COLUMN_PORTIONS));
                            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                            String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME ));
                            String comment = c.getString(c.getColumnIndex(COLUMN_COMMENT));
                            //sqlite stores boolean true as int 1, false as 0
                            boolean hasBreak = c.getInt(c.getColumnIndex(COLUMN_HAS_BREAK)) == 1 ?true:false;
                            toReturn.add(new MealEntity(portions, id, datetime, comment, hasBreak));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;

    }

    @NotNull
    public List<OtherEntity> getAllRealOtherEntities() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT e."+COLUMN_ID +", "+COLUMN_DATETIME +", "+COLUMN_COMMENT +", "+COLUMN_HAS_BREAK +" FROM " + TABLE_EVENTS + " e " +
                " INNER JOIN "+ TABLE_OTHERS  + " o " +
                " ON o."+COLUMN_EVENT +" = e."+ COLUMN_ID +
                " WHERE e."+COLUMN_TYPE_OF_EVENT + " =  "+ OTHER +";";
        Cursor c = null;
        List<OtherEntity>toReturn = new ArrayList<>();
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                            String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME ));
                            String comment = c.getString(c.getColumnIndex(COLUMN_COMMENT));
                            //sqlite stores boolean true as int 1, false as 0
                            boolean hasBreak = c.getInt(c.getColumnIndex(COLUMN_HAS_BREAK)) == 1 ?true:false;
                            toReturn.add(new OtherEntity(id, datetime, comment, hasBreak));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;

    }

    @NotNull
    public List<ExerciseEntity> getAllRealExerciseEntities() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT ex."+COLUMN_INTENSITY + ", e."+COLUMN_ID +", "+COLUMN_DATETIME +", "+COLUMN_COMMENT +", "+COLUMN_HAS_BREAK +" FROM " + TABLE_EVENTS + " e " +
                " INNER JOIN "+ TABLE_EXERCISES  + " ex " +
                " ON ex."+COLUMN_EVENT +" = e."+ COLUMN_ID +
                " WHERE e."+COLUMN_TYPE_OF_EVENT + " =  "+ EXERCISE +";";
        Cursor c = null;
        List<ExerciseEntity>toReturn = new ArrayList<>();
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            int intensity = c.getInt(c.getColumnIndex(COLUMN_INTENSITY));
                            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                            String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME ));
                            String comment = c.getString(c.getColumnIndex(COLUMN_COMMENT));
                            //sqlite stores boolean true as int 1, false as 0
                            boolean hasBreak = c.getInt(c.getColumnIndex(COLUMN_HAS_BREAK)) == 1 ?true:false;
                            toReturn.add(new ExerciseEntity(intensity, id, datetime, comment, hasBreak));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;

    }

    @NotNull
    public List<BmEntity> getAllRealBmEntities() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT bm."+COLUMN_COMPLETENESS + ", bm."+COLUMN_BRISTOL+ ", e."+COLUMN_ID +", "+COLUMN_DATETIME +", "+COLUMN_COMMENT +", "+COLUMN_HAS_BREAK +" FROM " + TABLE_EVENTS + " e " +
                " INNER JOIN "+ TABLE_BMS  + " bm " +
                " ON bm."+COLUMN_EVENT +" = e."+ COLUMN_ID +
                " WHERE e."+COLUMN_TYPE_OF_EVENT + " =  "+ BM +";";
        Cursor c = null;
        List<BmEntity>toReturn = new ArrayList<>();
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            int complete = c.getInt(c.getColumnIndex(COLUMN_COMPLETENESS));
                            int bristol = c.getInt(c.getColumnIndex(COLUMN_BRISTOL));
                            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                            String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME ));
                            String comment = c.getString(c.getColumnIndex(COLUMN_COMMENT));
                            //sqlite stores boolean true as int 1, false as 0
                            boolean hasBreak = c.getInt(c.getColumnIndex(COLUMN_HAS_BREAK)) == 1 ?true:false;
                            toReturn.add(new BmEntity(complete, bristol, id, datetime, comment, hasBreak));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;
    }

    @NotNull
    public List<RatingEntity> getAllRealRatingEntities() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT r."+COLUMN_AFTER + ", e."+COLUMN_ID +", "+COLUMN_DATETIME +", "+COLUMN_COMMENT +", "+COLUMN_HAS_BREAK +" FROM " + TABLE_EVENTS + " e " +
                " INNER JOIN "+ TABLE_RATINGS  + " r " +
                " ON r."+COLUMN_EVENT +" = e."+ COLUMN_ID +
                " WHERE e."+COLUMN_TYPE_OF_EVENT + " =  "+ RATING +";";
        Cursor c = null;
        List<RatingEntity>toReturn = new ArrayList<>();
        try {
            c = db.rawQuery(QUERY, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        try {
                            int after = c.getInt(c.getColumnIndex(COLUMN_AFTER));
                            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                            String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME ));
                            String comment = c.getString(c.getColumnIndex(COLUMN_COMMENT));
                            //sqlite stores boolean true as int 1, false as 0
                            boolean hasBreak = c.getInt(c.getColumnIndex(COLUMN_HAS_BREAK)) == 1 ?true:false;
                            toReturn.add(new RatingEntity(after, id, datetime, comment, hasBreak));
                        } finally {
                            c.moveToNext();
                        }
                    }
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }
        return toReturn;
    }
}