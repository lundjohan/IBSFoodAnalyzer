package com.johanlund.screens.statistics.avg_stat.common;

import android.support.v7.widget.RecyclerView;

import com.johanlund.screens.statistics.avg_stat.common.AvgStatAdapter;
import com.johanlund.stat_backend.avg_scorewrapper.AvgScoreWrapper;
import com.johanlund.stat_backend.point_classes.TagPoint;

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
