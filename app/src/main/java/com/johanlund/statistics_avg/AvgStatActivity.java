package com.johanlund.statistics_avg;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.johanlund.base_classes.Break;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_general.StatAsyncTask;
import com.johanlund.statistics_general.StatBaseActivity;
import com.johanlund.util.ScoreTimesBase;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

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
        StatAsyncTask asyncThread = new StatAsyncTask(adapter, recyclerView);
        asyncThread.execute(getScoreWrapper(), twbs);
    }

    protected abstract List<TagsWrapperBase> getTagsWrapperBase();
}