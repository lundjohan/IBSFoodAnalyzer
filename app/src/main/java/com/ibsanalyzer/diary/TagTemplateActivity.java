package com.ibsanalyzer.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.model.TagTemplate;
import com.ibsanalyzer.util.Util;

import java.io.Serializable;

import static com.ibsanalyzer.constants.Constants.NEW_TYPE_FOR_TAGTEMPLATE;
import static com.ibsanalyzer.constants.Constants.PUT_TAG_TEMPLATE;
import static com.ibsanalyzer.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.TAGNAME_SEARCH_STRING;
import static com.ibsanalyzer.constants.Constants.WHICH_TYPE;

public abstract class TagTemplateActivity extends AppCompatActivity implements View
        .OnClickListener {
    protected TagTemplate is_a = null;

    protected EditText name;
    protected EditText type_of;

    /**
     * a TagTemplate must be sent back because 1. TagAdderActivity must now which TagTemplate
     * has been chosen and 2. TagTemplateEdit/Adder-Activity must now which parent has been
     * created/ chosen.
     *
     * @param idOfTagTemplate <0 means new TagTemplate. If editing this is
     *                        the id.
     */
    protected void finishDoneClicked(long idOfTagTemplate) {
        //1. create a TagTemplate object from name, is_a
        TagTemplate tagTemplate = new TagTemplate(Util.makeFirstLetterCapitalAndRestSmall(name.getText().toString()), is_a);
        //2 update database
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        Intent data = new Intent();
        if (idOfTagTemplate >= 0) {
            dbHandler.editTagTemplate(tagTemplate, idOfTagTemplate);
        } else {
            dbHandler.addTagTemplate(tagTemplate);
            data.putExtra(PUT_TAG_TEMPLATE, (Serializable) tagTemplate);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    protected abstract void doneClicked();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                doneClicked();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_template_adder);
        name = (EditText) findViewById(R.id.name_box);
        type_of = (EditText) findViewById(R.id.is_a_type_of);
        Intent intent = getIntent();
        if (intent.hasExtra(TAGNAME_SEARCH_STRING)) {
            name.setText(intent.getStringExtra(TAGNAME_SEARCH_STRING));
        }
    }

    @Override
    public void onClick(View v) {
        startRecursiveIntent();
    }

    protected void startRecursiveIntent() {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, NEW_TYPE_FOR_TAGTEMPLATE);
    }

    //data coming back from TagAdderActivity for adding to a is_a_type_of
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode != NEW_TYPE_FOR_TAGTEMPLATE) {
            return;
        }
        int whichType = -1;
        if (data.hasExtra(WHICH_TYPE)) {
            whichType = data.getExtras().getInt(WHICH_TYPE);
        }
        TagTemplate tagTemplateChild = null;
        if (data.hasExtra(RETURN_TAG_TEMPLATE_SERIALIZABLE)) {
            tagTemplateChild = (TagTemplate) data.getExtras().getSerializable
                    (RETURN_TAG_TEMPLATE_SERIALIZABLE);
            is_a = tagTemplateChild;
            type_of.setText(is_a.get_tagname());
        }
    }
}
