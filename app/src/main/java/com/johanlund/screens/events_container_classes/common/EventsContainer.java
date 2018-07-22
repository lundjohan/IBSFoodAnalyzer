package com.johanlund.screens.events_container_classes.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.johanlund.base_classes.Event;
import com.johanlund.screens.events_container_classes.DiaryFragment;
import com.johanlund.screens.events_container_classes.EventsContainerUser;
import com.johanlund.util.Util;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.johanlund.constants.Constants.CHANGED_EVENT;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED;

/**
 * Created by Johan on 2017-08-30.
 * <p>
 * Common class for Activities or Fragments that has a graphically container that handles listing
 * of events. A recyclerview is used as the list.
 * <p>
 * Users of this class must implement EventsContainerUser.
 * <p>
 * The class handles changes of events, and potential updates of tagtemplates in diary.
 * <p>
 *  This class should in principle have no knowledge of database etc. It is only using a list.
 *  (A small exception in adapter exist for colorcoding the events, however)
 */

public class EventsContainer {
    public static final int EVENT_CHANGE = 1010;
    public static final int EVENT_NEW = 1000;
    public RecyclerView recyclerView;
    public List<Event> eventsOfDay = new ArrayList<>();
    public EventAdapter adapter;
    EventsContainerUser user;
    EventsContainerUser.Listener userListener;
    LinearLayoutManager layoutManager;
    //context is solely used to retrieve resources inside EventAdapter
    private Context context;

    /**
     * This class' raison d'être is to avoid code duplication. It should actually be part of users view-and-controller set-up.
     * That's why it looks fragmented with references both to a view user and a controller user.
     * @param user - the view that using this class
     * @param userListener  - the controller that uses this class
     * @param context
     */
    public EventsContainer(EventsContainerUser user, EventsContainerUser.Listener userListener, Context context) {
        this.user = user;
        this.userListener = userListener;
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
            userListener.executeChangedEvent(requestCode, data);
        }
    }

    public void initiateRecyclerView(RecyclerView recyclerView, boolean colorRating, Context
            layoutInitiator) {
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
        int eventType = event.getType();
        userListener.changeEventActivity(event, eventType, EVENT_CHANGE, position);
    }

    //first remove event from list
    //then adds a new
    public void changeEventInList(int pos, Event e) {
        eventsOfDay.remove(pos);
        //this line is needed, otherwise ec.adapter cannot handle it.
        adapter.notifyItemRemoved(pos);
        user.bindEventToList(e);
    }

    public View getItemView(int pos) {
        return layoutManager.findViewByPosition(pos);
    }
}