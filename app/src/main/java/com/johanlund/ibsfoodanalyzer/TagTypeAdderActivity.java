package com.johanlund.ibsfoodanalyzer;

import android.os.Bundle;

/**
 * This Activity can be used to edit or create a new TagTemplate.
 * <p>
 * One alternative is to have to have a TagTypeActivity as parent for a
 * TagTypeEditActivity and a TagTemplateAddActivity
 */
public class TagTypeAdderActivity extends TagTypeActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected String getTitleStr() {
        return "Add Tag Type";
    }

    public void doneClicked() {
        super.finishDoneClicked(-1);
        finish();

    }


}
