package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Tag;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.tag;
import static android.media.CamcorderProfile.get;

/**
 * Created by Johan on 2017-05-13.
 */

class TagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Tag>tagsList;;
    private Activity parentActivity;
    public TagAdapter(List<Tag> tagsList, MealActivity mealActivity) {
        this.tagsList = tagsList;
        parentActivity = mealActivity;
    }

    //s. 355
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        Tag t = tagsList.get(position);
        viewHolder.quantity.setText(t.getName());
        viewHolder.tagName.setText(Double.toString(t.getSize()));
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView quantity;
        public TextView tagName;

        public ViewHolder(View itemView) {
            super(itemView);
            quantity = (TextView) itemView.findViewById(R.id.tag_quantity);
            tagName = (TextView) itemView.findViewById(R.id.tagname);
        }
    }
}
