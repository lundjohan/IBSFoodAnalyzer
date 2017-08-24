package com.ibsanalyzer.inputday;

import android.os.Bundle;

import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_OTHER_SERIALIZABLE;

public class OtherActivity extends TagEventActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_other;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void finish() {
        //create event
        Other other = new Other(getLocalDateTime(), tagsList);
        returnEvent(other, RETURN_OTHER_SERIALIZABLE);
    }
}
