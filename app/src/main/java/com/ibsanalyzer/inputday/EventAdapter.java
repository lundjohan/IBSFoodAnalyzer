package com.ibsanalyzer.inputday;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ibsanalyzer.base_classes.Divider;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2017-04-10.
 */

class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Event> events = new ArrayList<>();

    public EventAdapter(List<Event> events) {
        this.events = events;
    }
    private final int MEAL = 0, SCORE = 4;

    class MealViewHolder extends RecyclerView.ViewHolder {
        public TextView tag1;
        public TextView tag2;

        public MealViewHolder(View itemView) {
            super(itemView);
            tag1 = (TextView) itemView.findViewById(R.id.tag1);
            tag2 = (TextView) itemView.findViewById(R.id.tag2);
        }
    }
    class ScoreViewHolder extends RecyclerView.ViewHolder {
        public TextView fromTime;
        public TextView toTime;
        public TextView afterScore;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            fromTime = (TextView) itemView.findViewById(R.id.fromTime);
            Log.d("Debug","inside constructor of ScoreViewHolder. scoreHolder.fromTime: "+ fromTime);
            toTime = (TextView) itemView.findViewById(R.id.toTime);
            afterScore = (TextView) itemView.findViewById(R.id.scoreAfter);
        }
    }


    /*method implemented with help from https://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView#viewholder2-java*/
    @Override
    public int getItemViewType(int position) {
        if (events.get(position) instanceof Meal) {
            return MEAL;
        } else if (events.get(position) instanceof Divider) {
            return SCORE;
        }
        return -1;  //is this good implementation?
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v;
        switch (viewType) {
            case 0:
                v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
                viewHolder = new MealViewHolder(v);
                break;
            case 4:
                v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
                viewHolder = new ScoreViewHolder(v);
                break;
        }
        return viewHolder;
    }


    //Change! Will crash!
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0: //MEAL
                MealViewHolder mealHolder = (MealViewHolder) holder;
                mealHolder.tag1.setText(events.get(position).getTags().get(0).getName());
                mealHolder.tag2.setText(events.get(position).getTags().get(1).getName());
                break;

            case 4: //SCORE
                ScoreViewHolder scoreHolder = (ScoreViewHolder) holder;
                Divider div = (Divider) events.get(position);
                LocalDateTime from = div.getTime();
                Log.d("Debug","scoreHolder: "+ scoreHolder);
                Log.d("Debug","scoreHolder.fromTime: "+ scoreHolder.fromTime);
                scoreHolder.fromTime.setText(String.valueOf(from.getHour())+':'+String.valueOf(from.getMinute()));

                //toTime will be much more advanced, do this implementation much later
                scoreHolder.toTime.setText("tomorrow 10:00");

                scoreHolder.afterScore.setText(Double.toString(div.getAfter()));
                break;
        }


    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
