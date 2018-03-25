package com.johanlund.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.InputEvent;
import com.johanlund.base_classes.Rating;
import com.johanlund.base_classes.Tag;
import com.johanlund.date_time.DateTimeFormat;
import com.johanlund.exceptions.CorruptedEventException;
import com.johanlund.model.EventsTemplate;
import com.johanlund.model.TagTemplate;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.johanlund.constants.Constants.BM;
import static com.johanlund.constants.Constants.EXERCISE;
import static com.johanlund.constants.Constants.MEAL;
import static com.johanlund.constants.Constants.OTHER;
import static com.johanlund.constants.Constants.RATING;
import static com.johanlund.database.TablesAndStrings.COLUMN_AFTER;
import static com.johanlund.database.TablesAndStrings.COLUMN_BRISTOL;
import static com.johanlund.database.TablesAndStrings.COLUMN_COMMENT;
import static com.johanlund.database.TablesAndStrings.COLUMN_COMPLETENESS;
import static com.johanlund.database.TablesAndStrings.COLUMN_DATE;
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
import static com.johanlund.database.TablesAndStrings.COLUMN_TAGTEMPLATE;
import static com.johanlund.database.TablesAndStrings.COLUMN_TYPE_OF_EVENT;
import static com.johanlund.database.TablesAndStrings.CREATE_BM_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_EVENTS_TEMPLATE_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_EVENT_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_EXERCISE_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_MEAL_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_OTHER_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_RATING_TABLE;
import static com.johanlund.database.TablesAndStrings.CREATE_TAGTEMPLATE_TABLE;
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
import static com.johanlund.database.TablesAndStrings.TABLE_TAGTEMPLATES;
import static com.johanlund.database.TablesAndStrings.TYPE_OF;

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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //since this version Rating scale has been reduced to 6 from 7. 'Abysmal' has disappeared and will be joined with 'Awful'.
        if (oldVersion <= 37) {
            db.execSQL(CHANGE_RATING_SCALE);
        }
    }
    private String CHANGE_RATING_SCALE =
            "UPDATE "+ TABLE_RATINGS + " SET " +
                    COLUMN_AFTER + " = " +COLUMN_AFTER + " -1 " + " WHERE "+ COLUMN_AFTER + " != '1' ";



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
                            COLUMN_TAGNAME, TYPE_OF},
                    null, null, null, null, null);

        } else {
            mCursor = db.query(true, TABLE_TAGTEMPLATES, new String[]{COLUMN_ID,
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
                    } catch (CorruptedEventException e) {
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
    public void addEvents(List<Event>events){
        for (Event e: events){
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
        values.put(COLUMN_DATE, DateTimeFormat.dateToSqLiteFormat(e.getTime().toLocalDate()));
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
            for (Tag t : ie.getTags()) {
                addTagAndPossiblyTagTemplate(t, eventId);

            }
        }
        //Exercise is does not inherit InputEvent but has one tag
        else if (event instanceof com.johanlund.base_classes.Exercise) {
            Tag t = ((com.johanlund.base_classes.Exercise) event).getTypeOfExercise();
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

    //notice how this method use null in select statement to avoid retrieving events from
    // eventstemplates
    public List<Event> getAllEventsMinusEventsTemplatesSorted() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + COLUMN_EVENTSTEMPLATE +
                " IS NULL " + " ORDER BY " + COLUMN_DATETIME + "" + " ASC";
        Cursor c = db.rawQuery(QUERY, null);
        return getEventsRetrieverHelper(c);
    }

    /**
     *
     * @param currentDate
     * @return an empty List<Event> in case the there exists no events that day
     */
    public List<Event> getAllEventsMinusEventsTemplateSortedFromDay(LocalDate currentDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + COLUMN_DATE + " =? " +
                " AND " + COLUMN_EVENTSTEMPLATE + " IS NULL " +
                " ORDER BY " + COLUMN_DATETIME + "" +
                " ASC";
        Cursor c = db.rawQuery(QUERY, new String[]{DateTimeFormat.dateToSqLiteFormat(currentDate)});
        return getEventsRetrieverHelper(c);
    }

    private Event getEvent(long eventId, Cursor c) throws CorruptedEventException {
        String datetime = c.getString(c.getColumnIndex(COLUMN_DATETIME));
        int type = c.getInt(c.getColumnIndex(COLUMN_TYPE_OF_EVENT));
        String comment = c.getString(c.getColumnIndex(COLUMN_COMMENT));
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
            CorruptedEventException {
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
            throw new CorruptedEventException("Event should not be null here");
        }
        event.setComment(comment);
        event.setHasBreak(hasBreak);
        return event;
    }

    private Event retrieveMeal(long eventId, LocalDateTime ldt) throws CorruptedEventException {
        Cursor c = retrieveHelper(eventId, TABLE_MEALS);
        com.johanlund.base_classes.Meal meal = null;
        if (c != null) {
            if (c.moveToFirst()) {  //hoppar här
                double portions = c.getDouble(c.getColumnIndex(COLUMN_PORTIONS));
                List<Tag> tags = getTagsWithEventId(eventId);
                meal = new com.johanlund.base_classes.Meal(ldt, tags, portions);
            }
        }
        c.close();
        this.close();
        if (meal == null) {
            throw new CorruptedEventException("Meal event should not be null here");
        }
        return meal;
    }

    private Event retrieveOther(long eventId, LocalDateTime ldt) throws CorruptedEventException {
        Cursor c = retrieveHelper(eventId, TABLE_OTHERS);
        com.johanlund.base_classes.Other other = null;
        if (c != null) {
            if (c.moveToFirst()) {  //k
                List<Tag> tags = getTagsWithEventId(eventId);
                other = new com.johanlund.base_classes.Other(ldt, tags);
            }
        }
        c.close();
        this.close();
        if (other == null) {
            throw new CorruptedEventException("Other event should not be null here");
        }
        return other;
    }

    private Event retrieveExercise(long eventId, LocalDateTime ldt) throws CorruptedEventException {
        Cursor c = retrieveHelper(eventId, TABLE_EXERCISES);
        com.johanlund.base_classes.Exercise exercise = null;
        if (c != null) {
            if (c.moveToFirst()) {
                int intensity = c.getInt(c.getColumnIndex(COLUMN_INTENSITY));
                Tag tag = getTagsWithEventId(eventId).get(0);
                exercise = new com.johanlund.base_classes.Exercise(ldt, tag, intensity);
            }
        }
        c.close();
        this.close();
        if (exercise == null) {
            throw new CorruptedEventException("Exercise event should not be null here");
        }
        return exercise;
    }

    private Event retrieveBm(long eventId, LocalDateTime ldt) throws CorruptedEventException {
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
            throw new CorruptedEventException("Bm event should not be null here");
        }
        return bm;
    }

    private Event retrieveRating(long eventId, LocalDateTime ldt) throws CorruptedEventException {
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
            throw new CorruptedEventException("Rating event should not be null here");
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

            List<Tag> tags = getTagsWithEventId(eventId);
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
        List<Tag> tags = getTagsWithEventId(eventId);
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

    /**
     * Note that this method also adds TagTemplate in case it is missing
     * @param t
     * @param eventId
     */
    private void addTagAndPossiblyTagTemplate(Tag t, long eventId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_DATETIME, DateTimeFormat.toSqLiteFormat(t.getTime()));
        values.put(COLUMN_SIZE, t.getSize());

        long tagTemplateId = getTagTemplateId(t.getName());
        if (tagTemplateId == -1){
            TagTemplate tt = new TagTemplate(t.getName(), null);
            tagTemplateId = addTagTemplate(tt);
        }
        values.put(COLUMN_TAGTEMPLATE, tagTemplateId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TAGS, null, values);
        db.close();
    }

    //===================================================================================
    //TagTemplate
    //===================================================================================
    public long addTagTemplate(TagTemplate tagTemplate) {
        ContentValues values = makeTagTemplateContentValues(tagTemplate);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_TAGTEMPLATES, null, values);
        db.close();
        return id;
        //Log.d("Debug", "addTagTemplate completed! TagTemplate " + tagTemplate.get_tagname() + "
        // with id nr: " + findTagTemplate(tagTemplate.get_tagname()).get_id() + " inserted!");
    }

    public void deleteTagTemplate(long idOfTagTemplate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGTEMPLATES, "_id=" + idOfTagTemplate, null);
        db.close();
    }

    public String getTagTemplateName(long idOfTagTemplate) {
        String toReturn = "";
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_ID + " =?";
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

    public void editTagTemplate(TagTemplate tagTemplate, long idOfTagTemplate) {
        ContentValues values = makeTagTemplateContentValues(tagTemplate);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_TAGTEMPLATES, values, "_id=" + idOfTagTemplate, null);
        db.close();
    }

    public void removeExercisesWithTagTemplate(long idOfTagTemplate) {
        //1. get all eventId where exercises has a tag with tagtemplate_id == idOfTagTemplate
        final String QUERY = "SELECT " + "a." + COLUMN_EVENT + " FROM " +
                TABLE_EXERCISES + " a "
                + " INNER JOIN " + TABLE_TAGS + " b ON " + " a." + COLUMN_EVENT + " = b." +
                COLUMN_EVENT + " WHERE b." + COLUMN_TAGTEMPLATE + " =? ";
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

    private ContentValues makeTagTemplateContentValues(TagTemplate tagTemplate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAGNAME, tagTemplate.get_tagname());
        actOnTagTemplateChild(values, tagTemplate.get_type_of(), TYPE_OF);
        return values;
    }

    private void actOnTagTemplateChild(ContentValues values, TagTemplate child, String
            childColumn) {
        if (child == null) {
            values.putNull(childColumn);
        } else {
            values.put(childColumn, getTagTemplateId(child.get_tagname()));
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

    public TagTemplate findTagTemplate(long id) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_ID + " = \"" +
                String.valueOf(id) + "\"";
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
        tt.set_is_a1(retrieveOneParentToTagTemplate(c, TYPE_OF));
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
            while (!cursor.isAfterLast()) {
                TagTemplate tagTemplate = new TagTemplate("");
                tagTemplate.set_tagname(cursor.getString(1));
                tagTemplates.add(tagTemplate);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return tagTemplates;
    }

    //almost copy-pasted from getEventsRetrieverHelper
    private List<Rating> getRatingsRetrieverHelper(Cursor c){
        List<Rating> ratingList = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    try {

                        long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                        Event event = getEvent(eventId, c);

                        ratingList.add((Rating)event);
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

    public LocalDate getDateOfLastEvent() {
        SQLiteDatabase db = this.getReadableDatabase();
        //must use datetime and not date since datetime can be used with MAX in sqlite
        final String QUERY = "Select MAX (" + COLUMN_DATETIME + ") FROM " + TABLE_EVENTS;
        Cursor c = db.rawQuery(QUERY, null);
        LocalDate ld = null;
        if (c != null) {
            c.moveToFirst();
            try {
                String dateTimeStr = c.getString(0);
                LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(dateTimeStr);
                ld = ldt.toLocalDate();
            } catch (Exception e) {
                ld = null;
                // Log.e(TAG, e.getMessage());
            }
        }
        c.close();
        db.close();
        return ld;

    }

    public boolean diaryIsEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_EVENTS;
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        if (c.getInt(0) > 0) {
            return false;
        }
        return true;
    }
}

