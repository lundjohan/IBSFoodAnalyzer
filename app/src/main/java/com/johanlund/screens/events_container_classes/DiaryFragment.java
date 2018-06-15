package com.johanlund.screens.events_container_classes;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.event_activities.mvc_controllers.ChangeEventActivity;
import com.johanlund.screens.event_activities.mvc_controllers.EventActivity;
import com.johanlund.screens.events_container_classes.common.EventsContainer;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.johanlund.constants.Constants.EVENT_POSITION;
import static com.johanlund.constants.Constants.CHANGING_EVENT_ID;
import static com.johanlund.constants.Constants.EVENT_TYPE;
import static com.johanlund.constants.Constants.ID_OF_EVENT;
import static com.johanlund.constants.Constants.ID_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.johanlund.constants.Constants.SWIPING_TO_DATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment implements EventsContainer
        .EventsContainerUser {

    //the date of the day as put by calender.
    LocalDate currentDate;

    public static final int CHANGED_MEAL = 1010;
    public static final int CHANGED_OTHER = 1011;
    public static final int CHANGED_EXERCISE = 1012;

    public static final int CHANGED_BM = 1013;
    public static final int CHANGED_RATING = 1014;

    public static final String TAG = "DebuggingDiaryFragment";
    static final int BACKGROUND_COLOR = Color.YELLOW;
    //===========================================================================================
    //timer used for tracking start time of app.
    public static long startTime = 0;
    //=============================================================================================


    //for pinning/ marking events, this must be cleaned when user quits application or app
    // crashes etc. List is sorted when added in asc order.
    List<Integer> eventsMarked = new ArrayList<>();


//==================================================================================================
    //as recommended for communication between Fragment to Activity.
    //https://developer.android.com/training/basics/fragments/communicating.html
//==================================================================================================

    EventsContainer ec;
    DiaryFragmentUser listener;
    public DiaryFragment() {

    }

    @Override
    public void addEventToList(Event event) {
        ec.eventsOfDay.add(event);
        //All dates must be the same, becuase dates are irrellevant in a EventsTemplate,
        // only time matter.
        //TODO: implement constriction for above
        Collections.sort(ec.eventsOfDay);
        ec.adapter.notifyDataSetChanged();
    }
//==================================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        ec = new EventsContainer(this, getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.events_layout);
        ec.initiateRecyclerView(recyclerView, true, this.getContext());

        //b is not allowed to be null
        Bundle b = this.getArguments();
        currentDate = ((LocalDate) b.getSerializable(SWIPING_TO_DATE));

        fillEventListWithDatabase(currentDate);
        if (savedInstanceState == null || !savedInstanceState.containsKey("ec.eventsOfDay")) {
            //populate array, this will be added to when button is pressed
            //===================================================================
            //  populateList();


            //=====================================================
        } else { //behövs denna eller räcker det med onRestoreInstanceState?
            //   ec.eventsOfDay = savedInstanceState.getParcelableArrayList("ec.eventsOfDay");
        }
        return view;
    }
    //this might be called before onCreateView is finished (/started?).
    LocalDate getCurrentDate(){
        if (currentDate!= null){
            return currentDate;
        }
        Bundle b = this.getArguments();
        return((LocalDate) b.getSerializable(SWIPING_TO_DATE));
    }

    //Will this work??? A Fragment (DiaryContainerFragment) listening to a Fragment (this)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DiaryFragmentUser) getParentFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ec.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void executeChangedEvent(int requestCode, Intent data) {
        int posInList = data.getIntExtra(POS_OF_EVENT_RETURNED, -1);
        long eventId = data.getLongExtra(ID_OF_EVENT_RETURNED, -1);
        if (posInList == -1) {
            throw new RuntimeException("Received no EVENT POSITION from New/Changed Event " +
                    "Activity (MealActivity etc)");
        } else if (eventId == -1) {
            throw new RuntimeException("Received no EVENT ID from New/Changed Event Activity " +
                    "(MealActivity etc)");
        }
        DBHandler dbHandler = new DBHandler(getContext());
        if (data.hasExtra(RETURN_EVENT_SERIALIZABLE)) {
            Event event = (Event) data.getSerializableExtra(RETURN_EVENT_SERIALIZABLE);
            dbHandler.changeEvent(eventId, event);
            ec.changeEventInList(posInList, event);
        }
    }

    @Override
    public void updateTagsInListOfEventsAfterTagTemplateChange() {
        fillEventListWithDatabase(currentDate);
    }

    /*
    Obs krasch om man klickar för snabbt, i alla fall vid ec.adapter.notifyItemRemoved!
     */
    @Override
    public void onItemClicked(View v, int position) {
        if (!markingModeIsOn()) {
            ec.editEvent(position);
        } else {
            clickHelper(v, position);
        }
    }
    /*
    see http://stackoverflow.com/questions/27945078/onlongitemclick-in-recyclerview
    These are for clicks on items in RecyclerView
     */

    //put in smaller methods to make clearer
    @Override
    public boolean onItemLongClicked(final View v, final int position) {
        final Event pressedEvent = ec.eventsOfDay.get(position);
        if (!markingModeIsOn()) {
            //initiate pop-up menu
            PopupMenu popup = new PopupMenu(getActivity(), v);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.event_item_marked_menu, popup.getMenu());
            //makeBreakOrUnBreakOpitionVisible
            Menu menu = popup.getMenu();
            MenuItem breakItem = menu.findItem(R.id.insertBreakMenuItem);
            MenuItem unBreakItem = menu.findItem(R.id.removeBreakMenuItem);

            if (pressedEvent.hasBreak()) {
                breakItem.setVisible(false);
                unBreakItem.setVisible(true);
            } else {
                breakItem.setVisible(true);
                unBreakItem.setVisible(false);
            }


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    //marking for templates, copy etc
                    if (item.getItemId() == R.id.markedMenuItem) {
                        eventsMarked.add(position);
                        v.setBackgroundColor(BACKGROUND_COLOR);
                        listener.changeToMarkedMenu();
                    }

                    //options down here for break/ unbreak
                    else if (item.getItemId() == R.id.insertBreakMenuItem) {
                        //1. make that event in item have a break true
                        addBreakToEvent(pressedEvent);

                        //2. update ec.adapter for that position
                        ec.adapter.notifyItemChanged(position);
                    } else if (item.getItemId() == R.id.removeBreakMenuItem) {
                        //1. make that event in item lose break
                        removeBreakFromevent(pressedEvent);

                        //2. update ec.adapter for that position
                        ec.adapter.notifyItemChanged(position);
                    } else if (item.getItemId() == R.id.deleteEvent) {
                        //1. remove event from database
                        DBHandler dbHandler = new DBHandler(getContext());
                        dbHandler.deleteEvent(pressedEvent);
                        //2 remove event from ec.eventsOfDay
                        ec.eventsOfDay.remove(pressedEvent);
                        ec.adapter.notifyDataSetChanged();
                    }
                    return true;
                }
            });
            popup.show();
        } else {
            clickHelper(v, position);
        }
        return false;
    }

    //add break both to event and to inside event table in database
    private void addBreakToEvent(Event e) {
        e.setHasBreak(true);
        DBHandler dbHandler = new DBHandler(getContext());
        dbHandler.changeEventExcludingEventsTemplates(e);
    }

    private void removeBreakFromevent(Event e) {
        e.setHasBreak(false);
        DBHandler dbHandler = new DBHandler(getContext());
        dbHandler.changeEventExcludingEventsTemplates(e);
    }

    //same actions for short and long clicks
    // given: markingModeIsOn
    private void clickHelper(View v, int position) {
        if (eventIsMarked(position)) {
            removeEventsMarked(v, position);

        } else {   //markingModeIsOn but eventIsNotMarked
            eventsMarked.add(position);

            /*sort the Integer List eventsMarked in ascending order. It will avoid confusion
             later when eventsMarked will be used to retrieve a list.*/
            Collections.sort(eventsMarked);
            v.setBackgroundColor(BACKGROUND_COLOR);
        }
    }

    private void removeEventsMarked(View v, int position) {

        eventsMarked.remove(Integer.valueOf(position)); //remove special case integer
        v.setBackgroundColor(Color.WHITE);

        //if last item now is unmarked, then change back menu.
        if (!markingModeIsOn()) {
            listener.changeToTabbedMenu();

        }
    }

    List<Event> retrieveMarkedEvents() {
        List<Event> eventsToSend = new ArrayList<>();
        for (int i : eventsMarked) {
            eventsToSend.add(ec.eventsOfDay.get(i));
        }
        //

        return eventsToSend;
    }



    private boolean markingModeIsOn() {
        return eventsMarked.size() > 0;
    }


    //user requests to change event
    public void changeEventActivity(Event event, int eventType, int valueToReturn, int
            posInList) {
        Intent intent = new Intent(getActivity(), ChangeEventActivity.class);
        intent.putExtra(EVENT_TYPE, eventType);
        intent.putExtra(EVENT_POSITION, posInList);
        DBHandler dbHandler = new DBHandler(getContext());
        long eventId = dbHandler.getEventIdOutsideEventsTemplate(event);
        intent.putExtra(ID_OF_EVENT, eventId);
        startActivityForResult(intent, valueToReturn);
    }

    private boolean eventIsMarked(int position) {
        return eventsMarked.contains(position);
    }

    public void fillEventListWithDatabase(LocalDate theDate) {
        DBHandler dbHandler = new DBHandler(getContext());

        //see here why reference just cant be changed. notifyDataSetChanged won't work in that case.
        //https://stackoverflow.com/questions/15422120/notifydatasetchange-not-working-from
        // -custom-ec.adapter
        ec.eventsOfDay.clear();
        List<Event> sortedEvents = dbHandler.getAllEventsMinusEventsTemplateSortedFromDay(theDate);
        ec.eventsOfDay.addAll(sortedEvents);
        ec.adapter.notifyDataSetChanged();
        //place focus at top (otherwise user has to scroll up, which make time sizeable time for
        // large imports).
        ec.recyclerView.scrollToPosition(ec.eventsOfDay.size() - 1);
    }

    public List<Event> getEvents() {
        return ec.eventsOfDay;
    }


    void unmarkAllEvents() {
        for (int i = eventsMarked.size() - 1; i >= 0; i--) {
            View itemView = ec.getItemView(eventsMarked.get(i));
            if (itemView != null) {
                removeEventsMarked(itemView, eventsMarked.get(i));
            }
        }
    }

    public interface DiaryFragmentUser {
        void changeToTabbedMenu();

        void changeToMarkedMenu();
    }
}
