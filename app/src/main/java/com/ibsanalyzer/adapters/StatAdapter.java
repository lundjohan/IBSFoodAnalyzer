package com.ibsanalyzer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.calc_score_classes.ScoreWrapper;
import com.ibsanalyzer.inputday.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibsanalyzer.tagpoint_classes.TagPoint;

/**
 * Created by Johan on 2017-06-21.
 */

public class StatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Map<String, TagPoint> tagPoints = new HashMap<>();
    private ScoreWrapper scoreWrapper;

    public StatAdapter(Map<String, TagPoint> tagPoints) {
        this.tagPoints = tagPoints;
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
        TagPoint tp = tagPoints.get(position);
        if (tp!=null) {
            viewHolder.tagName.setText(tp.getName());
            viewHolder.quantity.setText(Double.toString(tp.getQuantity()));
            viewHolder.scoreField.setText(Double.toString(scoreWrapper.getScore(tp)));
        }
    }

    @Override
    public int getItemCount() {
        return tagPoints.size();
    }

    public void setScoreWrapper(ScoreWrapper scoreWrapper) {
        this.scoreWrapper = scoreWrapper;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
