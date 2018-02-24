package com.johanlund.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johanlund.calc_score_classes.ScoreWrapper;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.tagpoint_classes.TagPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2017-06-21.
 * <p>
 * tagPoints should perhaps be an ArrayList instead of a map.
 * A bit surprising that it goes so fast, even though onBindListener calls HashMap.values => is
 * this equivalent of a get, or is a list created every time?
 */

public class StatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //list needs to be initiated, otherwise getItemCount crashes.
    protected List<TagPoint>tagPointsList = new ArrayList<>();
    protected ScoreWrapper scoreWrapper;

    public StatAdapter(ScoreWrapper scoreWrapper) {
        this.scoreWrapper = scoreWrapper;
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
        TagPoint tp = tagPointsList.get(position);
        viewHolder.tagName.setText(tp.getName());
        viewHolder.scoreField.setText(String.format("%.1f", scoreWrapper.getScore(tp)));
        viewHolder.quantity.setText(String.format("%.1f", tp.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return tagPointsList.size();
    }

    public void setTagPointsList(List<TagPoint>tagPointsList){
        this.tagPointsList = tagPointsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagName;
        public TextView scoreField;
        public TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            tagName = (TextView) itemView.findViewById(R.id.tagname_stat);
            scoreField = (TextView) itemView.findViewById(R.id.score_view);
            quantity = (TextView) itemView.findViewById(R.id.quantity_stat);

        }
    }
}
