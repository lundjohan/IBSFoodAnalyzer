package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_JSON;
import static com.ibsanalyzer.constants.Constants.RETURN_OTHER_JSON;

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
        Other other = new Other(datetime, tagsList);
        Util.jsonAndMoreFinishingData(other,RETURN_OTHER_JSON, this);
        super.finish();
    }
}
