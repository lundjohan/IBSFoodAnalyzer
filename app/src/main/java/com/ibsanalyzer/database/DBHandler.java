package com.ibsanalyzer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.InputEvent;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.date_time.DateTimeFormat;
import com.ibsanalyzer.model.EventsTemplate;
import com.ibsanalyzer.model.TagTemplate;
import com.ibsanalyzer.util.Util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.BM;
import static com.ibsanalyzer.constants.Constants.EXERCISE;
import static com.ibsanalyzer.constants.Constants.MEAL;
import static com.ibsanalyzer.constants.Constants.OTHER;
import static com.ibsanalyzer.constants.Constants.RATING;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_AFTER;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_BRISTOL;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_COMPLETENESS;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_DATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_DATETIME;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_EVENT;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_EVENTSTEMPLATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_ID;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_INTENSITY;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_NAME;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_PORTIONS;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_SIZE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGNAME;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGTEMPLATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TYPE_OF_EVENT;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_BM_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_EVENTS_TEMPLATE_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_EVENT_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_EXERCISE_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_MEAL_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_OTHER_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_RATING_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_TAGTEMPLATE_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_TAG_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.DATABASE_NAME;
import static com.ibsanalyzer.database.TablesAndStrings.DATABASE_VERSION;
import static com.ibsanalyzer.database.TablesAndStrings.ENABLE_FOREIGN_KEYS;
import static com.ibsanalyzer.database.TablesAndStrings.FIRST_COLUMN_IS_A;
import static com.ibsanalyzer.database.TablesAndStrings.SECOND_COLUMN_IS_A;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_BMS;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_EVENTS;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_EVENTSTEMPLATEEVENTS;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_EVENTSTEMPLATES;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_EXERCISES;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_MEALS;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_OTHERS;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_RATINGS;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_TAGS;
import static com.ibsanalyzer.database.TablesAndStrings.TABLE_TAGTEMPLATES;
import static com.ibsanalyzer.database.TablesAndStrings.THIRD_COLUMN_IS_A;

/**
 * Created by Johan on 2017-05-06.
 * see p. 554
 * see https://sqlite.org/foreignkeys.html for creation of foreign keys.
 */

public class DBHandler extends SQLiteOpenHelper {
    private final String TAG = DBHandler.class.toString();

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL(ENABLE_FOREIGN_KEYS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Debug", "inside DBHANDLER onCreate");
        db.execSQL(ENABLE_FOREIGN_KEYS);

        db.execSQL(CREATE_TAGTEMPLATE_TABLE);
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  // Drop older
        // table if existed
        Log.d("Debug", "inside DBHANDLER onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTSTEMPLATEEVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTSTEMPLATES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGTEMPLATES);

        // Create tables again
        onCreate(db);
    }


    //-----------------------------------------------------------------------------------
    //get cursor methods
    //-----------------------------------------------------------------------------------
    public Cursor getCursorToTagTemplates() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT  * FROM " + TABLE_TAGTEMPLATES, null);
    }

    public Cursor fetchTagTemplatesByName(String inputText) throws SQLException {
        Cursor mCursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        if (inputText == null || inputText.length() == 0) {

            mCursor = db.query(TABLE_TAGTEMPLATES, new String[]{COLUMN_ID,
                            COLUMN_TAGNAME, FIRST_COLUMN_IS_A, SECOND_COLUMN_IS_A,
                            THIRD_COLUMN_IS_A},
                    null, null, null, null, null);

        } else {
            mCursor = db.query(true, TABLE_TAGTEMPLATES, new String[]{COLUMN_ID,
                            COLUMN_TAGNAME, FIRST_COLUMN_IS_A, SECOND_COLUMN_IS_A,
                            THIRD_COLUMN_IS_A},
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
        deleteAllTablesRowsExceptTagTemplates();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGTEMPLATES, null, null);
        db.close();
    }
    public void deleteAllTablesRowsExceptTagTemplates() {
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
            int type = Util.getTypeOfEvent(e);
            long idOfEventAdded = addEventToRefEventsTemplate(e, type, template_id);
            Log.d("Debug", "id of event added to EventsTemplate: " + idOfEventAdded);
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
                addToMealTable(idOfEvent, (Meal) e);
                break;
            }
            case OTHER: {
                addToOtherTable(idOfEvent, (Other) e);
                break;
            }
            case EXERCISE: {
                addToExerciseTable(idOfEvent, (Exercise) e);
                break;
            }
            case BM: {
                addToBmTable(idOfEvent, (Bm) e);
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
                    long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                    Log.d("Debug", "eventId loaded from inside an EventsTemplate: " + eventId);
                    Event e = getEvent(eventId);
                    events.add(e); //här: e == null
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
     * This is used when creating Events inside an EventsTemplate
     * <p>
     * returns id of event added
     */
    private long addEventToRefEventsTemplate(Event event, long typeOfEvent, long
            idOfEventsTemplate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENTSTEMPLATE, idOfEventsTemplate);
        return addEventHelper1(event, values, typeOfEvent);
    }

    /**
     * This is used when creating Events NOT inside an EventsTemplate.
     * This is the "normal" use case.
     */
    public long addEvent(Event event, long typeOfEvent) {
        ContentValues values = new ContentValues();
        return addEventHelper1(event, values, typeOfEvent);
    }

    private long addEventHelper1(Event event, ContentValues values, long typeOfEvent){
        values.put(COLUMN_DATETIME, DateTimeFormat.toSqLiteFormat(event.getTime()));
        values.put (COLUMN_DATE,DateTimeFormat.dateToSqLiteFormat(event.getTime().toLocalDate()));
        values.put(COLUMN_TYPE_OF_EVENT, typeOfEvent);
        return addEventHelper2(event, values);
    }
    private long addEventHelper2(Event event, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long eventId = db.insert(TABLE_EVENTS, DATABASE_NAME, values);
        //if inputEvent (Meal, Other)=> add tags
        if (event instanceof InputEvent) {
            InputEvent ie = (InputEvent) event;
            for (Tag t : ie.getTags()) {
                addTag(t, eventId);

            }
        }
        //Exercise is does not inherit InputEvent but has one tag
        else if (event instanceof Exercise){
            Tag t = ((Exercise) event).getTypeOfExercise();
            addTag(t, eventId);
        }
        db.close();
        return eventId;
    }

    //-----------------------------------------------------------------------------------
    //delete
    //-----------------------------------------------------------------------------------
    public void deleteEvent(Event event) {
        long eventId = getEventId(event);
        if (eventId < 0) {
            throw new RuntimeException("Trying to delete event with invalid eventId");
        }
        deleteEvent(getEventId(event));
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
    public long getEventId(Event event) {
        int type = Util.getTypeOfEvent(event);
        String date = DateTimeFormat.toSqLiteFormat(event.getTime());
        return getEventId(type, date);
    }

    /**
     * @param typeOfEvent
     * @param date
     * @return returns -1 if no such event exists
     */
    private long getEventId(int typeOfEvent, String date) {
        long eventId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT " + COLUMN_ID + " FROM " + TABLE_EVENTS + " WHERE " +
                COLUMN_TYPE_OF_EVENT +
                " = ? AND " + COLUMN_DATETIME + " = ?";
        Cursor c = db.rawQuery(QUERY, new String[]{String.valueOf(typeOfEvent), date});
        if (c != null) {
            if (c.moveToFirst()) {
                eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
            }
        }
        db.close();
        return eventId;
    }

    /**
     * Changes a Meal in database to toMeal (id:s doesn't have to be the same afterwards).
     *
     * @param eventId
     * @param toMeal
     * @return
     */
    public void changeMeal(long eventId, Meal toMeal) {
        deleteEvent(eventId);
        addMeal(toMeal);
    }

    public void changeOther(long eventId, Other toOther) {
        deleteEvent(eventId);
        addOther(toOther);
    }

    public void changeExercise(long eventId, Exercise toExercise) {
        deleteEvent(eventId);
        addExercise(toExercise);
    }

    public void changeBm(long eventId, Bm toBm) {
        deleteEvent(eventId);
        addBm(toBm);
    }

    public void changeRating(long eventId, Rating toRating) {
        deleteEvent(eventId);
        addRating(toRating);
    }

    public List<Event> getAllEventsSorted() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + TABLE_EVENTS + " ORDER BY " + COLUMN_DATETIME + "" +
                " ASC";
        Cursor c = db.rawQuery(QUERY, null);
        List<Event> eventList = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                    String date = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                    LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(date);
                    int typeOfEvent = c.getInt(c.getColumnIndex(COLUMN_TYPE_OF_EVENT));
                    Event event = getEvent(eventId, ldt, typeOfEvent);
                    eventList.add(event);
                    c.moveToNext();
                }
            }
        }
        c.close();
        db.close();
        return eventList;
    }

    private Event getEvent(long eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Event e = null;
        final String QUERY = "SELECT *  FROM " + TABLE_EVENTS + " WHERE " +
                COLUMN_ID + " = ?";
        Cursor c = db.rawQuery(QUERY, new String[]{String.valueOf(eventId)});
        if (c != null) {
            if (c.moveToFirst()) {
                String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                int type = c.getInt(c.getColumnIndex(COLUMN_TYPE_OF_EVENT));
                e = getEvent(eventId, DateTimeFormat.fromSqLiteFormat(datetime), type);
            }
        }
        db.close();
        return e;
    }

    private Event getEvent(long eventId, LocalDateTime ldt, int typeOfEvent) {
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
            throw new NullPointerException("Event should not be null here");
        }
        return event;
    }

    private Event retrieveMeal(long eventId, LocalDateTime ldt) {
        Cursor c = retrieveHelper(eventId, TABLE_MEALS);
        Meal meal = null;
        if (c != null) {
            if (c.moveToFirst()) {
                double portions = c.getDouble(c.getColumnIndex(COLUMN_PORTIONS));
                List<Tag> tags = getTagsWithEventId(eventId);
                meal = new Meal(ldt, tags, portions);
            }
        }
        c.close();
        this.close();
        if (meal == null) {
            throw new NullPointerException("Meal event should not be null here");
        }
        return meal;
    }

    private Event retrieveOther(long eventId, LocalDateTime ldt) {
        Cursor c = retrieveHelper(eventId, TABLE_OTHERS);
        Other other = null;
        if (c != null) {
            if (c.moveToFirst()) {  //k
                List<Tag> tags = getTagsWithEventId(eventId);
                other = new Other(ldt, tags);
            }
        }
        c.close();
        this.close();
        if (other == null) {
            throw new NullPointerException("Other event should not be null here");
        }
        return other;
    }

    private Event retrieveExercise(long eventId, LocalDateTime ldt) {
        Cursor c = retrieveHelper(eventId, TABLE_EXERCISES);
        Exercise exercise = null;
        if (c != null) {
            if (c.moveToFirst()) {
                int intensity = c.getInt(c.getColumnIndex(COLUMN_INTENSITY));
                Tag tag = getTagsWithEventId(eventId).get(0);
                exercise = new Exercise(ldt, tag, intensity);
            }
        }
        c.close();
        this.close();
        if (exercise == null) {
            throw new NullPointerException("Exercise event should not be null here");
        }
        return exercise;
    }

    private Event retrieveBm(long eventId, LocalDateTime ldt) {
        Cursor c = retrieveHelper(eventId, TABLE_BMS);
        Bm bm = null;
        if (c != null) {
            if (c.moveToFirst()) {
                int complete = c.getInt(c.getColumnIndex(COLUMN_COMPLETENESS));
                int bristol = c.getInt(c.getColumnIndex(COLUMN_BRISTOL));
                bm = new Bm(ldt, complete, bristol);
            }
        }
        c.close();
        this.close();
        if (bm == null) {
            throw new NullPointerException("Bm event should not be null here");
        }
        return bm;
    }

    private Event retrieveRating(long eventId, LocalDateTime ldt) {
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
            throw new NullPointerException("Rating event should not be null here");
        }
        return rating;
    }

    //db must be closed elsewhere, if closed here using methods will fail using cursor
    private Cursor retrieveHelper(long eventId, String nameOfEventTable) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + nameOfEventTable + " WHERE " + COLUMN_EVENT + " =" +
                " ?";
        Cursor c = db.rawQuery(QUERY, new String[]{String.valueOf(eventId)});  //c blir null när laddas från eventstemplate, varför?
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
    List<Meal> getAllMeals() {

        List<Meal> meals = new ArrayList<>();
        final String QUERY = "SELECT * FROM " + TABLE_MEALS;

        //retrieve portions and event_id
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(QUERY, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            double portions = c.getDouble(c.getColumnIndex(COLUMN_PORTIONS));
            long eventId = c.getLong(c.getColumnIndex(COLUMN_EVENT));
            LocalDateTime ldt = getDateFromEvent(eventId);

            List<Tag> tags = getTagsWithEventId(eventId);
            Meal meal = new Meal(ldt, tags, portions);
            meals.add(meal);
            c.moveToNext();
        }
        c.close();
        db.close();
        return meals;
    }


    public void addMeal(Meal meal) {
        //first create event and obtain its id
        long eventId = addEvent(meal, MEAL);
        addToMealTable(eventId, meal);

    }

    private void addToMealTable(long eventId, Meal meal) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_PORTIONS, meal.getPortions());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_MEALS, DATABASE_NAME, values);
        db.close();
    }

    //There can be several events one time, but only one meal.
    //apart from testing, is this method useless???
    // => depends on how statistics classes will be formed and how events will be retrieved from
    // diary list.
    public Meal retrieveMealByTime(LocalDateTime ldt) {

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
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                portions = cursor.getDouble(cursor.getColumnIndex(COLUMN_PORTIONS));
                eventId = cursor.getLong(cursor.getColumnIndex(COLUMN_EVENT));
            }
        }
        cursor.close();
        db.close();
        List<Tag> tags = getTagsWithEventId(eventId);
        return new Meal(ldt, tags, portions);
    }

    //==============================================================================================
    // Other
    //==============================================================================================
    public void addOther(Other other) {
        //first create event and obtain its id
        long eventId = addEvent(other, OTHER);
        addToOtherTable(eventId, other);
    }

    private void addToOtherTable(long eventId, Other other) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_OTHERS, DATABASE_NAME, values);
        db.close();
    }

    //==============================================================================================
    // Exercise
    //==============================================================================================
    public void addExercise(Exercise exercise) {
        //first create event and obtain its id
        long eventId = addEvent(exercise, EXERCISE);
        addTag(exercise.getTypeOfExercise(), eventId);
        addToExerciseTable(eventId, exercise);
    }

    private void addToExerciseTable(long eventId, Exercise exercise) {
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
    public void addBm(Bm bm) {
        //first create event and obtain its id
        long eventId = addEvent(bm, BM);
        addToBmTable(eventId, bm);
    }

    private void addToBmTable(long eventId, Bm bm) {
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
        long eventId = addEvent(rating, RATING);
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
    private List<Tag> getTagsWithEventId(long event_id) {
        final String TAG_QUERY = "SELECT * FROM " + TABLE_TAGS + " WHERE " + COLUMN_EVENT + " =? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(TAG_QUERY, new String[]{String.valueOf(event_id)});
        List<Tag> tags = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {
                    String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATETIME));
                    LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(date);
                    double size = cursor.getDouble(cursor.getColumnIndex(COLUMN_SIZE));
                    long tagTemplateId = cursor.getLong(cursor.getColumnIndex(COLUMN_TAGTEMPLATE));
                    String tagname = getTagname(tagTemplateId);
                    Tag t = new Tag(ldt, tagname, size);
                    tags.add(t);
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        db.close();
        return tags;
    }

    private void addTag(Tag t, long eventId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_DATETIME, DateTimeFormat.toSqLiteFormat(t.getTime()));
        values.put(COLUMN_SIZE, t.getSize());

        long tagTemplateId = getTagTemplateId(t.getName());
        values.put(COLUMN_TAGTEMPLATE, tagTemplateId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TAGS, null, values);
        db.close();
    }

    //===================================================================================
    //TagTemplate
    //===================================================================================
    public void addTagTemplate(TagTemplate tagTemplate) {
        ContentValues values = makeTagTemplateContentValues(tagTemplate);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TAGTEMPLATES, null, values);
        db.close();
        //Log.d("Debug", "addTagTemplate completed! TagTemplate " + tagTemplate.get_tagname() + "
        // with id nr: " + findTagTemplate(tagTemplate.get_tagname()).get_id() + " inserted!");
    }

    public void editTagTemplate(TagTemplate tagTemplate, int idOfTagTemplate) {
        ContentValues values = makeTagTemplateContentValues(tagTemplate);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_TAGTEMPLATES, values, "_id=" + idOfTagTemplate, null);
        db.close();
    }

    private ContentValues makeTagTemplateContentValues(TagTemplate tagTemplate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAGNAME, tagTemplate.get_tagname());
        actOnTagTemplateChild(values, tagTemplate.get_is_a1(), FIRST_COLUMN_IS_A);
        actOnTagTemplateChild(values, tagTemplate.get_is_a2(), SECOND_COLUMN_IS_A);
        actOnTagTemplateChild(values, tagTemplate.get_is_a3(), THIRD_COLUMN_IS_A);
        return values;
    }

    private void actOnTagTemplateChild(ContentValues values, TagTemplate child, String
            childColumn) {
        if (child == null) {
            values.putNull(childColumn);
        } else {
            values.put(childColumn, child.get_tagname());
        }
    }

    public List<TagTemplate> getAllTagTemplates() {
        List<TagTemplate> tagTemplates = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TagTemplate tt = findTagTemplateHelper2(cursor);
                tagTemplates.add(tt);
                cursor.moveToNext();
            }
        }
        return tagTemplates;
    }

    public TagTemplate findTagTemplate(String tagName) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_TAGNAME + " = " +
                "\"" + tagName + "\"";
        return findTagTemplateHelper(query);
    }

    public TagTemplate findTagTemplate(int anInt) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_ID + " = \"" +
                String.valueOf(anInt) + "\"";
        return findTagTemplateHelper(query);
    }

    //returns null if there is no such TagTemplate in database
    private TagTemplate findTagTemplateHelper(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        TagTemplate tt = null;
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            tt = findTagTemplateHelper2(cursor);
        }
        return tt;
    }

    private TagTemplate findTagTemplateHelper2(Cursor c) {
        TagTemplate tt = new TagTemplate();
        tt.set_tagname(c.getString(c.getColumnIndex(COLUMN_TAGNAME)));

        //sets nulls or parent tagtemplates
        tt.set_is_a1(retrieveOneParentToTagTemplate(c, FIRST_COLUMN_IS_A));
        tt.set_is_a2(retrieveOneParentToTagTemplate(c, SECOND_COLUMN_IS_A));
        tt.set_is_a3(retrieveOneParentToTagTemplate(c, THIRD_COLUMN_IS_A));
        return tt;
    }

    /**
     * @param c
     * @param columnName
     * @return null if parent doesn't exist, otherwise return the parent
     */
    private TagTemplate retrieveOneParentToTagTemplate(Cursor c, String columnName) {
        TagTemplate parent;
        if (c.isNull(c.getColumnIndex(columnName))) {
            parent = null;
        } else {
            parent = findTagTemplate(c.getColumnIndex(columnName));
        }
        return parent;
    }

    public String getTagname(long id) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_ID + " = \"" +
                id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String tagname = "";
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            tagname = cursor.getString(cursor.getColumnIndex(COLUMN_TAGNAME));
        }
        db.close();
        return tagname;
    }

    public long getTagTemplateId(String name) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_TAGNAME + " = " +
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
    public List<TagTemplate> retrieveTagNames() {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<TagTemplate> tagTemplates = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            Log.d("Debug", "get position of cursor: " + cursor.getPosition());
            Log.d("Debug", "Do I ever come into while loop for cursor???");  //hit in kommer jag
            // inte.
            while (!cursor.isAfterLast()) {
                Log.d("Debug", "Do I ever come into while loop for cursor???");  //hit in kommer
                // jag inte.

                TagTemplate tagTemplate = new TagTemplate("");
                tagTemplate.set_tagname(cursor.getString(1));
                tagTemplates.add(tagTemplate);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        Log.d("Debug", "tagTemplates" + tagTemplates);
        return tagTemplates;
    }


    public void createSomeTagTemplates() {
        addTagTemplate(new TagTemplate("dairy"));
        addTagTemplate(new TagTemplate("yoghurt", findTagTemplate("dairy"), null, null));
        addTagTemplate(new TagTemplate("wheat"));
        addTagTemplate(new TagTemplate("running"));
        addTagTemplate(new TagTemplate("sleep"));
        addTagTemplate(new TagTemplate("sugar"));
        addTagTemplate(new TagTemplate("honey"));
        addTagTemplate(new TagTemplate("pizza", findTagTemplate("wheat"), null, null));
    }

    //===================================================================================
    //Import from txt
    //===================================================================================
    //this comes from txt file
    //important to add TagTemplates before adding events
    public void addEventsWithUnknownTagTemplates(List<Event> events) {
        for (Event e : events) {
            if (e instanceof Meal) {
                Meal meal = (Meal) e;
                List<Tag> tags = meal.getTags();
                addNewTagTemplateFromTags(tags);
                addMeal((meal));
            } else if (e instanceof Other) {
                Other other = (Other) e;
                List<Tag> tags = other.getTags();
                addNewTagTemplateFromTags(tags);
                addOther((other));
            } else if (e instanceof Exercise) {
                Exercise exercise = (Exercise) e;
                Tag tag = exercise.getTypeOfExercise();
                addTagTemplate(new TagTemplate(tag.getName()));
                addExercise((exercise));
            } else if (e instanceof Bm) {
                Bm bm = (Bm) e;
                addBm((bm));
            } else if (e instanceof Rating) {
                Rating rating = (Rating) e;
                addRating((rating));
            }

        }


    }

    private void addNewTagTemplateFromTags(List<Tag> tags) {
        for (Tag t : tags) {
            addTagTemplate(new TagTemplate(t.getName()));
        }
    }

    //99% copy pasted from DBHandler getAllEventsSorted.
    public List<Event> getAllEventsSortedFromDay(LocalDate currentDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + COLUMN_DATE + " =? " + " ORDER BY " + COLUMN_DATETIME + "" +
                " ASC";
        Cursor c = db.rawQuery(QUERY, new String[]{DateTimeFormat.dateToSqLiteFormat(currentDate)});
        List<Event> eventList = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                   try {

                       long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                       String dateTime = c.getString(c.getColumnIndex(COLUMN_DATETIME));
                       LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(dateTime);
                       int typeOfEvent = c.getInt(c.getColumnIndex(COLUMN_TYPE_OF_EVENT));
                       Event event = getEvent(eventId, ldt, typeOfEvent);
                       eventList.add(event);
                       c.moveToNext();
                   }
                   catch (Exception e){
                       Log.e(TAG, "Something went wrong reading an event, jumping to next");
                       Log.e(TAG, "exception", e);
                       c.moveToNext();
                   }
                }
            }
        }
        c.close();
        db.close();
        return eventList;
    }
}

