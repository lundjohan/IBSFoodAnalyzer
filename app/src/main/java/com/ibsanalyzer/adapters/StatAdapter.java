package com.ibsanalyzer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.calc_score_classes.ScoreWrapper;
import com.ibsanalyzer.inputday.R;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2017-06-21.
 * <p>
 * tagPoints should perhaps be an ArrayList instead of a map.
 * A bit surprising that it goes so fast, even though onBindListener calls HashMap.values => is
 * this equivalent of a get, or is a list created every time?
 */

public class StatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Map<String, TagPoint> tagPoints = new HashMap<>();
    private ScoreWrapper scoreWrapper;
    private int typeOfScore;

    public StatAdapter(Map<String, TagPoint> tagPoints) {
        this.tagPoints = tagPoints;
    }


    public int getTypeOfScore() {
        return typeOfScore;
    }

    public void setTypeOfScore(int typeOfScore) {
        this.typeOfScore = typeOfScore;
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
        List<TagPoint> tagPointsList = new ArrayList<TagPoint>(tagPoints.values());
        TagPoint tp = tagPointsList.get(position);
        viewHolder.tagName.setText(tp.getName());
        viewHolder.scoreField.setText(String.format("%.1f", scoreWrapper.getScore(tp)));
        viewHolder.quantity.setText(String.format("%.1f", tp.getQuantity()));

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
