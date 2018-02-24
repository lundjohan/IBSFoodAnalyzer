package com.johanlund.adapters;

import android.support.v7.widget.RecyclerView;

import com.johanlund.calc_score_classes.ScoreWrapper;
import com.johanlund.tagpoint_classes.TagPoint;

/**
 * Created by Johan on 2018-02-13.
 */

public class BmStatAdapter extends StatAdapter {
    public BmStatAdapter(ScoreWrapper scoreWrapper) {
        super(scoreWrapper);
    }
    //this is the essential one
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TagPoint tp = tagPointsList.get(position);
        viewHolder.tagName.setText(tp.getName());
        viewHolder.scoreField.setText(String.format("%.1f", scoreWrapper.getScore(tp)));

        //this differs from rating stat
        viewHolder.quantity.setText(Integer.toString(tp.getNrOfBMs()));

    }
}
