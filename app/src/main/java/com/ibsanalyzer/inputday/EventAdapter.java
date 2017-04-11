package com.ibsanalyzer.inputday;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ibsanalyzer.base_classes.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2017-04-10.
 */

class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tag1;
        public TextView tag2;

        public ViewHolder(View itemView){
            super(itemView);
            tag1 = (TextView)itemView.findViewById(R.id.tag1);
            tag2 = (TextView)itemView.findViewById(R.id.tag2);
        }
    }


    private List<Event> events = new ArrayList<>();

    //Wait to use this one, perhaps should be in other place...
    private int [] images = {R.drawable.meal};
    public EventAdapter (List<Event>events){
        this.events = events;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //känner mig osäker på parent.getContext => läs mer om LayoutInflater.from
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    //Change! Will crash!
    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        holder.tag1.setText(events.get(position).getTags().get(0).getName());
        holder.tag2.setText(events.get(position).getTags().get(1).getName());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
