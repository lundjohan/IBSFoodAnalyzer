package com.ibsanalyzer.inputday;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import com.ibsanalyzer.adapters.EventAdapter;
import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;
import com.ibsanalyzer.util.InsertPositions;
import com.ibsanalyzer.util.Util;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ibsanalyzer.constants.Constants.RETURN_BM_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_OTHER_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_RATING_SERIALIZABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment implements View.OnClickListener, EventAdapter
        .OnItemClickListener, EventAdapter.OnItemLongClickListener {
    public static final int NEW_MEAL = 1000;
    public static final int NEW_OTHER = 1001;
    public static final int NEW_EXERCISE = 1002;
    public static final int NEW_BM = 1003;
    public static final int NEW_SCORE = 1004;

    public static final String TAG = "DebuggingDiaryFragment";
    //===========================================================================================
    //timer used for tracking start time of app.
    public static long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    private synchronized void logTimePassed() {
        long nanos = System.nanoTime() - startTime;
       /* int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;*/

        Log.d(TAG, "Time passed in ms: " + nanos/1000000);

    }
    //=============================================================================================


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    EventAdapter adapter;

    List<Event> eventList = new ArrayList<>();

    //for pinning/ marking events, this must be cleaned when user quits application or app
    // crashes etc
    List<Integer> eventsMarked = new ArrayList<>();
    static final int BACKGROUND_COLOR = Color.YELLOW;

    //switcher tab and it's tabs
    ViewSwitcher tabsLayoutSwitcher;
    TabItem toTemplateTab;

//==================================================================================================
    //as recommended for communication between Fragment to Activity.
    //https://developer.android.com/training/basics/fragments/communicating.html
//==================================================================================================

    DiaryFragmentListener callback;


    // Container Activity must implement this interface
    public interface DiaryFragmentListener {
        ViewSwitcher getTabsLayoutSwitcher();

        void doEventsTemplateAdder(List<Event> events);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (DiaryFragmentListener) context;

        Bundle args = getArguments();
        setHasOptionsMenu(true);
    }
//==================================================================================================

    public DiaryFragment() {

        Bundle b = getArguments();

    }

    //p. 121 Android Essentials
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //    outState.putParcelableArrayList("eventList", new ArrayList<Event>(eventList));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //=====================TIMER================================================================
        Log.d(TAG, "BEFORE TIMER STARTS (DIARYFRAGMENT STARTS)");
        startTime = System.nanoTime();
        logTimePassed();
        //==========================================================================================

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        super.onCreate(savedInstanceState);

        //starts as invisible appBarLayout but when user marks something this pops up
        tabsLayoutSwitcher = (ViewSwitcher) callback.getTabsLayoutSwitcher();
        setUpEventButtons(view);
        initiateRecyclerView(view);

        setUpMenu();
        if (savedInstanceState == null || !savedInstanceState.containsKey("eventList")) {
            //populate array, this will be added to when button is pressed
            //===================================================================
            //  populateList();


            //=====================================================
        } else { //behövs denna eller räcker det med onRestoreInstanceState?
            //   eventList = savedInstanceState.getParcelableArrayList("eventList");
        }
        //fill recyclerView from database
//        fillEventListWithDatabase();
        //=====================TIMER================================================================
        Log.d(TAG, "BEFORE quiting onCreateView");
        logTimePassed();
        //==========================================================================================
        return view;
    }

    private void setUpEventButtons(View view) {
        //EventModel Buttons, do onClick here so handlers doesnt have to be in parent Activity
        ImageButton mealBtn = (ImageButton) view.findViewById(R.id.mealBtn);
        mealBtn.setOnClickListener(this);

        ImageButton otherBtn = (ImageButton) view.findViewById(R.id.otherBtn);
        otherBtn.setOnClickListener(this);

        ImageButton exerciseBtn = (ImageButton) view.findViewById(R.id.exerciseBtn);
        exerciseBtn.setOnClickListener(this);

        ImageButton bmBtn = (ImageButton) view.findViewById(R.id.bmBtn);
        bmBtn.setOnClickListener(this);

        ImageButton scoreBtn = (ImageButton) view.findViewById(R.id.ratingBtn);
        scoreBtn.setOnClickListener(this);
    }

    private void setUpMenu() {
        //cant come up with better solution for gaining access to toolbar buttons that lie on
        // main_activity.xml
        ImageButton toTemplateBtn = (ImageButton) getActivity().findViewById(R.id.to_template_btn);
        toTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.doEventsTemplateAdder(retrieveMarkedEvents());
            }
        });
    }

    private void initiateRecyclerView(View v) {
        //==========================================================================================
        recyclerView = (RecyclerView) v.findViewById(R.id.events_layout);
        layoutManager = new LinearLayoutManager((Context) this.callback, LinearLayoutManager
                .VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(adapter);
        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        //==========================================================================================
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        //common for all
        DBHandler dbHandler = new DBHandler(getContext());
        Event event = null;

        switch (requestCode) {

            case NEW_MEAL:
                if (data.hasExtra(RETURN_MEAL_SERIALIZABLE)) {
                    //add to database
                    event = (Meal) data.getSerializableExtra(RETURN_MEAL_SERIALIZABLE);
                    dbHandler.addMeal((Meal) event);
                }
                break;
            case NEW_OTHER:
                if (data.hasExtra(RETURN_OTHER_SERIALIZABLE)) {
                    event = (Other) data.getSerializableExtra(RETURN_OTHER_SERIALIZABLE);
                    dbHandler.addOther((Other) event);
                }
                break;
            case NEW_EXERCISE:
                if (data.hasExtra(RETURN_EXERCISE_SERIALIZABLE)) {
                    event = (Exercise) data.getSerializableExtra(RETURN_EXERCISE_SERIALIZABLE);
                    dbHandler.addExercise((Exercise) event);
                }
                break;
            case NEW_BM:
                if (data.hasExtra(RETURN_BM_SERIALIZABLE)) {
                    event = (Bm) data.getSerializableExtra(RETURN_BM_SERIALIZABLE);
                    dbHandler.addBm((Bm) event);
                }
                break;
            case NEW_SCORE:
                if (data.hasExtra(RETURN_RATING_SERIALIZABLE)) {
                    event = (Rating) data.getSerializableExtra(RETURN_RATING_SERIALIZABLE);
                    dbHandler.addRating((Rating) event);
                }
                break;
        }
        addEventToList(eventList, event, adapter);

    }

    //also adds DateMarkerEvent if appropriate
    private static void addEventToList(List<Event> events, Event event, RecyclerView.Adapter
            adapter) {
        InsertPositions insertPositions = Util.insertEventWithDayMarker(events, event);

        //notify RecyclerView of changes
        adapter.notifyItemInserted(insertPositions.getPosInserted());
        if (insertPositions.isDateMarkerAdded()) {
            adapter.notifyItemInserted(insertPositions.getPosDateMarker());
        }
    }

    /*This is needed since onClick otherwise goes to parent Activity*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mealBtn:
                newMealActivity(v);
                break;
            case R.id.otherBtn:
                newOtherActivity(v);
                break;
            case R.id.exerciseBtn:
                newExerciseActivity(v);
                break;
            case R.id.bmBtn:
                newBmActivity(v);
                break;
            case R.id.ratingBtn:
                newScoreItem(v);
                break;
        }
        //do other buttons here
    }


    //dessa metoder ska istället skapa fragments.
    //ska de skapas inifrån denna fragment (vad jag tror) eller från callBack
    public void newMealActivity(View view) {
        Intent intent = new Intent((Activity) this.callback, MealActivity.class);
        startActivityForResult(intent, NEW_MEAL);
    }

    private void newOtherActivity(View v) {
        Intent intent = new Intent((Activity) this.callback, OtherActivity.class);
        startActivityForResult(intent, NEW_OTHER);
    }

    private void newExerciseActivity(View v) {
        Intent intent = new Intent((Activity) this.callback, ExerciseActivity.class);
        startActivityForResult(intent, NEW_EXERCISE);
    }

    private void newBmActivity(View v) {
        Intent intent = new Intent((Activity) this.callback, BmActivity.class);
        startActivityForResult(intent, NEW_BM);
    }

    public void newScoreItem(View view) {
        Intent intent = new Intent((Activity) this.callback, RatingActivity.class);
        startActivityForResult(intent, NEW_SCORE);
    }


    /*
    see http://stackoverflow.com/questions/27945078/onlongitemclick-in-recyclerview
    These are for clicks on items in RecyclerView
     */

    /*
    Obs krasch om man klickar för snabbt, i alla fall vid adapter.notifyItemRemoved!
     */
    @Override
    public void onItemClicked(View v, int position) {
        final Event pressedEvent = eventList.get(position);
        if (pressedEvent instanceof DateMarkerEvent) {
            return;
        }
        Log.d("Debug", "inside fragment, item was clicked");
        if (!markingModeIsOn()) {
            editEvent(position);
        } else {
            clickHelper(v, position);
        }
    }


    //put in smaller methods to make clearer
    @Override
    public boolean onItemLongClicked(final View v, final int position) {
        final Event pressedEvent = eventList.get(position);
        //it should not be possible to press a DateMarkerEvent
        if (pressedEvent instanceof DateMarkerEvent) {
            return false;
        }
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
                        changeToMarkedMenu();
                    }

                    //options down here for break/ unbreak
                    else if (item.getItemId() == R.id.insertBreakMenuItem) {
                        //1. make that event in item have a break true
                        pressedEvent.setBreak(true);
                        //2. update adapter for that position
                        adapter.notifyItemChanged(position);
                    } else if (item.getItemId() == R.id.removeBreakMenuItem) {
                        //1. make that event in item lose break
                        pressedEvent.setBreak(false);
                        //2. update adapter for that position
                        adapter.notifyItemChanged(position);
                    }
                    else if (item.getItemId() == R.id.deleteEvent) {
                        //1. remove event from database
                        DBHandler dbHandler = new DBHandler(getContext());
                        dbHandler.deleteEvent(pressedEvent);
                        //2 remove event from eventList
                        int removedDateMarker = Util.removeEventAndAlsoDateMarkerIfLast(eventList, position);

                        //3 notify RecyclerView of changes
                        //3.1 DateMarkerPos is last in list and should be removed first
                        if (removedDateMarker != -1) {
                            adapter.notifyItemRemoved(removedDateMarker);
                        }
                        //3.2 Pos of removed normal event
                        adapter.notifyItemRemoved(position);
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

    //same actions for short and long clicks
    // given: markingModeIsOn
    private void clickHelper(View v, int position) {
        if (eventIsMarked(position)) {
            eventsMarked.remove(Integer.valueOf(position)); //remove special case integer
            v.setBackgroundColor(Color.WHITE);

            //if last item now is unmarked, then change back menu.
            if (!markingModeIsOn()) {
                changeToTabbedMenu();

            }
        } else {   //markingModeIsOn but eventIsNotMarked
            eventsMarked.add(position);
            v.setBackgroundColor(BACKGROUND_COLOR);
        }
    }

    private List<Event> retrieveMarkedEvents() {
        List<Event> eventsToSend = new ArrayList<>();
        for (int i : eventsMarked) {
            eventsToSend.add(eventList.get(i));
        }
        return eventsToSend;
    }

    /*
        When user markes list items for template or copying
     */
    private void changeToMarkedMenu() {
        tabsLayoutSwitcher.showNext();
        //callBack.viewPager do something here to stop swipe
    }

    /*
        When user unmarkes list items
     */
    private void changeToTabbedMenu() {
        tabsLayoutSwitcher.showNext();

    }

    private boolean markingModeIsOn() {
        return eventsMarked.size() > 0;
    }

    private void editEvent(int position) {
        //TODO
    }

    private boolean eventIsMarked(int position) {
        return eventsMarked.contains(position);
    }

    public void fillEventListWithDatabase() {
        DBHandler dbHandler = new DBHandler(((Activity) callback).getApplicationContext());

        //see here why reference just cant be changed. notifyDataSetChanged won't work in that case.
        //https://stackoverflow.com/questions/15422120/notifydatasetchange-not-working-from
        // -custom-adapter
        eventList.clear();
        //=====================TIMER================================================================
        Log.d(TAG, "1. starting sortedEvents");
        Log.d(TAG, "2. BEFORE dbHandler.getAllEventsSorted()");
        logTimePassed();
        //==========================================================================================

        List<Event>sortedEvents =dbHandler.getAllEventsSorted();
       // Log.d(TAG, "3. sortedEvents.size()" + sortedEvents.size());
        logTimePassed();
        Log.d(TAG, "(4) 5. ended sortedEvents");
        //=====================TIMER================================================================
        Log.d(TAG, "BEFORE eventList.addAll()");
        logTimePassed();
        //==========================================================================================
        eventList.addAll(sortedEvents);
        //=====================TIMER================================================================
        Log.d(TAG, "BEFORE addDateEventsToList");
        logTimePassed();
        //==========================================================================================
        Util.addDateEventsToList(eventList);
        //=====================TIMER================================================================
        Log.d(TAG, "BEFORE adapter.notifyDataSetChanged");
        logTimePassed();
        //==========================================================================================
        adapter.notifyDataSetChanged();
        //place focus at top (otherwise user has to scroll up, which make time sizeable time for
        // large imports).
        //=====================TIMER================================================================
        Log.d(TAG, "BEFORE recyclerView.scrollToPosition");
        logTimePassed();
        //==========================================================================================
        recyclerView.scrollToPosition(eventList.size() - 1);
    }




    public List<Event> getEvents() {
        return eventList;
    }
}
