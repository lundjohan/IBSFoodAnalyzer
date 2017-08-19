package com.ibsanalyzer.database;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import com.ibsanalyzer.inputday.R;

import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_ID;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_IS_A;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGNAME;

/**
 * Created by Johan on 2017-05-10.
 */

//based on https://github.com/codepath/android_guides/wiki/Populating-a-ListView-with-a-CursorAdapter
public class TagnameCursorAdapter extends CursorAdapter implements Filterable {


    public TagnameCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_tagtemplate_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tagName = (TextView) view.findViewById(R.id.tagname_stat);
        TextView inherits = (TextView) view.findViewById(R.id.inherits);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGNAME));
        String inheritanceOne = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IS_A));
        tagName.setText(name);
        if (inheritanceOne == null){
            inherits.setText("");
            return;
        }
        inherits.setText(inheritanceOne);

        //String inheritanceTwo = "";//cursor.getString(cursor.getColumnIndexOrThrow("priority"));
        //String inheritanceThree = "";//cursor.getString(cursor.getColumnIndexOrThrow("priority"));
        //TODO: there should be a maxlength of each one of them.
        //inherits.append(inheritanceOne + " " + inheritanceTwo + " " + inheritanceThree);

    }

    //temporarily, inefficient and bad solution. Prefer to use database instead of looping cursor
    private String getTagNameFromId(Cursor cursor, int id){
        cursor.moveToFirst();
        String tagName = "";
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))==id){
                tagName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAGNAME));
                break;
            }
            cursor.moveToNext();
        }
        return tagName;
    }

/*    @Override
    public Filter getFilter() {
        // return a filter that filters data based on a constraint

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }*/

}
