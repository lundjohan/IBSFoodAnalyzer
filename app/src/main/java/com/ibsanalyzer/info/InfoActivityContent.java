package com.ibsanalyzer.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static com.ibsanalyzer.constants.Constants.LAYOUT_RESOURCE;

public class InfoActivityContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int resource = intent.getIntExtra(LAYOUT_RESOURCE, 0);
        setContentView(resource);
    }
}
