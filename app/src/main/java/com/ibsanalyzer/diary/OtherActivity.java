package com.ibsanalyzer.diary;

import android.os.Bundle;

import com.ibsanalyzer.base_classes.Other;

import static com.ibsanalyzer.constants.Constants.OTHER;

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
    protected int getEventType() {
        return OTHER;
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.activity_meal_info;
    }

}
