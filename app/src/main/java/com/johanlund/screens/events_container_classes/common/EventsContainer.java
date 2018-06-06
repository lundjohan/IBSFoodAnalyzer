package com.johanlund.screens.events_container_classes.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.Meal;
import com.johanlund.base_classes.Other;
import com.johanlund.base_classes.Rating;
import com.johanlund.screens.event_activities.BmActivity;
import com.johanlund.screens.event_activities.ExerciseActivity;
import com.johanlund.screens.event_activities.MealActivity;
import com.johanlund.screens.event_activities.OtherActivity;
import com.johanlund.screens.event_activities.RatingActivity;
import com.johanlund.util.Util;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.johanlund.constants.Constants.CHANGED_EVENT;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED;
import static com.johanlund.screens.events_container_classes.DiaryFragment.CHANGED_BM;
import static com.johanlund.screens.events_container_classes.DiaryFragment.CHANGED_EXERCISE;
import static com.johanlund.screens.events_container_classes.DiaryFragment.CHANGED_MEAL;
import static com.johanlund.screens.events_container_classes.DiaryFragment.CHANGED_OTHER;
import static com.johanlund.screens.events_container_classes.DiaryFragment.CHANGED_RATING;

/**
 * Created by Johan on 2017-08-30.
 * <p>
 * Common class for Activities or Fragments that has a graphically container that handles listing
 * of events. A recyclerview is used as the list.
 * <p>
 * Users of this class must implement EventsContainerUser.
 * <p>
 * The class handles changes of events, and potential updates of tagtemplates in diary.
 */

public class EventsContainer {
    public interface EventsContainerUser extends EventAdapter
            .EventAdapterUser {
        void addEventToList(Event event);

        void executeChangedEvent(int requestCode, Intent data);

        void changeEventActivity(Event event, Class activityClass, int valueToReturn, int
                posInList);

        void updateTagsInListOfEventsAfterTagTemplateChange();
    }

    public static final int NEW_MEAL = 1000;
    public static final int NEW_OTHER = 1001;
    public static final int NEW_EXERCISE = 1002;
    public static final int NEW_BM = 1003;
    public static final int NEW_RATING = 1004;
    public RecyclerView recyclerView;
    public List<Event> eventsOfDay = new ArrayList<>();
    EventsContainerUser user;
    LinearLayoutManager layoutManager;
    public EventAdapter adapter;
    //context is solely used to retrieve resources inside EventAdapter
    private Context context;

    public EventsContainer(EventsContainerUser user, Context context) {
        this.user = user;
        this.context = context;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data.hasExtra(TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED)) {
            user.updateTagsInListOfEventsAfterTagTemplateChange();
        }
        if (data.hasExtra(CHANGED_EVENT)) {
            user.executeChangedEvent(requestCode, data);
        }
    }


    public void initiateRecyclerView(RecyclerView recyclerView, boolean colorRating, Context layoutInitiator) {
        this.recyclerView = recyclerView;
        layoutManager = new LinearLayoutManager(layoutInitiator, LinearLayoutManager
                .VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(eventsOfDay, user, colorRating, context);
        recyclerView.setAdapter(adapter);
        Util.addLineSeparator(recyclerView, layoutManager);
    }

    /**
     * This method is called when user want to CHANGE an event as opposed to create a new one.
     *
     * @param position
     */
    public void editEvent(int position) {
        Event event = eventsOfDay.get(position);
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
        eventsOfDay.remove(pos);
        //this line is needed, otherwise ec.adapter cannot handle it.
        adapter.notifyItemRemoved(pos);
        user.addEventToList(e);
    }

    public View getItemView(int pos) {
        return layoutManager.findViewByPosition(pos);
    }
}