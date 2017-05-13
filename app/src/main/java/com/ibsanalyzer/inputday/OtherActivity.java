package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Other;

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
        Gson gson = new Gson();
        String otherAsJSON = gson.toJson(other);
        Intent data = new Intent();
        data.putExtra("returnOtherJSON", otherAsJSON);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
