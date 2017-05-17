package com.ibsanalyzer.inputday;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.model.EventsTemplate;

import java.util.List;

import static android.R.attr.name;

/**
 * Created by Johan on 2017-05-17.
 */

public class EventsTemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<EventsTemplate>listOfTemplates;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eventstemplate, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder)holder;
        h.nameOfTemplate.setText(listOfTemplates.get(position).getNameOfTemplate());

    }

    @Override
    public int getItemCount() {
        return listOfTemplates.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameOfTemplate;
        public ViewHolder(View itemView) {
            super(itemView);
            nameOfTemplate = (TextView) itemView.findViewById(R.id.template_title);
        }
    }
}
