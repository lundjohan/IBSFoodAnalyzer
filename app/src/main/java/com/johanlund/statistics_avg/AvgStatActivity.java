package com.johanlund.statistics_avg;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_general.StatBaseActivity;

public abstract class AvgStatActivity extends StatBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_stat);
        recyclerView = (RecyclerView) findViewById(R.id.avg_stat_table);
        continueOnCreate();
    }
}