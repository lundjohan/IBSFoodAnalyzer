package com.ibsanalyzer.inputday;

import android.os.Bundle;

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
        finish();

    }


}
