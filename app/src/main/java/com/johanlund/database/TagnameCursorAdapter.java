package com.johanlund.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.johanlund.ibsfoodanalyzer.R;

import static com.johanlund.database.TablesAndStrings.COLUMN_TAGNAME;
import static com.johanlund.database.TablesAndStrings.TYPE_OF;

/**
 * Created by Johan on 2017-05-10.
 */

//based on https://github.com/codepath/android_guides/wiki/Populating-a-ListView-with-a
// -CursorAdapter
public class TagnameCursorAdapter extends CursorAdapter implements Filterable {
    ChangingTagTemplate changingTagTemplate;
    Context mainContext;

    public interface ChangingTagTemplate {
        void editTagTemplate(long tagTemplateId);

        void delTagTemplate(long tagTemplateId);
    }

    public TagnameCursorAdapter(ChangingTagTemplate changingTagTemplate, Cursor c) {
        super((AppCompatActivity) changingTagTemplate, c, 0);
        this.changingTagTemplate = changingTagTemplate;
        this.mainContext = ((Activity) changingTagTemplate).getApplicationContext();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_tagtemplate_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor c) {
        final TextView tagName = (TextView) view.findViewById(R.id.name_of_stat_option);
        TextView inherits = (TextView) view.findViewById(R.id.inherits);
        ImageView threeDotsBtn = (ImageView) view.findViewById(R.id.settings_btn_inside_listView);

        threeDotsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.delete_edit_tagtemplate_menu, popup
                        .getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        /*Ugly solution of using the TagName to get the id of TagTemplate.
                        But the Cursor c is always in last position here (I guess it is not meant
                         to be used in a onClickEvent that is called after the first bindView),
                         so it can't be used (it would of course have been better to retrieve the
                          id directly instead of using the name of the TagName)*/
                        String tagNameStr = (String) tagName.getText();
                        DBHandler dbHandler = new DBHandler(mainContext);
                        long tagTemplateId = dbHandler.getTagTemplateId(tagNameStr);
                        if (item.getItemId() == R.id.edit_tagtemplate) {
                            changingTagTemplate.editTagTemplate(tagTemplateId);
                        } else if (item.getItemId() == R.id.del_tagtemplate) {
                            changingTagTemplate.delTagTemplate(tagTemplateId);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        String name = c.getString(c.getColumnIndexOrThrow(COLUMN_TAGNAME));
        Long inheritanceId = c.getLong(c.getColumnIndexOrThrow(TYPE_OF));
        DBHandler dbHandler = new DBHandler(mainContext);
        String parentName = dbHandler.getTagTemplateName(inheritanceId);
        tagName.setText(name);
        if (parentName == null) {
            inherits.setText("");
            return;
        }
        inherits.setText(parentName);

        //String inheritanceTwo = "";//cursor.getString(cursor.getColumnIndexOrThrow("priority"));
        //String inheritanceThree = "";//cursor.getString(cursor.getColumnIndexOrThrow("priority"));
        //TODO: there should be a maxlength of each one of them.
        //inherits.append(parentName + " " + inheritanceTwo + " " + inheritanceThree);

    }
}
