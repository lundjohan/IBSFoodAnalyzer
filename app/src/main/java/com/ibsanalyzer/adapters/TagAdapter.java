package com.ibsanalyzer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.diary.TagEventActivity;

import java.util.List;

/**
 * Created by Johan on 2017-05-13.
 */

public class TagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Tag> tagsList;
    private TagEventActivity parentActivity;

    public TagAdapter(List<Tag> tagsList, TagEventActivity tagEventActivity) {
        this.tagsList = tagsList;
        parentActivity = tagEventActivity;
    }

    //s. 355
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Tag t = tagsList.get(position);
        viewHolder.quantity.setText(Double.toString(t.getSize()));
        viewHolder.tagName.setText(t.getName());
        viewHolder.deleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.onTagItemDeleteClicked(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView quantity;
        public TextView tagName;
        public ImageView deleteTag;

        public ViewHolder(View itemView) {
            super(itemView);
            quantity = (TextView) itemView.findViewById(R.id.tag_quantity);
            tagName = (TextView) itemView.findViewById(R.id.stattagname);
            deleteTag = (ImageView) itemView.findViewById(R.id.deleteTag);
        }
    }
}
