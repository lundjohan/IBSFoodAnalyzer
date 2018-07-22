package com.johanlund.screens.events_templates_actions.mvc_views;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.johanlund.base_classes.Event;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.model.EventsTemplate;
import com.johanlund.screens.common.mvcviews.ViewMvc;
import com.johanlund.screens.common.mvcviews.ViewMvcAbstract;
import com.johanlund.screens.events_container_classes.EventsContainerUser;
import com.johanlund.screens.events_container_classes.common.EventsContainer;
import com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvc;
import com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvcImpl;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public abstract class EventsTemplateViewMvcAbstract extends ViewMvcAbstract implements EventsTemplateViewMvc, EventsContainerUser, EventButtonsViewMvc{
    protected final Context context;
    protected EventsTemplateViewMvc.Listener listener;
    protected EventsContainer ec;
    protected EventButtonsViewMvcImpl mButtonsViewMvc;

    public EventsTemplateViewMvcAbstract(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.activity_events_template, container, true);
        context = inflater.getContext();
        ViewGroup upperPart = rootView.findViewById(R.id.upperPart);
        inflater.inflate(getUpperPartOfLayout(), upperPart, true);

        //Set up EventsContainer
        ec = new EventsContainer(this, listener, context);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        ec.initiateRecyclerView(recyclerView, false, context);


        //Set up EventButtonsViewMvcImpl
        mButtonsViewMvc = new EventButtonsViewMvcImpl(inflater, (ViewGroup) rootView.findViewById(R.id.buttons));
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
}
