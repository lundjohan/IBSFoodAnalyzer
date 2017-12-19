package com.ibsanalyzer.database;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibsanalyzer.diary.R;

import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_ID;
import static com.ibsanalyzer.database.TablesAndStrings.COLUMN_TAGNAME;
import static com.ibsanalyzer.database.TablesAndStrings.FIRST_COLUMN_IS_A;

/**
 * Created by Johan on 2017-05-10.
 */

//based on https://github.com/codepath/android_guides/wiki/Populating-a-ListView-with-a
// -CursorAdapter
public class TagnameCursorAdapter extends CursorAdapter implements Filterable {
    ChangingTagTemplate changingTagTemplate;
    public interface ChangingTagTemplate {
        void editTagTemplate(long tagTemplateId);
        void delTagTemplate(long tagTemplateId);
    }

    public TagnameCursorAdapter(ChangingTagTemplate changingTagTemplate, Cursor c) {
        super((AppCompatActivity)changingTagTemplate, c, 0);
        this.changingTagTemplate = changingTagTemplate;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_tagtemplate_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor c) {
        TextView tagName = (TextView) view.findViewById(R.id.name_of_tag);
        TextView inherits = (TextView) view.findViewById(R.id.inherits);
        ImageView threeDotsBtn = (ImageView) view.findViewById(R.id.three_dots_inside_listView);

        threeDotsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.delete_edit_tagtemplate_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.edit_tagtemplate) {
                            changingTagTemplate.editTagTemplate(c.getInt(c.getColumnIndex(COLUMN_ID)));
                        }
                        else if (item.getItemId() == R.id.del_tagtemplate){
                            changingTagTemplate.delTagTemplate(c.getInt(c.getColumnIndex(COLUMN_ID)));;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        String name = c.getString(c.getColumnIndexOrThrow(COLUMN_TAGNAME));
        String inheritanceOne = c.getString(c.getColumnIndexOrThrow(FIRST_COLUMN_IS_A));
        tagName.setText(name);
        if (inheritanceOne == null) {
            inherits.setText("");
            return;
        }
        inherits.setText(inheritanceOne);

        //String inheritanceTwo = "";//cursor.getString(cursor.getColumnIndexOrThrow("priority"));
        //String inheritanceThree = "";//cursor.getString(cursor.getColumnIndexOrThrow("priority"));
        //TODO: there should be a maxlength of each one of them.
        //inherits.append(inheritanceOne + " " + inheritanceTwo + " " + inheritanceThree);

    }
}
