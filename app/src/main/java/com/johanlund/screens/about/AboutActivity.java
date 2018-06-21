package com.johanlund.screens.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.johanlund.ibsfoodanalyzer.R;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("About");
    }
}
