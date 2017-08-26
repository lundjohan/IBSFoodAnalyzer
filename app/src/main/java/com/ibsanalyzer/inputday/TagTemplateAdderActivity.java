package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.model.TagTemplate;

import java.io.Serializable;

import static com.ibsanalyzer.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;

/**
 * This Activity can be used to edit or create a new TagTemplate.
 * <p>
 * One alternative is to have to have a TagTemplateActivity as parent for a
 * TagTemplateEditActivity and a TagTemplateAddActivity
 */
public class TagTemplateAdderActivity extends TagTemplateActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void doneClicked() {
        super.finishDoneClicked(-1);

    }





}
