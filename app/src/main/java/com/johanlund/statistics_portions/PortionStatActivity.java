package com.johanlund.statistics_portions;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_adapters.PortionStatAdapter;
import com.johanlund.statistics_general.StatAdapter;
import com.johanlund.statistics_general.StatBaseActivity;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_portion_scorewrapper.PortionScoreWrapper;

public abstract class PortionStatActivity extends StatBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portion_stat);
        recyclerView = (RecyclerView) findViewById(R.id.avg_portion_table);
        continueOnCreate();
    }

    @Override
    protected String getInfoStr() {
        return "";
    }

    @Override
    public StatAdapter<PortionPoint> getStatAdapter() {
        return new PortionStatAdapter(getScoreWrapper());
    }

    public abstract PortionScoreWrapper getScoreWrapper();
}
