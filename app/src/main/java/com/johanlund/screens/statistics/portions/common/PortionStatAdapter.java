package com.johanlund.screens.statistics.portions.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.statistics.common.StatAdapter;
import com.johanlund.stat_backend.point_classes.PortionPoint;
import com.johanlund.stat_backend.portion_scorewrapper.PortionScoreWrapper;

/**
 * Created by Johan on 2018-04-06.
 */

public class PortionStatAdapter extends StatAdapter<PortionPoint> {   //list needs to be
    // initiated, otherwise getItemCount crashes.
    protected PortionScoreWrapper portionScoreWrapper;

    public PortionStatAdapter(PortionScoreWrapper portionScoreWrapper) {
        this.portionScoreWrapper = portionScoreWrapper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stat, parent,
                false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        PortionPoint tp = pointList.get(position);
        viewHolder.interval.setText(tp.getRange().toString());
        viewHolder.scoreField.setText(String.format("%.1f", tp.getScore()));
        viewHolder.quant.setText(String.format("%.1f", tp.getQuant()));

    }

    @Override
    public int getItemCount() {
        return pointList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView interval;
        public TextView scoreField;
        public TextView quant;

        public ViewHolder(View itemView) {
            super(itemView);
            interval = (TextView) itemView.findViewById(R.id.tagname_stat);
            scoreField = (TextView) itemView.findViewById(R.id.score_view);
            quant = (TextView) itemView.findViewById(R.id.quantity_stat);
        }
    }
}