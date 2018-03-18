package com.johanlund.statistics_time;

import com.johanlund.adapters.AvgStatAdapter;
import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics.StatBaseActivity;

import java.util.List;

/**
 * Created by Johan on 2018-03-17.
 */

public abstract class TimeStatActivity extends StatBaseActivity{

    @Override
    public AvgStatAdapter getStatAdapter() {
        return null;
    }


    @Override
    protected void startAsyncTask(List<Chunk> chunks) {

    }
}
