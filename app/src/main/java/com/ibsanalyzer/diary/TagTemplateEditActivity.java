package com.ibsanalyzer.diary;

import android.content.Intent;
import android.os.Bundle;

import static com.ibsanalyzer.constants.Constants.TAGTEMPLATE_ID;

public class TagTemplateEditActivity extends TagTemplateActivity {
    //this variable is used to know which TagTemplate in database that should be edited (as name
    // could have been change there is no other way of knowing original TagTemplate otherwise)
    int idOfTagTemplate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(TAGTEMPLATE_ID)) {
            idOfTagTemplate = intent.getIntExtra(TAGTEMPLATE_ID, -1);
        }
    }

    public void doneClicked() {
        super.finishDoneClicked(idOfTagTemplate);
    }
}
