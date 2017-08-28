package com.ibsanalyzer.adapters;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.inputday.R;
import com.ibsanalyzer.inputday.TagEventActivity;

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
        viewHolder.threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(parentActivity, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                     @Override
                                                     public boolean onMenuItemClick(MenuItem item) {
                                                         switch (item.getItemId()) {
                                                             case R.id.edit_tag:
                                                                 parentActivity
                                                                         .onTagItemEditClicked(
                                                                                 position);
                                                                 return true;
                                                             case R.id.delete_tag:
                                                                 parentActivity
                                                                         .onTagItemDeleteClicked
                                                                                 (position);
                                                                 return true;
                                                             default:
                                                                 return false;
                                                         }
                                                     }
                                                 }
                );
                popup.inflate(R.menu.tag_item_menu);
                popup.show();
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
        public ImageView threeDots;

        public ViewHolder(View itemView) {
            super(itemView);
            quantity = (TextView) itemView.findViewById(R.id.tag_quantity);
            tagName = (TextView) itemView.findViewById(R.id.stattagname);
            threeDots = (ImageView) itemView.findViewById(R.id.threeDotsMenu);
        }
    }
}
