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

import java.io.Serializable;

import static com.ibsanalyzer.constants.Constants.NEW_TYPE_FOR_TAGTEMPLATE;
import static com.ibsanalyzer.constants.Constants.PUT_TAG_TEMPLATE;
import static com.ibsanalyzer.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.TYPE_OF_1;
import static com.ibsanalyzer.constants.Constants.TYPE_OF_2;
import static com.ibsanalyzer.constants.Constants.TYPE_OF_3;
import static com.ibsanalyzer.constants.Constants.WHICH_TYPE;
import static com.ibsanalyzer.constants.Constants.WHICH_TYPE_OF;

public abstract class TagTemplateActivity extends AppCompatActivity implements View
        .OnClickListener {
    protected TagTemplate is_a_1 = null;
    protected TagTemplate is_a_2 = null;
    protected TagTemplate is_a_3 = null;

    protected EditText name;
    protected EditText type_of_1;
    protected EditText type_of_2;
    protected EditText type_of_3;

    /**
     * a TagTemplate must be sent back because 1. TagAdderActivity must now which TagTemplate
     * has been chosen and 2. TagTemplateEdit/Adder-Activity must now which parent has been
     * created/ chosen.
     *
     * @param idOfTagTemplate <0 means new TagTemplate. If editing this is
     *                        the id.
     */
    protected void finishDoneClicked(int idOfTagTemplate) {
        //1. create a TagTemplate object from name, is_a1, is_a_2, is_a_3.
        TagTemplate tagTemplate = new TagTemplate(name.getText().toString(), is_a_1, is_a_2,
                is_a_3);
        //2 update database
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        if (idOfTagTemplate >= 0) {
            dbHandler.editTagTemplate(tagTemplate, idOfTagTemplate);
        } else {
            dbHandler.addTagTemplate(tagTemplate);
        }
        Intent data = new Intent();
        data.putExtra(PUT_TAG_TEMPLATE, (Serializable) tagTemplate);
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
        type_of_1 = (EditText) findViewById(R.id.is_a_type_of_1);
        type_of_2 = (EditText) findViewById(R.id.is_a_type_of_2);
        type_of_3 = (EditText) findViewById(R.id.is_a_type_of_3);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.is_a_type_of_1:
                newTypeOf(TYPE_OF_1);
                break;
            case R.id.is_a_type_of_2:
                newTypeOf(TYPE_OF_2);
                break;
            case R.id.is_a_type_of_3:
                newTypeOf(TYPE_OF_3);
                break;
        }
    }

    protected void newTypeOf(int toPutInExtra) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        intent.putExtra(WHICH_TYPE_OF, toPutInExtra);
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

            switch (whichType) {
                case TYPE_OF_1:
                    is_a_1 = tagTemplateChild;
                    type_of_1.setText(is_a_1.get_tagname());
                    break;
                case TYPE_OF_2:
                    is_a_2 = tagTemplateChild;
                    type_of_2.setText(is_a_2.get_tagname());
                    break;
                case TYPE_OF_3:
                    is_a_3 = tagTemplateChild;
                    type_of_3.setText(is_a_3.get_tagname());
                    break;
            }
        }
    }
}
