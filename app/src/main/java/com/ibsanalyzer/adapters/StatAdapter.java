package com.ibsanalyzer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.inputday.R;

import java.util.ArrayList;
import java.util.List;

import stat_classes.TagPoint;

import static com.ibsanalyzer.constants.Constants.AVG_SCORE;
import static com.ibsanalyzer.constants.Constants.BLUE_ZONE_SCORE;
import static com.ibsanalyzer.constants.Constants.BRISTOL_SCORE;
import static com.ibsanalyzer.constants.Constants.COMPLETENESS_SCORE;

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
        if (typeOfScore == AVG_SCORE) {
            viewHolder.scoreField.setText(Double.toString(tp.getAdjustedAvgScore()));
        } else if (typeOfScore == BLUE_ZONE_SCORE) {
            viewHolder.scoreField.setText(Double.toString(tp.getBlueZonesQuant()));
        } else if (typeOfScore == COMPLETENESS_SCORE) {
            viewHolder.scoreField.setText(Double.toString(tp.getCompleteAvg()));
        } else if (typeOfScore == BRISTOL_SCORE) {
            viewHolder.scoreField.setText(Double.toString(tp.getAvgBristol()));
        }

    }

    public int getTypeOfScore() {
        return typeOfScore;
    }

    @Override
    public int getItemCount() {
        return tagPoints.size();
    }

    public void setTypeOfScore(int typeOfScore) {
        this.typeOfScore = typeOfScore;
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
