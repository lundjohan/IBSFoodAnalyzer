package com.johanlund.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.TITLE_STRING;

public class ActivityInfoContent extends AppCompatActivity {

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
