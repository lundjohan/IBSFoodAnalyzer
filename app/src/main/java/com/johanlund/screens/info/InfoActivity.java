package com.johanlund.screens.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.johanlund.ibsfoodanalyzer.R;

import static com.johanlund.constants.Constants.INFO_STR;


public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        TextView textView = (TextView)findViewById(R.id.infoTextView);
        String infoStr = getIntent().getStringExtra(INFO_STR);
        textView.setText(infoStr);
    }
    public static void newInfoActivity(AppCompatActivity activity, String str){
        Intent intent = new Intent(activity, InfoActivity.class);
        intent.putExtra(INFO_STR, str);
        activity.startActivity(intent);
    }
}
