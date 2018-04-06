package com.johanlund.statistics_portions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.johanlund.base_classes.Chunk;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_adapters.TimeStatAdapter;
import com.johanlund.statistics_general.StatAdapter;
import com.johanlund.statistics_general.StatBaseActivity;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.statistics_portion_scorewrapper.PortionScoreWrapper;

import java.util.List;

public abstract class PortionStatActivity extends StatBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portion_stat);
    }

    @Override
    protected String getInfoStr() {
        return "";
    }

    @Override
    public StatAdapter<PortionPoint> getStatAdapter() {
        return null;
    }
}
