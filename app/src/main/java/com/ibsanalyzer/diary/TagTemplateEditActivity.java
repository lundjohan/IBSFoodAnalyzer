package com.ibsanalyzer.diary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.ibsanalyzer.model.TagTemplate;

import static com.ibsanalyzer.constants.Constants.TAGTEMPLATE_ID;
import static com.ibsanalyzer.constants.Constants.TAG_TEMPLATE_ID;
import static com.ibsanalyzer.constants.Constants.TAG_TEMPLATE_TO_EDIT;

public class TagTemplateEditActivity extends TagTemplateActivity {
    //this variable is used to know which TagTemplate in database that should be edited (as name
    // could have been change there is no other way of knowing original TagTemplate otherwise)
    long idOfTagTemplate = -1;
    TagTemplate tt;

    /*
    Should probably have try/ catch clauses instead of if, else. (What if a TagTemplateID is invalid for example?)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(TAG_TEMPLATE_ID)) {
            idOfTagTemplate = intent.getLongExtra(TAG_TEMPLATE_ID, -1);
        }
        else{
            throw new IllegalArgumentException("TagTemplateEditActivity needs to be started with a valid TAG_TEMPLATE_ID");
        }
        if (intent.hasExtra(TAG_TEMPLATE_TO_EDIT)) {
            tt = (TagTemplate)intent.getSerializableExtra(TAG_TEMPLATE_TO_EDIT);
        }
        else{
            throw new IllegalArgumentException("TagTemplateEditActivity needs to be started with a valid TagTemplate");
        }

        //fill in form with data from tt
        name.setText(tt.get_tagname());
        if (tt.get_is_a1()!= null) {
            type_of_1.setText(tt.get_is_a1().get_tagname());
        }
        if (tt.get_is_a2()!= null) {
            type_of_2.setText(tt.get_is_a2().get_tagname());
        }
            if (tt.get_is_a3()!= null) {
                type_of_3.setText(tt.get_is_a3().get_tagname());
            }


    }

    public void doneClicked() {
        super.finishDoneClicked(idOfTagTemplate);
        finish();
    }
}
