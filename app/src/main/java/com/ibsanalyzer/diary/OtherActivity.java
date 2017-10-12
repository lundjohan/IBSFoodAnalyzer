package com.ibsanalyzer.diary;

import android.os.Bundle;

import com.ibsanalyzer.base_classes.Other;

import static com.ibsanalyzer.constants.Constants.RETURN_OTHER_SERIALIZABLE;

public class OtherActivity extends TagEventActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_other;
    }

    @Override
    protected void buildEvent() {
        //create event
        Other other = new Other(getLocalDateTime(), tagsList);
        returnEvent(other, RETURN_OTHER_SERIALIZABLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}