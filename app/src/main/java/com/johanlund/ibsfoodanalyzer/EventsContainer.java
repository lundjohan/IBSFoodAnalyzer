package com.johanlund.ibsfoodanalyzer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.johanlund.adapters.EventAdapter;
import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.Meal;
import com.johanlund.base_classes.Other;
import com.johanlund.base_classes.Rating;
import com.johanlund.util.Util;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.johanlund.constants.Constants.CHANGED_EVENT;
import static com.johanlund.constants.Constants.NEW_EVENT;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED;
import static com.johanlund.ibsfoodanalyzer.DiaryFragment.CHANGED_BM;
import static com.johanlund.ibsfoodanalyzer.DiaryFragment.CHANGED_EXERCISE;
import static com.johanlund.ibsfoodanalyzer.DiaryFragment.CHANGED_MEAL;
import static com.johanlund.ibsfoodanalyzer.DiaryFragment.CHANGED_OTHER;
import static com.johanlund.ibsfoodanalyzer.DiaryFragment.CHANGED_RATING;

/**
 * Created by Johan on 2017-08-30.
 * <p>
 * Common class for Activities or Fragments that has a graphically container that handles listing
 * of events.
 * Can be used together with its interface EventsContainerUser
 */

public class EventsContainer {


    public static final int NEW_MEAL = 1000;
    public static final int NEW_OTHER = 1001;
    public static final int NEW_EXERCISE = 1002;
    public static final int NEW_BM = 1003;
    public static final int NEW_RATING = 1004;
    public RecyclerView recyclerView;
    public List<Event> eventList = new ArrayList<>();
    EventsContainerUser user;
    LinearLayoutManager layoutManager;
    EventAdapter adapter;

    public EventsContainer(EventsContainerUser user) {
        this.user = user;
    }

    public void doOnClick(View v) {
        switch (v.getId()) {
            case R.id.mealBtn:
                user.newMealActivity(v);
                break;
            case R.id.otherBtn:
                user.newOtherActivity(v);
                break;
            case R.id.exerciseBtn:
                user.newExerciseActivity(v);
                break;
            case R.id.bmBtn:
                user.newBmActivity(v);
                break;
            case R.id.ratingBtn:
                user.newScoreItem(v);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data.hasExtra(TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED)){
            user.updateTagsInListOfEventsAfterTagTemplateChange();
        }
        if (data.hasExtra(CHANGED_EVENT)) {
            user.executeChangedEvent(requestCode, data);
        }
        if (data.hasExtra(NEW_EVENT)){
            user.executeNewEvent(requestCode, data);
        }
    }
    
    public void setUpEventButtons(View view) {
        //EventModel Buttons, do onClick here so handlers don't have to be in parent Activity
        ImageButton mealBtn = (ImageButton) view.findViewById(R.id.mealBtn);
        mealBtn.setOnClickListener(user);

        ImageButton otherBtn = (ImageButton) view.findViewById(R.id.otherBtn);
        otherBtn.setOnClickListener(user);

        ImageButton exerciseBtn = (ImageButton) view.findViewById(R.id.exerciseBtn);
        exerciseBtn.setOnClickListener(user);

        ImageButton bmBtn = (ImageButton) view.findViewById(R.id.bmBtn);
        bmBtn.setOnClickListener(user);

        ImageButton scoreBtn = (ImageButton) view.findViewById(R.id.ratingBtn);
        scoreBtn.setOnClickListener(user);
    }

    public void initiateRecyclerView(RecyclerView recyclerView, Context layoutInitiator) {
        this.recyclerView = recyclerView;
        layoutManager = new LinearLayoutManager(layoutInitiator, LinearLayoutManager
                .VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(eventList, user);
        recyclerView.setAdapter(adapter);
        Util.addLineSeparator(recyclerView, layoutManager);
    }

    /**
     * This method is called when user want to CHANGE an event as opposed to create a new one.
     *
     * @param position
     */
    public void editEvent(int position) {
        Event event = eventList.get(position);
        Class c = event.getClass();
        if (c.equals(Meal.class)) {
            user.changeEventActivity(event, MealActivity.class, CHANGED_MEAL, position);
        } else if (c.equals(Other.class)) {
            user.changeEventActivity(event, OtherActivity.class, CHANGED_OTHER, position);
        } else if (c.equals(Exercise.class)) {
            user.changeEventActivity(event, ExerciseActivity.class, CHANGED_EXERCISE, position);
        } else if (c.equals(Bm.class)) {
            user.changeEventActivity(event, BmActivity.class, CHANGED_BM, position);
        } else if (c.equals(Rating.class)) {
            user.changeEventActivity(event, RatingActivity.class, CHANGED_RATING, position);
        }
    }

    //first remove event from list
    //then adds a new
    public void changeEventInList(int pos, Event e) {
        eventList.remove(pos);
        //this line is needed, otherwise ec.adapter cannot handle it.
        adapter.notifyItemRemoved(pos);
        user.addEventToList(e);
    }
    public View getItemView(int pos){
       return layoutManager.findViewByPosition(pos);
    }

    public interface EventsContainerUser extends EventAdapter
            .EventAdapterUser, View.OnClickListener {
        void addEventToList(Event event);

        void executeNewEvent(int requestCode, Intent data);

        void executeChangedEvent(int requestCode, Intent data);

        void changeEventActivity(Event event, Class activityClass, int valueToReturn, int
                posInList);

        void newMealActivity(View view);

        void newOtherActivity(View v);

        void newExerciseActivity(View v);

        void newBmActivity(View v);

        void newScoreItem(View view);

        void updateTagsInListOfEventsAfterTagTemplateChange();
    }
}
