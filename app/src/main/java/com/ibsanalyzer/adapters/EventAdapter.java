package com.ibsanalyzer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.InputEvent;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.date_time.DateTimeFormat;
import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;
import com.ibsanalyzer.util.Util;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.BM;
import static com.ibsanalyzer.constants.Constants.DATE_MARKER;
import static com.ibsanalyzer.constants.Constants.EXERCISE;
import static com.ibsanalyzer.constants.Constants.MEAL;
import static com.ibsanalyzer.constants.Constants.OTHER;
import static com.ibsanalyzer.constants.Constants.RATING;
import static com.ibsanalyzer.diary.R.id.tagNames;
import static com.ibsanalyzer.diary.R.id.tagQuantities;

/**
 * Created by Johan on 2017-04-10.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Event> events = new ArrayList<>();
    private EventAdapterUser usingEntity;

    public EventAdapter(List<Event> events, EventAdapterUser usingEntity) {
        this.events = events;
        this.usingEntity = usingEntity;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    private void setTime(Event event, EventViewHolder holder) {
        LocalDateTime time = event.getTime();
        LocalTime lt = time.toLocalTime();

        holder.time.setText(DateTimeFormat
                .toTextViewFormat(lt));
    }

    private void bindTagsToTagEventViewHolder(InputEvent inputEvent, InputEventViewHolder
            tagHolder) {
        setTime(inputEvent, tagHolder);

        //clearing up, because recyclerview remembers cache. Problems with to many tags in
        // imports of files otherwise
        tagHolder.tagQuantsLayout.removeAllViews();
        tagHolder.tagNamesLayout.removeAllViews();

        for (Tag tag : inputEvent.getTags()) {
            TextView tagQuant = new TextView(tagHolder.tagQuantsLayout.getContext());
            tagQuant.setText('X' + Double.toString(tag.getSize()));
            tagHolder.tagQuantsLayout.addView(tagQuant);

            TextView tagName = new TextView(tagHolder.tagNamesLayout.getContext());
            tagName.setText(tag.getName());
            tagHolder.tagNamesLayout.addView(tagName);
        }
    }

    /*method implemented with help from https://guides.codepath
    .com/android/Heterogenous-Layouts-inside-RecyclerView#viewholder2-java*/
    @Override
    public int getItemViewType(int position) {
        Event e = events.get(position);
        return Util.getTypeOfEvent(e);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v;
        switch (viewType) {
            case MEAL:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent,
                        false);
                viewHolder = new MealViewHolder(v, parent.getContext());
                break;
            case OTHER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other, parent,
                        false);
                viewHolder = new OtherViewHolder(v, parent.getContext());
                break;
            case EXERCISE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise,
                        parent, false);
                viewHolder = new ExerciseViewHolder(v);
                break;
            case BM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bm, parent,
                        false);
                viewHolder = new BmViewHolder(v);
                break;
            case RATING:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating,
                        parent, false);
                viewHolder = new ScoreViewHolder(v);
                break;
            case DATE_MARKER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_datemarker,
                        parent, false);
                viewHolder = new DateMarkerViewHolder(v);
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
                usingEntity.onItemClicked(v, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                usingEntity.onItemLongClicked(v, position);
                return true;
            }
        });
        //check for breaks
        if (!(holder instanceof DateMarkerViewHolder)) {
            if (event.hasBreak()) {
                EventViewHolder eventHolder = (EventViewHolder) holder;
                eventHolder.setBreakLayout();
            }
        }
        switch (holder.getItemViewType()) {
            case MEAL:
                Meal meal = (Meal) event;
                MealViewHolder mealHolder = (MealViewHolder) holder;
                mealHolder.portions.setText(String.valueOf(meal.getPortions()));
                bindTagsToTagEventViewHolder(meal, mealHolder);
                break;
            case OTHER:
                Other other = (Other) event;
                OtherViewHolder otherHolder = (OtherViewHolder) holder;
                bindTagsToTagEventViewHolder(other, otherHolder);
                break;
            case EXERCISE:
                Exercise exercise = (Exercise) event;
                ExerciseViewHolder exerciseHolder = (ExerciseViewHolder) holder;
                setTime(exercise, exerciseHolder);
                exerciseHolder.intensity.setText(Exercise.intensityLevelToText(exercise
                        .getIntensity()));
                exerciseHolder.typeOfExcercise.setText(exercise.getTypeOfExercise().getName());
                break;
            case BM:
                Bm bm = (Bm) event;
                BmViewHolder bmHolder = (BmViewHolder) holder;
                setTime(bm, bmHolder);
                bmHolder.completeness.setText(Bm.completenessScoreToText(bm.getComplete()));
                bmHolder.bristol.setText(String.valueOf(bm.getBristol()));
                break;
            case RATING:
                Rating rating = (Rating) event;
                ScoreViewHolder scoreHolder = (ScoreViewHolder) holder;
                setTime(rating, scoreHolder);
                scoreHolder.afterScore.setText(Rating.pointsToText(rating.getAfter()));
                break;
            case DATE_MARKER:
                DateMarkerEvent dateMarker = (DateMarkerEvent) event;
                DateMarkerViewHolder dateMarkerViewHolder = (DateMarkerViewHolder) holder;
                dateMarkerViewHolder.dateView.setText(DateTimeFormat.toTextViewFormat(dateMarker
                        .getDate()));
                break;
        }
    }

    /*
    Click Listeners
     */
    public interface EventAdapterUser {
        public void onItemClicked(View v, int position);

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

        public void setBreakLayout() {
            //TODO Problem in below is that it takes makes competition with marked_event
            //=> solution seems to be to implement some top id in every item and then make it
            // visible here or similiar
            //  itemView.setBackgroundResource(R.drawable.frame_top_bold);

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

    class ExerciseViewHolder extends EventViewHolder {
        public TextView typeOfExcercise;
        public TextView intensity;

        public ExerciseViewHolder(View itemView) {

            super(itemView);
            typeOfExcercise = (TextView) itemView.findViewById(R.id.exercise_type);
            intensity = (TextView) itemView.findViewById(R.id.intensity);
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
            afterScore = (TextView) itemView.findViewById(R.id.scoreAfter);
        }
    }

    //special case -> this one is not a REAL event. Its only purpose is to show start (actually
    // placed at the end)of Day
    class DateMarkerViewHolder extends RecyclerView.ViewHolder {
        public TextView dateView;

        public DateMarkerViewHolder(View itemView) {
            super(itemView);
            this.dateView = (TextView) itemView.findViewById(R.id.dateMarker);
        }
    }
}
