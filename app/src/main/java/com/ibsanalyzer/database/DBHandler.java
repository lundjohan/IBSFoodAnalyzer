package com.ibsanalyzer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.InputEvent;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.date_time.DateTimeFormat;
import com.ibsanalyzer.model.EventsTemplate;
import com.ibsanalyzer.model.TagTemplate;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_DATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_EVENT;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_EVENTSTEMPLATE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_ID;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_IS_A;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_NAME;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_PORTIONS;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_SIZE;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGNAME;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGTEMPLATE;
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


/**
 * Created by Johan on 2017-05-06.
 * see p. 554
 * see https://sqlite.org/foreignkeys.html for creation of foreign keys.
 */

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int
            version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
    //add methods
    //===================================================================================
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

    private long addEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, DateTimeFormat.toSqLiteFormat(event.getTime()));
        SQLiteDatabase db = this.getWritableDatabase();
        long eventId = db.insert(TABLE_EVENTS, null, values);
        //if inputEvent => add tags
        if (event instanceof InputEvent) {
            InputEvent ie = (InputEvent) event;
            for (Tag t : ie.getTags()) {
                addTag(t, eventId);

            }
        }
        return eventId;
    }


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

    //===================================================================================
    //find and get methods
    //===================================================================================

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


    //
    public String getTagname(long id) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_ID + " = \"" +
                id + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String tagname = "";
        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            tagname = cursor.getString(1);
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

    //===================================================================================
    //delete methods
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


    public boolean deleteAllTagTemplates() {
        SQLiteDatabase db = this.getWritableDatabase();
        int doneDelete = db.delete(TABLE_TAGTEMPLATES, null, null);
        db.close();
        return doneDelete > 0;
    }


    //===================================================================================
    //static create methods
    //===================================================================================
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

    //===================================================================================
    //get cursor methods
    //===================================================================================
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

    //==============================================================================================
    // Meal methods
    //==============================================================================================
    public void addMeal(Meal meal) {
        //first create event and obtain its id
        long eventId = addEvent(meal);
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

        //testing if anything has been put in
        String count = "SELECT count(*) FROM " + TABLE_MEALS;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        Cursor cursor = db.rawQuery(QUERY, null);


        //debugging
        String cursorstr = DatabaseUtils.dumpCursorToString(cursor);
        Log.d("Debug", cursorstr);


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

    public List<Event> getAllEvents() {
        List<Event>eventList = new ArrayList<>();
        eventList.addAll(getAllMeals());
        eventList.addAll(getAllOthers());
        eventList.addAll(getAllExercises());
        eventList.addAll(getAllBMs());
        eventList.addAll(getAllRatings());
        return eventList;
    }
}
