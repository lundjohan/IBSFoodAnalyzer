package com.johanlund.screens.events_templates_actions.mvc_views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.johanlund.base_classes.Event;
import com.johanlund.constants.Constants;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.model.EventsTemplate;
import com.johanlund.screens.common.mvcviews.ViewMvc;
import com.johanlund.screens.common.mvcviews.ViewMvcAbstract;
import com.johanlund.screens.event_activities.mvc_controllers.ChangeEventActivity;
import com.johanlund.screens.events_container_classes.EventsContainerUser;
import com.johanlund.screens.events_container_classes.common.EventsContainer;
import com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvc;
import com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvcImpl;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static com.johanlund.constants.Constants.EVENT_POSITION;
import static com.johanlund.constants.Constants.EVENT_TYPE;
import static com.johanlund.constants.Constants.ID_OF_EVENT;
import static com.johanlund.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;

public abstract class EventsTemplateViewMvcAbstract extends ViewMvcAbstract implements EventsTemplateViewMvc, EventsContainerUser, EventButtonsViewMvc{
    protected final Context context;
    protected EventsTemplateViewMvc.Listener listener;
    protected EventsContainer ec;

    public EventsTemplateViewMvcAbstract(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.activity_events_template, container, true);
        context = inflater.getContext();
        ViewGroup upperPart = rootView.findViewById(R.id.upperPart);
        inflater.inflate(getUpperPartOfLayout(), upperPart, true);

        //Set up EventsContainer
        ec = new EventsContainer(this, context);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        ec.initiateRecyclerView(recyclerView, false, context);
    }

    @NotNull
    @Override
    public EventsContainer getEventsContainer() {
        return ec;
    }

    protected abstract int getUpperPartOfLayout();


    @Override
    public void setListener(EventsTemplateViewMvc.Listener listener){
        this.listener = listener;

    }

    public void doneClicked(View v){
        EventsTemplate toReturn = createEventsTemplateFromView();
        listener.completeSession(toReturn);
    }

    @Override
    public void onItemClicked(View v, int position) {
        ec.editEvent(position);
    }

    @Override
    public boolean onItemLongClicked(final View v, final int position) {
        //initiate pop-up menu
        PopupMenu popup = new PopupMenu(context, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.event_delete_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.deleteEventForEventsTemplate) {
                    ec.eventsOfDay.remove(position);
                    ec.adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        popup.show();
        return true;
    }

    /*
    --------------------------------------------------------------------------------------------
    EventContainerUser methods
    --------------------------------------------------------------------------------------------
    */
    @Override
    public void bindEventToList(Event event) {
        ec.eventsOfDay.add(event);
        //All dates must be the same, becuase dates are irrellevant in a EventsTemplate,
        // only time matter.
        //TODO: implement constriction for above
        Collections.sort(ec.eventsOfDay);
        ec.adapter.notifyDataSetChanged();

    }


    @Override
    public void bindChangedEventToList (Event event, int posInList){
        ec.changeEventInList(posInList, event);
    }

    @Override
    public void updateTagsInListOfEventsAfterTagTemplateChange() {
        EventsTemplate et = createEventsTemplateFromView();
        EventsTemplate controlledEt = listener.removeTagTypesThatDontExist(et);
        bindEventsTemplateToView(controlledEt);
        ec.adapter.notifyDataSetChanged();
    }

    //listener to use lower in hiearchy
    @Override
    protected ViewMvc.Listener getListener() {
        return listener;
    }
    @Override
    public void handleEcOnActivityResult(int requestCode, int resultCode, Intent data) {
        ec.onActivityResult(requestCode, resultCode, data);
    }
    /*
   --------------------------------------------------------------------------------------------
   EventsContainerUser.Listener methods
   --------------------------------------------------------------------------------------------
   */
    //user requests to change event
    @Override
    public void changeEventActivity(Event event, int eventType, int valueToReturn, int
            posInList) {
        listener.changeEventActivity(event,eventType, valueToReturn,
        posInList);
    }

    //TODO code is extremly similar to DiaryFragment (except for database handling)
    @Override
    public void executeChangedEvent(int requestCode, Intent data) {
        listener.executeChangedEvent(requestCode,data);
    }

}
