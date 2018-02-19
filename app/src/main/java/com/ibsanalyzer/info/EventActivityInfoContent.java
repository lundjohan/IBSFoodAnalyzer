package com.ibsanalyzer.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static com.ibsanalyzer.constants.Constants.LAYOUT_RESOURCE;
import static com.ibsanalyzer.constants.Constants.TITLE_STRING;

public class EventActivityInfoContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int resource = intent.getIntExtra(LAYOUT_RESOURCE, 0);
        String halfTitle = intent.getStringExtra(TITLE_STRING);
        setContentView(resource);
        setTitle(halfTitle + " Info");
    }
}
