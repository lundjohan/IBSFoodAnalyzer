package com.johanlund.screens.statistics.avg_stat.common;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.statistics.common.StatAsyncTask;
import com.johanlund.screens.statistics.common.StatBaseActivity;
import com.johanlund.util.TagsWrapperBase;

import java.util.List;

public abstract class AvgStatActivity extends StatBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_stat);
        recyclerView = (RecyclerView) findViewById(R.id.avg_stat_table);
        continueOnCreate();
    }

    protected void calculateStats() {
        List<TagsWrapperBase> twbs = getTagsWrapperBase();
        asyncThread = new StatAsyncTask(this, adapter, recyclerView);
        asyncThread.execute(getScoreWrapper(), twbs);
    }

    protected abstract List<TagsWrapperBase> getTagsWrapperBase();
}