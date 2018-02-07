package com.ibsanalyzer.info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ibsanalyzer.diary.R;

import static com.ibsanalyzer.constants.Constants.INFO_STR;


public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView textView = (TextView)findViewById(R.id.infoTextView);
        String infoStr = getIntent().getStringExtra(INFO_STR);
        textView.setText(infoStr);
    }
}
