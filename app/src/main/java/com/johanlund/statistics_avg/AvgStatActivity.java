package com.johanlund.statistics_avg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.johanlund.statistics_adapters.AvgStatAdapter;
import com.johanlund.base_classes.Chunk;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_general.StatBaseActivity;
import com.johanlund.statistics_avg_scorewrapper.AvgScoreWrapper;
import com.johanlund.statistics_point_classes.TagPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AvgStatActivity extends StatBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_stat);
        recyclerView = (RecyclerView) findViewById(R.id.avg_stat_table);
        continueOnCreate();
    }
}