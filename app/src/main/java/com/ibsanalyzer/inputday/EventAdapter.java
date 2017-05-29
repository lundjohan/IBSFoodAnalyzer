package com.ibsanalyzer.inputday;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.BM;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.InputEvent;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.date_time.DateTimeFormat;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.w3c.dom.Text;

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
        this.events = events;
        this.usingFragment = fragment;
    }

    private final int _MEAL = 0, _OTHER = 1, _EXERCISE = 2, _BM = 3, _SCORE = 4, _DATE_MARKER = 5;

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

        public void setBreakLayout() {
          //TODO Problem in below is that it takes makes competition with marked_event
            //=> solution seems to be to implement some top id in every item and then make it visible here or similiar
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
            Log.d("Debug", "Inside InputEventViewHolder, tagNamesLayout.toString() " +
                    tagNamesLayout.toString());
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


    /*method implemented with help from https://guides.codepath
    .com/android/Heterogenous-Layouts-inside-RecyclerView#viewholder2-java*/
    @Override
    public int getItemViewType(int position) {
        if (events.get(position) instanceof Meal) {
            return _MEAL;
        } else if (events.get(position) instanceof Other) {
            return _OTHER;
        } else if (events.get(position) instanceof Exercise) {
            return _EXERCISE;
        } else if (events.get(position) instanceof BM) {
            return _BM;
        } else if (events.get(position) instanceof Rating) {
            return _SCORE;
        } else if (events.get(position) instanceof DateMarkerEvent) {
            return _DATE_MARKER;
        }

        throw new RuntimeException("unknown class");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v;
        switch (viewType) {
            case _MEAL:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent,
                        false);
                viewHolder = new MealViewHolder(v, parent.getContext());
                break;
            case _OTHER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other, parent,
                        false);
                viewHolder = new OtherViewHolder(v, parent.getContext());
                break;
            case _EXERCISE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise,
                        parent, false);
                viewHolder = new ExerciseViewHolder(v);
                break;
            case _BM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bm, parent,
                        false);
                viewHolder = new BmViewHolder(v);
                break;
            case _SCORE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating,
                        parent, false);
                viewHolder = new ScoreViewHolder(v);
                break;
            case _DATE_MARKER:
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
        //check for breaks
        if (!(holder instanceof DateMarkerViewHolder)) {
            if (event.hasBreak()) {
                EventViewHolder eventHolder = (EventViewHolder) holder;
                eventHolder.setBreakLayout();
            }
        }
        switch (holder.getItemViewType()) {
            case _MEAL:
                Meal meal = (Meal) event;
                MealViewHolder mealHolder = (MealViewHolder) holder;
                mealHolder.portions.setText(String.valueOf(meal.getPortions()));
                bindTagsToTagEventViewHolder(meal, mealHolder);
                break;
            case _OTHER:
                Other other = (Other) event;
                OtherViewHolder otherHolder = (OtherViewHolder) holder;
                bindTagsToTagEventViewHolder(other, otherHolder);
                break;
            case _EXERCISE:
                Exercise exercise = (Exercise) event;
                ExerciseViewHolder exerciseHolder = (ExerciseViewHolder) holder;
                setTime(exercise, exerciseHolder);
                exerciseHolder.intensity.setText(Exercise.intensityLevelToText(exercise
                        .getIntensity()));
                exerciseHolder.typeOfExcercise.setText(exercise.getTypeOfExercise().getName());
                break;
            case _BM:
                BM bm = (BM) event;
                BmViewHolder bmHolder = (BmViewHolder) holder;
                setTime(bm, bmHolder);
                bmHolder.completeness.setText(BM.completenessScoreToText(bm.getComplete()));
                bmHolder.bristol.setText(String.valueOf(bm.getBristol()));
                break;
            case _SCORE:
                Rating rating = (Rating) event;
                ScoreViewHolder scoreHolder = (ScoreViewHolder) holder;
                setTime(rating, scoreHolder);
                scoreHolder.afterScore.setText(Rating.pointsToText(rating.getAfter()));
                break;
            case _DATE_MARKER:
                DateMarkerEvent dateMarker = (DateMarkerEvent) event;
                DateMarkerViewHolder dateMarkerViewHolder = (DateMarkerViewHolder) holder;
                dateMarkerViewHolder.dateView.setText(DateTimeFormat.toTextViewFormat(dateMarker
                        .getDate()));
                break;
        }
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    private void setTime(Event event, EventViewHolder holder) {
        LocalDateTime time = event.getTime();
        LocalDate ld = time.toLocalDate();
        LocalTime lt = time.toLocalTime();


        holder.time.setText(DateTimeFormat.toTextViewFormat(ld) + " " + DateTimeFormat
                .toTextViewFormat(lt));
    }

   /* private String formatTime(LocalDateTime ldt) {
        return DateTimeFormat.toTextViewFormat(ldt.toLocalTime());//String.format("%02d", ldt
        .getHour()) + ':' + String.format("%02d", ldt.getMinute());
    }*/

    private void bindTagsToTagEventViewHolder(InputEvent inputEvent, InputEventViewHolder
            tagHolder) {
        setTime(inputEvent, tagHolder);
        List<String> tagStrings = new ArrayList<>();
        for (Tag tag : inputEvent.getTags()) {
            TextView tagQuant = new TextView(tagHolder.tagQuantsLayout.getContext());
            tagQuant.setText('X' + Double.toString(tag.getSize()));
            tagHolder.tagQuantsLayout.addView(tagQuant);

            TextView tagName = new TextView(tagHolder.tagNamesLayout.getContext());
            tagName.setText(tag.getName());
            tagHolder.tagNamesLayout.addView(tagName);
        }
    }
}
