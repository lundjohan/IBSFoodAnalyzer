package com.ibsanalyzer.inputday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.BM;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Score;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.inputday.R.id.tagNames;
import static com.ibsanalyzer.inputday.R.id.tagQuantities;

/**
 * Created by Johan on 2017-04-10.
 */

class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Event> events = new ArrayList<>();
    private DiaryFragment usingFragment;
    public EventAdapter(List<Event> events, DiaryFragment fragment) {
        this.events = events; this.usingFragment = fragment;
    }
   private final int _MEAL = 0, _OTHER = 1,_EXERCISE = 2,_BM = 3, _SCORE = 4;

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
        public LinearLayout tagQuantsLayout;
        public LinearLayout tagNamesLayout;
        public Context context;
       /* public List<String>tagQuantsList;
        public List<String>tagNamesList;*/

        public InputEventViewHolder(View itemView, Context context) {
            super(itemView);
            //used for initation of textViews for lists
            this.context = context;
            tagQuantsLayout = (LinearLayout) itemView.findViewById(tagQuantities);
            tagNamesLayout = (LinearLayout) itemView.findViewById(tagNames);
        }
    }
    class MealViewHolder extends InputEventViewHolder {
        public TextView portions;

        public MealViewHolder(View itemView, Context context) {
            super(itemView, context);
            portions = (TextView) itemView.findViewById(R.id.portions);
        }
    }
    class OtherViewHolder extends InputEventViewHolder {

        public OtherViewHolder(View itemView, Context context) {
            super(itemView, context);
        }
    }
    class ExerciseViewHolder extends InputEventViewHolder {
        public TextView intensity;

        public ExerciseViewHolder(View itemView, Context context) {
            super(itemView, context);
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
                return _MEAL;
            }
            else if (events.get(position) instanceof Other) {
                return _OTHER;
            }
            else if (events.get(position) instanceof Exercise) {
                return _EXERCISE;
            }
            else if (events.get(position) instanceof BM) {
                return _BM;
            } else if (events.get(position) instanceof Score) {
                return _SCORE;
            }
        throw new RuntimeException("unknown class");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v;
        switch (viewType) {
            case _MEAL:
                v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
                viewHolder = new MealViewHolder(v, parent.getContext());
                break;
            case _OTHER:
                v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
                viewHolder = new OtherViewHolder(v, parent.getContext());
                break;
            case _EXERCISE:
                v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
                viewHolder = new ExerciseViewHolder(v, parent.getContext());
                break;
            case _BM:
                v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bm, parent, false);
                viewHolder = new BmViewHolder(v);
                break;
            case _SCORE:
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
                    TextView tagQuant = new TextView(mealHolder.tagQuantsLayout.getContext());
                    tagQuant.setText('X'+Double.toString(tag.getSize()));
                    mealHolder.tagQuantsLayout.addView(tagQuant);

                    TextView tagName = new TextView(mealHolder.tagNamesLayout.getContext());
                    tagName.setText(tag.getName());
                    mealHolder.tagNamesLayout.addView(tagName);
                }
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
