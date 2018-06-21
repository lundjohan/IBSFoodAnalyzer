package com.johanlund.screens.events_container_classes.common;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.InputEvent;
import com.johanlund.base_classes.Meal;
import com.johanlund.base_classes.Other;
import com.johanlund.base_classes.Rating;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.database.DBHandler;
import com.johanlund.date_time.DateTimeFormat;
import com.johanlund.ibsfoodanalyzer.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

import static com.johanlund.constants.Constants.BM;
import static com.johanlund.constants.Constants.EXERCISE;
import static com.johanlund.constants.Constants.MEAL;
import static com.johanlund.constants.Constants.OTHER;
import static com.johanlund.constants.Constants.RATING;
import static com.johanlund.ibsfoodanalyzer.R.id.tagNames;
import static com.johanlund.ibsfoodanalyzer.R.id.tagQuantities;

/**
 * Created by Johan on 2017-04-10.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Event> daysEvents = new ArrayList<>();
    private EventAdapterUser usingEntity;
    private boolean shouldHaveColorRating = true;
    //this is used solely to retrieve resources
    private Context context;

    public EventAdapter(List<Event> daysEvents, EventAdapterUser usingEntity, boolean shouldHaveColorRating, Context context) {
        this.daysEvents = daysEvents;
        this.usingEntity = usingEntity;
        this.shouldHaveColorRating = shouldHaveColorRating;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return daysEvents.size();
    }

    private void setTime(Event event, TextView timeView) {
        LocalDateTime time = event.getTime();
        LocalTime lt = time.toLocalTime();

        timeView.setText(DateTimeFormat
                .toTextViewFormat(lt));
    }

    private void bindTagsToTagEventViewHolder(InputEvent inputEvent, InputEventViewHolder
            tagHolder) {
        setTime(inputEvent, tagHolder.firstLine);

        //clearing up, because recyclerview remembers cache. Problems with too many tags in
        // imports of files otherwise
        tagHolder.tagQuantsLayout.removeAllViews();
        tagHolder.tagNamesLayout.removeAllViews();

        for (TagWithoutTime tag : inputEvent.getTagsWithoutTime()) {
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
        Event e = daysEvents.get(position);
        return e.getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v;
        switch (viewType) {
            case MEAL:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent,
                        false);
                viewHolder = new InputEventViewHolder(v, parent.getContext());
                break;
            case OTHER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other, parent,
                        false);
                viewHolder = new InputEventViewHolder(v, parent.getContext());
                break;
            case EXERCISE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise,
                        parent, false);
                viewHolder = new ViewHolderWithoutTagList(v);
                break;
            case BM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bm, parent,
                        false);
                viewHolder = new ViewHolderWithoutTagList(v);
                break;
            case RATING:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating,
                        parent, false);
                viewHolder = new ViewHolderWithoutTagList(v);
                break;
        }
        //here: make v clickable item.

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Event event = daysEvents.get(position);
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
        EventViewHolder eventHolder = (EventViewHolder) holder;
        //check for breaks
        if (event.hasBreak()) {
            eventHolder.setBreakLayout();
        }
        //add comments, same for all daysEvents
        if (eventHolder.comment != null) {
            eventHolder.comment.setText(event.getComment());
        }

        if (shouldHaveColorRating) {
            //add color to show rating score graphically
            eventHolder.colorFromRating.setBackgroundColor(retrieveRatingColor(position));
        }


        switch (holder.getItemViewType()) {
            case MEAL:
                Meal meal = (Meal) event;
                InputEventViewHolder mealHolder = (InputEventViewHolder) holder;
                mealHolder.secondLine.setText("Portions: " + String.valueOf(meal.getPortions()));
                bindTagsToTagEventViewHolder(meal, mealHolder);
                break;
            case OTHER:
                Other other = (Other) event;
                InputEventViewHolder otherHolder = (InputEventViewHolder) holder;
                bindTagsToTagEventViewHolder(other, otherHolder);
                break;
            case EXERCISE:
                Exercise exercise = (Exercise) event;
                ViewHolderWithoutTagList exerciseHolder = (ViewHolderWithoutTagList) holder;
                setTime(exercise, exerciseHolder.firstLine);
                exerciseHolder.rightLine.setText(Exercise.intensityLevelToText(exercise
                        .getIntensity()));
                exerciseHolder.secondLine.setText(exercise.getTypeOfExercise().getName());
                break;
            case BM:
                Bm bm = (Bm) event;
                ViewHolderWithoutTagList bmHolder = (ViewHolderWithoutTagList) holder;
                setTime(bm, bmHolder.firstLine);
                bmHolder.rightLine.setText(Bm.completenessScoreToText(bm.getComplete()));
                bmHolder.secondLine.setText("Bristol: " + String.valueOf(bm.getBristol()));
                break;
            case RATING:
                Rating rating = (Rating) event;
                ViewHolderWithoutTagList scoreHolder = (ViewHolderWithoutTagList) holder;
                setTime(rating, scoreHolder.secondLine);
                scoreHolder.rightLine.setText(Rating.pointsToText(rating.getAfter()));
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
        public View colorFromRating;
        public View itemView;
        public TextView comment;

        //used for time for all daysEvents except Rating, Rating prints a "From".
        public TextView firstLine;

        //used for type of exercise for Exercise, Bristol for BM, time for Rating, portions for Meal
        public TextView secondLine;

        public View breakLine;

        public EventViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            colorFromRating = itemView.findViewById(R.id.color_from_rating);
            comment = (TextView) itemView.findViewById(R.id.commentInItem);
            firstLine = (TextView) itemView.findViewById(R.id.firstLine);
            secondLine = (TextView) itemView.findViewById(R.id.secondLine);
            breakLine = (View) itemView.findViewById(R.id.break_line);
        }

        public void setBreakLayout() {
            breakLine.setVisibility(View.VISIBLE);

        }
    }

    //this is for Meal and Other Events
    class InputEventViewHolder extends EventViewHolder {
        public LinearLayout tagQuantsLayout;
        public LinearLayout tagNamesLayout;
        public Context context;

        public InputEventViewHolder(View itemView, Context context) {
            super(itemView);
            //used for initation of textViews for lists
            this.context = context;
            tagQuantsLayout = (LinearLayout) itemView.findViewById(tagQuantities);
            tagNamesLayout = (LinearLayout) itemView.findViewById(tagNames);
        }
    }

    //RangeHolder class for Exercise, BM and Rating.
    class ViewHolderWithoutTagList extends EventViewHolder {


        //used as score for Rating, completeness for BM, intensity for Exercise
        public TextView rightLine;

        public ViewHolderWithoutTagList(View itemView) {
            super(itemView);
            rightLine = (TextView) itemView.findViewById(R.id.rightLine);
        }
    }
//==================================================================================================
//Coloring of Event based on preceding Rating - these calculations are made here
//==================================================================================================

    /**
     * returns an int reflecting a color
     * if int is outside of scope, color white will be returned
     */
    private int retrieveRatingColor(int positionInDaysEvent) {
        int ratingOfEvent = retrieveRatingOfEvent(positionInDaysEvent);
        int color = ContextCompat.getColor(context, R.color.colorWhite);
        switch (ratingOfEvent) {
            case 1:
                color = ContextCompat.getColor(context, R.color.colorRatingAwful);
                break;
            case 2:
                color = ContextCompat.getColor(context, R.color.colorRatingBad);
                break;
            case 3:
                color = ContextCompat.getColor(context, R.color.colorRatingDeficient);
                break;
            case 4:
                color = ContextCompat.getColor(context, R.color.colorRatingOk);
                break;
            case 5:
                color = ContextCompat.getColor(context, R.color.colorRatingGreat);
                break;
            case 6:
                color = ContextCompat.getColor(context, R.color.colorRatingPhenomenal);
                break;
        }
        return color;
    }

    /**
     * The rating
     * returns -1 if event should have no rating (such is the case if event is not a rating, and
     * is not preceded by a rating)
     */
    private int retrieveRatingOfEvent(int posInEventsList) {
        Event e = daysEvents.get(posInEventsList);
        int toReturn = -1;
        if (e.getType() == RATING) {
            Rating rating = (Rating) e;
            toReturn = rating.getAfter();
        }
        //else event is other than Rating
        else {
            toReturn = retrieveScoreRatingOfPrecedingRatingInDay(posInEventsList, daysEvents);

            //there is no preceding rating in days eventlist, but it can be one earlier days.
            if (toReturn == 0) {
                LocalDate dayBefore = e.getTime().toLocalDate().minusDays(1);
                toReturn = retrieveScoreFromLastOccurringRatingFromDay(dayBefore);
                //if toReturn is still -1, let it be the return value.
            }
        }
        return toReturn;
    }

    /**
     * Has to take break into account.
     * <p>
     * Returns -1 if there is no preceding rating due to Break, 0 if there is no preceding rating
     * in this days list.
     *
     * @param posInEventsList
     * @return
     */
    private int retrieveScoreRatingOfPrecedingRatingInDay(int posInEventsList, List<Event>eventsOfDay) {
        int score = 0;
        //loop backwards, starting from event in list preceding posIn...
        for (int i = posInEventsList - 1; i >= 0; --i) {
            Event e = eventsOfDay.get(i);

            //if you have reach so far in loop, and e has a break, then return value will be -1.
            if (e.hasBreak()) {
                score = -1;
                break;
            }

            //hopefully the event is a rating, then we will get our score.
            else if (e instanceof Rating) {
                score = ((Rating) e).getAfter();
                break;
            }
        }
        //-1 if break and no earlier rating exist, 0 if no preceding rating for THIS day exist,
        // but might very well exist an earlier day.
        return score;
    }

    /**
     * Recursive function.
     * Searches until preceding rating is found (returns score),
     * or until break (returns -1),
     * or until there are no more preceding events to search (returns -1).
     *
     * @param theDay
     * @return
     */
    private int retrieveScoreFromLastOccurringRatingFromDay(LocalDate theDay) {
        //try the day before theDay.
        DBHandler dbHandler = new DBHandler(context);

        //database returns an empty list, in case there are no events to be retrieved for that day
        List<Event> eventsDay = dbHandler.getAllEventsMinusEventsTemplateSortedFromDay(theDay);
        if (eventsDay.isEmpty()){
            return -1;
        }
        //send last position of eventsDay as argument
        int score = retrieveScoreRatingOfPrecedingRatingInDay(eventsDay.size() - 1, eventsDay);
        if (score == 0) {
            LocalDate dayBefore = theDay.minusDays(1);
            retrieveScoreFromLastOccurringRatingFromDay(dayBefore);
        }
        return score;
    }
}
