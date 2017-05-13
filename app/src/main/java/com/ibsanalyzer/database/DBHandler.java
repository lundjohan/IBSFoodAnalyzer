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
    public static final String TABLE_TAGTEMPLATES = "tag_templates";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TAGNAME = "_tagname"; //this should be unique
    public static final String COLUMN_IS_A = "_is_a1";      //make it point to parent TagName and not to id, it make it possible to display in listview after filtering.

    // see https://sqlite.org/foreignkeys.html for creation of foreign keys.
    public static final String CREATE_TAGTEMPLATE_TABLE = "CREATE TABLE " +
            TABLE_TAGTEMPLATES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_TAGNAME + " TEXT NOT NULL UNIQUE, " +
            COLUMN_IS_A + " TEXT CHECK("+COLUMN_IS_A+" != "+COLUMN_TAGNAME+"),  " +
            " FOREIGN KEY(" + COLUMN_IS_A + ") REFERENCES " + TABLE_TAGTEMPLATES
            + " (" + COLUMN_TAGNAME + ")" +
            ");";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TAGTEMPLATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  // Drop older table if existed
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
        String query = "SELECT * FROM " + TABLE_TAGTEMPLATES + " WHERE " + COLUMN_ID + " = \"" +String.valueOf(anInt) + "\"";
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
            if (cursor.getString(2)== NO_INHERITANCE){
                parentTag = null;
            }
            else{
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
