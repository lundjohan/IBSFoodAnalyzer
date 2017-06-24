package com.ibsanalyzer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.inputday.R;

import java.util.ArrayList;
import java.util.List;

import com.ibsanalyzer.tagpoint_classes.TagPoint;

/**
 * Created by Johan on 2017-06-21.
 */

public class StatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TagPoint> tagPoints = new ArrayList<>();
    private int typeOfScore;

    public StatAdapter(List<TagPoint> tagPoints, int typeOfScore) {
        this.tagPoints = tagPoints;
        this.typeOfScore = typeOfScore;
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
        TagPoint tp = tagPoints.get(position);
        viewHolder.tagName.setText(tp.getName());
        viewHolder.quantity.setText(Double.toString(tp.getQuantity()));
        viewHolder.scoreField.setText(Double.toString(tp.getScore()));
    }

    @Override
    public int getItemCount() {
        return tagPoints.size();
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
