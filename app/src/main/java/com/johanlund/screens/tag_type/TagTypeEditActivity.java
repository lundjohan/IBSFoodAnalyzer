package com.johanlund.screens.tag_type;

import android.content.Intent;
import android.os.Bundle;

import com.johanlund.database.DBHandler;
import com.johanlund.model.TagType;
import com.johanlund.util.Util;

import static com.johanlund.constants.Constants.IDS_OF_EARLIER_EDITED_TAG_TEMPLATES;
import static com.johanlund.constants.Constants.IDS_OF_EDITED_TAG_TEMPLATES;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_ID;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_TO_EDIT;

public class TagTypeEditActivity extends TagTypeActivity {
    //this variable is used to know which TagType in database that should be edited (as name
    // could have been change there is no other way of knowing original TagType otherwise)
    long idOfTagTemplate = -1;
    TagType tt;
    //this is stored
    private long[] idsFromEarlierEditedTagTemplates;

    /*
    Should probably have try/ catch clauses instead of if, else. (What if a TagTemplateID is
    invalid for example?)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(TAG_TEMPLATE_ID)) {
            idOfTagTemplate = intent.getLongExtra(TAG_TEMPLATE_ID, -1);
        } else {
            throw new IllegalArgumentException("TagTypeEditActivity needs to be started with a " +
                    "valid TAG_TEMPLATE_ID");
        }
        if (intent.hasExtra(TAG_TEMPLATE_TO_EDIT)) {
            tt = (TagType) intent.getSerializableExtra(TAG_TEMPLATE_TO_EDIT);
        } else {
            throw new IllegalArgumentException("TagTypeEditActivity needs to be started with a " +
                    "valid TagType");
        }
        if (intent.hasExtra((IDS_OF_EARLIER_EDITED_TAG_TEMPLATES))) {
            idsFromEarlierEditedTagTemplates = intent.getLongArrayExtra
                    (IDS_OF_EARLIER_EDITED_TAG_TEMPLATES);
        }

        //fill in form with data from tt
        name.setText(tt.get_tagname());
        if (tt.get_type_of() != null) {
            type_of.setText(tt.get_type_of().get_tagname());
        }
    }

    @Override
    protected String getTitleStr() {
        return "Edit Tag Type";
    }

    public void doneClicked() {
        //1. create a TagType object from name, is_a
        TagType tagType = new TagType(name.getText().toString(), is_a);
        //2 update database
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        Intent data = new Intent();
        if (idOfTagTemplate >= 0) {
            dbHandler.editTagTemplate(tagType, idOfTagTemplate);
            idsFromEarlierEditedTagTemplates = Util.appendToArray
                    (idsFromEarlierEditedTagTemplates, idOfTagTemplate);
            data.putExtra(IDS_OF_EDITED_TAG_TEMPLATES, idsFromEarlierEditedTagTemplates);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
