package com.ibsanalyzer.inputday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.ibsanalyzer.base_classes.BM;
import com.ibsanalyzer.base_classes.Score;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.tag;
import static com.ibsanalyzer.inputday.R.drawable.meal;
import static com.ibsanalyzer.inputday.R.id.tagView;
import static java.security.AccessController.getContext;

/**
 * Created by Johan on 2017-04-10.
 */

class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Event> events = new ArrayList<>();
    private DiaryFragment usingFragment;
    public EventAdapter(List<Event> events, DiaryFragment fragment) {
        this.events = events; this.usingFragment = fragment;
    }
   private final int MEAL = 0, _BM = 3, SCORE = 4;

    /*
    Click Listeners
     */
    public interface OnItemClickListener {
        public void onItemClicked(View v, int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(View v, int position);
    }

    abstract class EventViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView time;
        public EventViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
    //this is for tags
    abstract class InputEventViewHolder extends EventViewHolder {
        public ListView tagList;
        public List<String>listItems;
        ArrayAdapter<String>adapter;
        public InputEventViewHolder(View itemView,  Context context) {
            super(itemView);
            tagList = (ListView)itemView.findViewById(tagView);
            listItems = new ArrayList<>();
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listItems);


            tagList.setAdapter(adapter);
            //makes list non-clickable
            tagList.setClickable(false);
            tagList.setItemsCanFocus(false);


        }
    }
    class MealViewHolder extends InputEventViewHolder {
        public TextView portions;

        public MealViewHolder(View itemView, Context context) {
            super(itemView, context);
            portions = (TextView) itemView.findViewById(R.id.portions);

            //add tags, to be moved up in hierarchy

        }
    }
    class BmViewHolder extends EventViewHolder {
        public TextView bristol;
        public TextView completeness;

        public BmViewHolder(View itemView) {
            super(itemView);
            bristol = (TextView) itemView.findViewById(R.id.bristol);
            completeness = (TextView) itemView.findViewById(R.id.completeness);
        }
    }
    class ScoreViewHolder extends EventViewHolder {
        public TextView toTime;
        public TextView afterScore;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            toTime = (TextView) itemView.findViewById(R.id.toTime);
            afterScore = (TextView) itemView.findViewById(R.id.scoreAfter);
        }
    }


    /*method implemented with help from https://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView#viewholder2-java*/
    @Override
    public int getItemViewType(int position) {
        if (events.get(position) instanceof Meal) {
            return MEAL;
        } else if (events.get(position) instanceof BM) {
            return _BM;
        }else if (events.get(position) instanceof Score) {
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
                viewHolder = new MealViewHolder(v, parent.getContext());
                break;
            case 3:
                v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bm, parent, false);
                viewHolder = new BmViewHolder(v);
                break;
            case 4:
                v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
                viewHolder = new ScoreViewHolder(v);
                break;
        }
        //here: make v clickable item.

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Event event = events.get(position);
        /*
        Add clickability and holding in to item for copying etc.

        Inspiration from http://stackoverflow.com/questions/27945078/onlongitemclick-in-recyclerview
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usingFragment.onItemClicked(v, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                usingFragment.onItemLongClicked(v, position);
                return true;
            }
        });

        switch (holder.getItemViewType()) {
            case 0: //MEAL
                Meal meal = (Meal)event;
                MealViewHolder mealHolder = (MealViewHolder) holder;
                setTime(meal,mealHolder);
                mealHolder.portions.setText(String.valueOf(meal.getPortions()));

                //s. 323
                //do this one generic in InputEventViewHolder
                List<String>tagStrings = new ArrayList<>();
                for (Tag tag: meal.getTags()){
                    mealHolder.listItems.add(tag.getName());
                }
                mealHolder.adapter.notifyDataSetChanged();
                break;
            case 3: //BM
                BM bm = (BM)event;
                BmViewHolder bmHolder = (BmViewHolder) holder;
                setTime(bm, bmHolder);
                bmHolder.completeness.setText(BM.completenessScoreToText(bm.getComplete()));
                bmHolder.bristol.setText(String.valueOf(bm.getBristol()));
                break;
            case 4: //SCORE
                Score score = (Score)event;
                ScoreViewHolder scoreHolder = (ScoreViewHolder) holder;
                setTime(score,scoreHolder);

                //toTime will be much more advanced, do this implementation much later
                scoreHolder.toTime.setText("tomorrow 10:00");

                scoreHolder.afterScore.setText(Score.pointsToText(score.getAfter()));
                break;
        }
    }



    @Override
    public int getItemCount() {
        return events.size();
    }

    private void setTime(Event event, EventViewHolder holder) {
        LocalDateTime time = event.getTime();
        holder.time.setText(formatTime(time));
    }
    private String formatTime (LocalDateTime ldt){
        return String.format("%02d",ldt.getHour())+':'+String.format("%02d",ldt.getMinute());
    }
}
