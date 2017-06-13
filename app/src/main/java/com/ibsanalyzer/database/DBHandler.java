package com.ibsanalyzer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.R.attr.type;
import static android.media.tv.TvContract.Channels.COLUMN_TYPE;
import static android.os.Build.VERSION_CODES.M;
import static com.ibsanalyzer.constants.Constants.BM;
import static com.ibsanalyzer.constants.Constants.EXERCISE;
import static com.ibsanalyzer.constants.Constants.MEAL;
import static com.ibsanalyzer.constants.Constants.OTHER;
import static com.ibsanalyzer.constants.Constants.RATING;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_AFTER;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_BRISTOL;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_COMPLETENESS;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_DATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_EVENT;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_EVENTSTEMPLATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_ID;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_INTENSITY;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_IS_A;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_NAME;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_PORTIONS;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_SIZE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAG;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGNAME;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGTEMPLATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TYPE_OF_EVENT;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_BM_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_EVENTS_TEMPLATE_TABLE;
import static com.ibsanalyzer.database.TablesAndStrings.CREATE_EVENTS_TEMPLATE_TO_EVENT_TABLE;
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
import static com.ibsanalyzer.database.TablesAndStrings.NO_INHERITANCE;
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
import static com.ibsanalyzer.inputday.R.drawable.meal;
import static com.ibsanalyzer.inputday.R.id.bristol;
import static com.ibsanalyzer.inputday.R.id.intensity;
import static com.ibsanalyzer.inputday.R.id.portions;


/**
 * Created by Johan on 2017-05-06.
 * see p. 554
 * see https://sqlite.org/foreignkeys.html for creation of foreign keys.
 */

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL(CREATE_EVENTS_TEMPLATE_TO_EVENT_TABLE);
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


    //===================================================================================
    //TagTemplate
    //===================================================================================
    public void addTagTemplate(TagTemplate tagTemplate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAGNAME, tagTemplate.get_tagname());
        TagTemplate parent = tagTemplate.get_is_a1();
        if (parent == null) {
            values.put(COLUMN_IS_A, NO_INHERITANCE); //OK MED -1???
        } else {
            values.put(COLUMN_IS_A, tagTemplate.get_is_a1().get_tagname());
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TAGTEMPLATES, null, values);
        db.close();
        //Log.d("Debug", "addTagTemplate completed! TagTemplate " + tagTemplate.get_tagname() + "
        // with id nr: " + findTagTemplate(tagTemplate.get_tagname()).get_id() + " inserted!");
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

    private TagTemplate findTagTemplateHelper2(Cursor cursor) {
        TagTemplate tt = new TagTemplate();
        tt.set_id(cursor.getInt(0));
        tt.set_tagname(cursor.getString(1));
        TagTemplate parentTag;
        if (cursor.getString(2) == NO_INHERITANCE) {
            parentTag = null;
        } else {
            parentTag = findTagTemplate(cursor.getString(2));
        }
        tt.set_is_a1(parentTag);
        return tt;
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
                tagTemplate.set_id(Integer.parseInt(cursor.getString(0)));
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

    public boolean deleteAllTagTemplates() {
        SQLiteDatabase db = this.getWritableDatabase();
        int doneDelete = db.delete(TABLE_TAGTEMPLATES, null, null);
        db.close();
        return doneDelete > 0;
    }

    public void createSomeTagTemplates() {
        addTagTemplate(new TagTemplate("dairy"));
        addTagTemplate(new TagTemplate("yoghurt", findTagTemplate("dairy")));
        addTagTemplate(new TagTemplate("wheat"));
        addTagTemplate(new TagTemplate("running"));
        addTagTemplate(new TagTemplate("sleep"));
        addTagTemplate(new TagTemplate("sugar"));
        addTagTemplate(new TagTemplate("honey"));
        addTagTemplate(new TagTemplate("pizza", findTagTemplate("wheat")));
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
                            COLUMN_TAGNAME, COLUMN_IS_A},
                    null, null, null, null, null);

        } else {
            mCursor = db.query(true, TABLE_TAGTEMPLATES, new String[]{COLUMN_ID,
                            COLUMN_TAGNAME, COLUMN_IS_A},
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
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGTEMPLATES, null, null);
        db.delete(TABLE_TAGS, null, null);
        db.delete(TABLE_EVENTS, null, null);
        db.delete(TABLE_RATINGS, null, null);
        db.delete(TABLE_BMS, null, null);
        db.delete(TABLE_EXERCISES, null, null);
        db.delete(TABLE_OTHERS, null, null);
        db.delete(TABLE_MEALS, null, null);
        db.delete(TABLE_EVENTSTEMPLATES, null, null);
        db.delete(TABLE_EVENTSTEMPLATEEVENTS, null, null);
        db.close();
    }

    //==============================================================================================
    //EventsTemplate functions
    //==============================================================================================
    public void addEventsTemplate(EventsTemplate et) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, et.getNameOfTemplate());
        SQLiteDatabase db = this.getWritableDatabase();

        //https://sqlite.org/c3ref/last_insert_rowid.html
        //insert returns rowId => If the table has a column of type INTEGER PRIMARY KEY then that
        // column is another alias for the rowid.
        long template_id = db.insert(TABLE_EVENTSTEMPLATES, null, values);

        for (Event e : et.getEvents()) {
            ContentValues eventValue = new ContentValues();
            eventValue.put(COLUMN_DATE, DateTimeFormat.toSqLiteFormat(e.getTime()));
            long event_id = db.insert(TABLE_EVENTS, null, eventValue);

            //insert into many-to-many table
            ContentValues eventsTemplateToEvent = new ContentValues();
            values.put(COLUMN_EVENT, event_id);
            values.put(COLUMN_EVENTSTEMPLATE, template_id);
        }
        db.close();
        //Log.d("Debug", "addTagTemplate completed! TagTemplate " + tagTemplate.get_tagname() + "
        // with id nr: " + findTagTemplate(tagTemplate.get_tagname()).get_id() + " inserted!");

    }

    public Cursor getCursorToEventsTemplates() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT  * FROM " + TABLE_EVENTSTEMPLATES, null);
    }

    //===================================================================================
    //Event
    //===================================================================================
    //-----------------------------------------------------------------------------------
    //add
    //-----------------------------------------------------------------------------------
    private long addEvent(Event event, long typeOfEvent) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, DateTimeFormat.toSqLiteFormat(event.getTime()));
        values.put(COLUMN_TYPE_OF_EVENT, typeOfEvent);
        SQLiteDatabase db = this.getWritableDatabase();
        long eventId = db.insert(TABLE_EVENTS, DATABASE_NAME, values);
        //if inputEvent => add tags
        if (event instanceof InputEvent) {
            InputEvent ie = (InputEvent) event;
            for (Tag t : ie.getTags()) {
                addTag(t, eventId);

            }
        }
        db.close();
        return eventId;
    }

    //-----------------------------------------------------------------------------------
    //gets
    //-----------------------------------------------------------------------------------
    public List<Event> getAllEventsSorted() {
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + TABLE_EVENTS + " ORDER BY " + COLUMN_DATE + " ASC";
        Cursor c = db.rawQuery(QUERY, null);
        List<Event> eventList = new ArrayList<>();
        if (c != null) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    long eventId = c.getLong(c.getColumnIndex(COLUMN_ID));
                    String date = c.getString(c.getColumnIndex(COLUMN_DATE));
                    LocalDateTime ldt = DateTimeFormat.fromSqLiteFormat(date);
                    int typeOfEvent = c.getInt(c.getColumnIndex(COLUMN_TYPE_OF_EVENT));  //här kraschar det.
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
        return meal;
    }
    private Event retrieveOther(long eventId, LocalDateTime ldt) {
        Cursor c = retrieveHelper(eventId, TABLE_OTHERS);
        Other other = null;
        if (c != null) {
            if (c.moveToFirst()) {
                List<Tag> tags = getTagsWithEventId(eventId);
                other = new Other(ldt, tags);
            }
        }
        c.close();
        this.close();
        return other;
    }
    private Event retrieveExercise(long eventId, LocalDateTime ldt) {
        Cursor c = retrieveHelper(eventId, TABLE_EXERCISES);
        Exercise exercise = null;
        if (c != null) {
            if (c.moveToFirst()) {
                int intensity = c.getInt(c.getColumnIndex(COLUMN_INTENSITY));
                String tagName = getTagname(c.getColumnIndex(COLUMN_TAGTEMPLATE));
                Tag tag = new Tag (ldt, tagName, 1);
                exercise = new Exercise(ldt, tag, intensity);
            }
        }
        c.close();
        this.close();
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
        return rating;
    }
    //db must be closed elsewhere, if closed here using methods will fail using cursor
    private Cursor retrieveHelper(long eventId, String nameOfEventTable){
        SQLiteDatabase db = this.getReadableDatabase();
        final String QUERY = "SELECT * FROM " + nameOfEventTable + " WHERE " + COLUMN_EVENT + " = ?";
        Cursor c= db.rawQuery(QUERY, new String[]{String.valueOf(eventId)});
        return c;
    }

    LocalDateTime getDateFromEvent(long eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_DATE + " FROM " + TABLE_EVENTS + " WHERE " + COLUMN_ID
                + " = ?";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(eventId)});
        LocalDateTime ldt = null;
        if (c != null) {
            if (c.moveToFirst()) {
                String date = c.getString(c.getColumnIndex(COLUMN_DATE));
                ldt = DateTimeFormat.fromSqLiteFormat(date);


            }
        }
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
        //now create meal
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_PORTIONS, meal.getPortions());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_MEALS, DATABASE_NAME, values);


        //testing if anything has been put in
        String count = "SELECT count(*) FROM " + TABLE_MEALS;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        //OK! icount == 1
        db.close();
    }

    //There can be several events one time, but only one meal.
    //apart from testing, is this method useless???
    // => depends on how statistics classes will be formed and how events will be retrieved from
    // diary list.
    public Meal retrieveMealByTime(LocalDateTime ldt) {

        Meal returnMeal = null;
        //select from meals where its event has time ...
        final String QUERY = "SELECT " + "a." + COLUMN_PORTIONS + ", a."
                + COLUMN_EVENT + " FROM " +
                TABLE_MEALS + " a "
                + " INNER JOIN " + TABLE_EVENTS + " b ON " + " a." + COLUMN_EVENT + " = b." +
                COLUMN_ID; //+
        //  " WHERE " + "b."+ COLUMN_DATE + " =?";

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
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        long tagTemplateId = getTagTemplateId(exercise.getTypeOfExercise().getName());
        values.put(COLUMN_TAGTEMPLATE, tagTemplateId);
        values.put(COLUMN_INTENSITY, (long)exercise.getIntensity());
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
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_BRISTOL, bm.getBristol());
        values.put(COLUMN_COMPLETENESS, bm.getComplete());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_BMS, DATABASE_NAME, values);
        db.close();
    }
    public void addRating(Rating rating) {
        long eventId = addEvent(rating, RATING);
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT, eventId);
        values.put(COLUMN_AFTER, (long)rating.getAfter());
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
                    String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
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
        values.put(COLUMN_DATE, DateTimeFormat.toSqLiteFormat(t.getTime()));
        values.put(COLUMN_SIZE, t.getSize());

        long tagTemplateId = getTagTemplateId(t.getName());
        values.put(COLUMN_TAGTEMPLATE, tagTemplateId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TAGS, null, values);
        db.close();
    }
}
