package com.ibsanalyzer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ibsanalyzer.model.TagTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Johan on 2017-05-06.
 * see p. 554
 * see https://sqlite.org/foreignkeys.html for creation of foreign keys.
 */

public class DBHandler extends SQLiteOpenHelper {
    //NO_INHERITANCE is used to say: "TagTemplate is not inheriting"
    public static final String NO_INHERITANCE = "0Null0";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tagnameDB.db";

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
    public static final String COLUMN_EVENTS = "events";

    //Event
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_TAGS = "tags";

    //Event => many-to-many =>  Tags
    private static final String TABLE_EVENTTAGS = "event_tags";
    private static final String COLUMN_EVENT = "event";
    private static final String COLUMN_TAG = "tag";

    //Meals
    private static final String TABLE_MEALS = "meals";
    private static final String COLUMN_PORTIONS = "portions";

    //Other
    private static final String TABLE_OTHERS = "others";

    //Exercise
    private static final String TABLE_EXERCISES = "exercises";
    private static final String COLUMN_INTENSITY = "intensity";

    //BMs
    private static final String TABLE_BMS = "bms";
    private static final String COLUMN_COMPLETENESS = "completeness";
    private static final String COLUMN_BRISTOL = "bristol";

    //Ratings
    private static final String TABLE_RATINGS = "ratings";
    private static final String COLUMN_AFTER = "after";

    //EventsTemplates
    private static final String TABLE_EVENTSTEMPLATES = "event_templates";

    //EventsTemplate => many-to-many => Events
    private static final String TABLE_EVENTSTEMPLATEEVENTS = "event_template_events";
    private static final String COLUMN_EVENTSTEMPLATE = "events_template";

    // see https://sqlite.org/foreignkeys.html for creation of foreign keys.
    public static final String CREATE_TAGTEMPLATE_TABLE = "CREATE TABLE " +
            TABLE_TAGTEMPLATES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_TAGNAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_IS_A + " TEXT CHECK(" + COLUMN_IS_A + " != " + COLUMN_TAGNAME + "),  " +
            " FOREIGN KEY(" + COLUMN_IS_A + ") REFERENCES " + TABLE_TAGTEMPLATES
            + " (" + COLUMN_TAGNAME + ")" +
            ");";
    //for date as int (which actually long) see => http://stackoverflow.com/questions/7363112/best-way-to-work-with-dates-in-android-sqlite
    public static final String CREATE_TAG_TABLE = "CREATE TABLE " +
            TABLE_TAGS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_TAGTEMPLATE + " INTEGER NOT NULL, " +
            COLUMN_SIZE + " REAL NOT NULL, " +
            COLUMN_DATE + " INTEGER NOT NULL, " +
            COLUMN_EVENTS + " INTEGER, " +
            " FOREIGN KEY(" + COLUMN_TAGTEMPLATE + ") REFERENCES " + TABLE_TAGTEMPLATES
            + " (" + COLUMN_ID + ")" +
            " FOREIGN KEY(" + COLUMN_EVENTS + ") REFERENCES " + TABLE_EVENTTAGS
            + " (" + COLUMN_TAG + ")" +
            ");";


    public static final String CREATE_EVENT_TABLE = "CREATE TABLE " +
            TABLE_EVENTS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_DATE + " INTEGER NOT NULL, " +
            COLUMN_TAGS + " INTEGER, " +
            " FOREIGN KEY(" + COLUMN_TAGS + ") REFERENCES " + TABLE_EVENTTAGS
            + " (" + COLUMN_EVENT + ")" +
            ");";
    public static final String CREATE_EVENTTAGS_TABLE = "CREATE TABLE " +
            TABLE_EVENTTAGS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_TAG + " INTEGER NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " (" + COLUMN_TAGS + ")" +
            " FOREIGN KEY(" + COLUMN_TAG + ") REFERENCES " + TABLE_TAGS
            + " (" + COLUMN_EVENT + ")" +
            ");";
    public static final String CREATE_MEAL_TABLE = "CREATE TABLE " +
            TABLE_MEALS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_PORTIONS + "REAL NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " (" + COLUMN_ID + ")" +
            ");";

    public static final String CREATE_OTHER_TABLE = "CREATE TABLE " +
            TABLE_OTHERS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " (" + COLUMN_ID + ")" +
            ");";

    public static final String CREATE_EXERCISE_TABLE = "CREATE TABLE " +
            TABLE_EXERCISES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_INTENSITY + "INTEGER NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " (" + COLUMN_ID + ")" +
            ");";

    public static final String CREATE_BM_TABLE = "CREATE TABLE " +
            TABLE_BMS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_COMPLETENESS + "INTEGER NOT NULL, " +
            COLUMN_BRISTOL + "INTEGER NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " (" + COLUMN_ID + ")" +
            ");";
    public static final String CREATE_RATING_TABLE = "CREATE TABLE " +
            TABLE_RATINGS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_AFTER + "INTEGER NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " (" + COLUMN_ID + ")" +
            ");";
    public static final String CREATE_EVENTS_TEMPLATE_TABLE = "CREATE TABLE " +
            TABLE_EVENTSTEMPLATES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTSTEMPLATEEVENTS
            + " (" + COLUMN_ID + ")" +
            ");";
    public static final String CREATE_EVENTS_TEMPLATE_TO_EVENT_TABLE = "CREATE TABLE " +
            TABLE_EVENTSTEMPLATEEVENTS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_EVENT + " INTEGER NOT NULL, " +
            COLUMN_EVENTSTEMPLATE + "INTEGER NOT NULL, " +
            " FOREIGN KEY(" + COLUMN_EVENT + ") REFERENCES " + TABLE_EVENTS
            + " (" + COLUMN_ID + ")" +
            " FOREIGN KEY(" + COLUMN_EVENTSTEMPLATE + ") REFERENCES " + TABLE_EVENTSTEMPLATES
            + " (" + COLUMN_ID + ")" +
            ");";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ENABLE_FOREIGN_KEYS);

        db.execSQL(CREATE_TAGTEMPLATE_TABLE);
        db.execSQL(CREATE_TAG_TABLE);

        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_EVENTTAGS_TABLE);

        db.execSQL(CREATE_MEAL_TABLE);
        db.execSQL(CREATE_OTHER_TABLE);
        db.execSQL(CREATE_EXERCISE_TABLE);
        db.execSQL(CREATE_BM_TABLE);
        db.execSQL(CREATE_RATING_TABLE);

        db.execSQL(CREATE_EVENTS_TEMPLATE_TABLE);
        db.execSQL(CREATE_EVENTS_TEMPLATE_TO_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTSTEMPLATEEVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTSTEMPLATES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTTAGS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGTEMPLATES);
        
        // Create tables again
        onCreate(db);
    }

    //===================================================================================
    //add, query and delete methods
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
        //Log.d("Debug", "addTagTemplate completed! TagTemplate " + tagTemplate.get_tagname() + " with id nr: " + findTagTemplate(tagTemplate.get_tagname()).get_id() + " inserted!");
    }

    //TODO
    public TagTemplate findTagTemplate(String tagName) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_TAGNAME + " = \"" + tagName + "\"";
        return findTagTemplateHelper(query);
    }

    public TagTemplate findTagTemplate(int anInt) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_ID + " = \"" + String.valueOf(anInt) + "\"";
        return findTagTemplateHelper(query);
    }

    private TagTemplate findTagTemplateHelper(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        TagTemplate tt = new TagTemplate();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            tt.set_id(cursor.getInt(0));
            tt.set_tagname(cursor.getString(1));
            TagTemplate parentTag;
            if (cursor.getString(2) == NO_INHERITANCE) {
                parentTag = null;
            } else {
                parentTag = findTagTemplate(cursor.getString(2));
            }

            //TagTemplate parentTag = cursor.getInt(2) == NO_INHERITANCE ? null : findTagTemplate(cursor.getInt(2)); //denna är fucked up
//            Log.d("Debug","childTag tagName = "+tt.get_tagname() + "parentTag tagName = "+parentTag.get_tagname());
            tt.set_is_a1(parentTag);
        }
        return tt;
    }

    //
    public String getTagname(int id) {
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_ID + " = \"" + id + "\"";
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


    //inspired by p. 557
    public List<TagTemplate> retrieveTagNames() {
        Log.d("Debug", "Do I ever come into retrieveTagNames???");
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<TagTemplate> tagTemplates = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            Log.d("Debug", "get position of cursor: " + cursor.getPosition());
            Log.d("Debug", "Do I ever come into while loop for cursor???");  //hit in kommer jag inte.
            while (!cursor.isAfterLast()) {
                Log.d("Debug", "Do I ever come into while loop for cursor???");  //hit in kommer jag inte.

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
        SQLiteDatabase db = this.getReadableDatabase();
        int doneDelete = db.delete(TABLE_TAGTEMPLATES, null, null);
        db.close();
        return doneDelete > 0;

    }

    public Cursor getCursorToAllElements() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT  * FROM " + TABLE_TAGTEMPLATES, null);
    }

    public void createSomeTagTemplates() {
        addTagTemplate(new TagTemplate("dairy"));
        addTagTemplate(new TagTemplate("yoghurt", findTagTemplate("dairy")));
        addTagTemplate(new TagTemplate("wheat"));
        addTagTemplate(new TagTemplate("pizza", findTagTemplate("wheat")));
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
}
