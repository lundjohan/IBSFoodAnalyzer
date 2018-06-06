package com.johanlund.screens.event_activities;

import android.os.Bundle;

import com.johanlund.base_classes.Other;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.event_activities.common.TagEventActivity;

import static com.johanlund.constants.Constants.OTHER;

public class OtherActivity extends TagEventActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_other;
    }

    @Override
    protected void buildEvent() {
        //create event
        Other other = new Other(getLocalDateTime(), getComment(), tagsList);
        returnEvent(other);
    }

    @Override
    protected String getTitleStr() {
        return "New Other";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String getTextForAddTagsBtn() {
        return "Add Other Components";
    }

    @Override
    protected int getEventType() {
        return OTHER;
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.info_other;
    }

}
