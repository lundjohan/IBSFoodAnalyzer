package com.johanlund.statistics_adapters;

import android.support.v7.widget.RecyclerView;

import com.johanlund.statistics_adapters.AvgStatAdapter;
import com.johanlund.statistics_avg_scorewrapper.AvgScoreWrapper;
import com.johanlund.statistics_point_classes.TagPoint;

import java.util.List;

/**
 * Created by Johan on 2018-02-13.
 */

public class BmAvgStatAdapter extends AvgStatAdapter {
    public BmAvgStatAdapter(AvgScoreWrapper avgScoreWrapper) {
        super(avgScoreWrapper);
    }
    //this is the essential one
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TagPoint tp = pointList.get(position);
        viewHolder.tagName.setText(tp.getName());
        viewHolder.scoreField.setText(String.format("%.1f", avgScoreWrapper.getScore(tp)));

        //this differs from rating stat
        viewHolder.quantity.setText(Integer.toString(tp.getNrOfBMs()));

    }
}
