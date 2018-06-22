package com.johanlund.screens.event_activities.mvc_controllers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.johanlund.model.TagType;
import com.johanlund.screens.tag_adder.TagAdderActivity;

import java.util.List;

import static com.johanlund.constants.Constants.IDS_OF_EDITED_TAG_TEMPLATES;
import static com.johanlund.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.johanlund.constants.Constants.TAGS_TO_ADD;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED;

public abstract class TagEventActivity extends EventActivity {
    /*
   ================================================================================================
   TAG RELATED
   ================================================================================================

    */
    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, TAGS_TO_ADD);
    }

    //data coming back from TagAdder
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode != TAGS_TO_ADD) {
            return;
        }
        if (data.hasExtra(TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED)) {
            long[] editedTagTemplatesIds = null;
            if (data.hasExtra(IDS_OF_EDITED_TAG_TEMPLATES)) {
                editedTagTemplatesIds = data.getLongArrayExtra(IDS_OF_EDITED_TAG_TEMPLATES);
            }
            updateTags(editedTagTemplatesIds);
        }
        if (data.hasExtra(RETURN_TAG_TEMPLATE_SERIALIZABLE)) {
            TagType tagType = (TagType) data.getExtras().getSerializable
                    (RETURN_TAG_TEMPLATE_SERIALIZABLE);
            mViewMVC.bindAddedTagToView(tagType.get_tagname());
        }
    }

    /**
     * is called after TagType(s) has been deleted or edited.
     * This function should be as simple as possible,
     * simply match this Activity's TagList and TagType in database,
     * if no match => remove from tagList (it is not so terrible if too many lines become removed)
     */
    private void updateTags(long[] editedTagTemplatesIds) {
        if (editedTagTemplatesIds != null && editedTagTemplatesIds.length > 0) {
            addEditedTags(editedTagTemplatesIds);
        }
        delTagsWithNamesThatHasNoOccuranceInModel();
    }

    private void delTagsWithNamesThatHasNoOccuranceInModel() {
        List<String> tagNames = mViewMVC.getTagNames();
        for (String tagName : tagNames) {
            if (dao.tagTemplateDoesntExist(tagName)) {
                mViewMVC.removeTagFromView(tagName);
            }
        }
    }

    /**
     * @param editedTagTemplatesIds is not null and not of length 0.
     */
    private void addEditedTags(long[] editedTagTemplatesIds) {
        for (long idOfTagTemplate : editedTagTemplatesIds) {
            String tagName = dao.retrieveNameOfTagTemplate(idOfTagTemplate);
            mViewMVC.bindAddedTagToView(tagName);
        }
    }
}
