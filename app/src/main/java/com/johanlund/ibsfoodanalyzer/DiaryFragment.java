package com.johanlund.ibsfoodanalyzer;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.johanlund.adapters.EventsTemplateAdapter;
import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.date_time.DatePickerFragment;
import com.johanlund.info.ActivityInfoContent;
import com.johanlund.model.EventsTemplate;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.johanlund.constants.Constants.DATE_TO_START_NEW_EVENTACTIVITY;
import static com.johanlund.constants.Constants.EVENT_POSITION;
import static com.johanlund.constants.Constants.EVENT_TO_CHANGE;
import static com.johanlund.constants.Constants.ID_OF_EVENT;
import static com.johanlund.constants.Constants.ID_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.LIST_OF_EVENTS;
import static com.johanlund.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.johanlund.constants.Constants.SWIPING_TO_DATE;
import static com.johanlund.constants.Constants.TITLE_STRING;
import static com.johanlund.ibsfoodanalyzer.EventsContainer.NEW_BM;
import static com.johanlund.ibsfoodanalyzer.EventsContainer.NEW_EXERCISE;
import static com.johanlund.ibsfoodanalyzer.EventsContainer.NEW_MEAL;
import static com.johanlund.ibsfoodanalyzer.EventsContainer.NEW_OTHER;
import static com.johanlund.ibsfoodanalyzer.EventsContainer.NEW_RATING;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment implements EventsContainer
        .EventsContainerUser, DatePickerDialog.OnDateSetListener {

    // Container Activity must implement this interface
    public interface DiaryFragmentListener {
        void startTemplateFragment(LocalDate date);

        //when user picks a date from calendar.
        void changeDate(LocalDate date);
    }

    //the date of the day as put by calender.
    LocalDate currentDate;
    TextView dateView;

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
    //switcher tab and it's tabs
    ViewSwitcher tabsLayoutSwitcher;

//==================================================================================================
    //as recommended for communication between Fragment to Activity.
    //https://developer.android.com/training/basics/fragments/communicating.html
//==================================================================================================

    EventsContainer ec;
    DiaryFragmentListener diaryListener;

    public DiaryFragment() {

    }

    @Override
    public void addEventToList(Event event) {
        ec.eventList.add(event);
        //All dates must be the same, becuase dates are irrellevant in a EventsTemplate,
        // only time matter.
        //TODO: implement constriction for above
        Collections.sort(ec.eventList);
        ec.adapter.notifyDataSetChanged();
    }

    //runs without a timer by reposting this handler at the end of the runnable
    private synchronized void logTimePassed() {
        long nanos = System.nanoTime() - startTime;
       /* int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;*/

        Log.d(TAG, "Time passed in ms: " + nanos / 1000000);

    }
//==================================================================================================

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        diaryListener = (DiaryFragmentListener) context;
        Bundle args = getArguments();
        setHasOptionsMenu(true);
    }

    //p. 121 Android Essentials
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //    outState.putParcelableArrayList("ec.eventList", new ArrayList<Event>(ec.eventList));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        setUpMenu(view);

        //this is used to set date after swipe
        Bundle b = this.getArguments();
        if (b != null) {
            changeToDate((LocalDate) b.getSerializable(SWIPING_TO_DATE));
        } else {
            changeToDate(LocalDate.now());
        }

        //starts as invisible appBarLayout but when user marks something this pops up
        tabsLayoutSwitcher = (ViewSwitcher) view.findViewById(R.id.tabLayoutSwitcher);
        ec = new EventsContainer(this);
        ec.setUpEventButtons(view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.events_layout);
        ec.initiateRecyclerView(recyclerView, this.getContext());


        fillEventListWithDatabase(currentDate);
        if (savedInstanceState == null || !savedInstanceState.containsKey("ec.eventList")) {
            //populate array, this will be added to when button is pressed
            //===================================================================
            //  populateList();


            //=====================================================
        } else { //behövs denna eller räcker det med onRestoreInstanceState?
            //   ec.eventList = savedInstanceState.getParcelableArrayList("ec.eventList");
        }

        //=====================TIMER================================================================
        Log.d(TAG, "BEFORE quiting onCreateView");
        logTimePassed();
        //==========================================================================================
        return view;
    }

    private void setUpMenu(View view) {
        //cant come up with better solution for gaining access to toolbar buttons that lie on
        // main_activity.xml
        dateView = (TextView) view.findViewById(R.id.diaryDateView);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerInDiary();
            }
        });
        ImageButton templateBtn = (ImageButton) view.findViewById(R.id.template_btn);
        templateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryListener.startTemplateFragment(currentDate);
            }
        });
        ImageButton infoBtn = (ImageButton) view.findViewById(R.id.info_dairy_btn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityInfoContent.class);
                intent.putExtra(LAYOUT_RESOURCE, R.layout.info_diary);
                intent.putExtra(TITLE_STRING, "Diary");
                startActivity(intent);
            }
        });

        ImageButton toTemplateBtn = (ImageButton) view.findViewById(R.id.to_template_btn);
        toTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEventsTemplateAdder(retrieveMarkedEvents());
            }
        });
        ImageButton copyBtn = (ImageButton) view.findViewById(R.id.copy_btn);
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventsTemplate et = new EventsTemplate(retrieveMarkedEvents(), "");
                EventsTemplateAdapter.startLoadEventsTemplate(et, getActivity());
            }
        });
        ImageButton cancelBtn = (ImageButton) view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = eventsMarked.size() - 1; i >= 0; i--) {
                    View itemView = ec.getItemView(eventsMarked.get(i));
                    if (itemView != null) {
                        removeEventsMarked(itemView, eventsMarked.get(i));
                    }
                }
            }
        });
    }

    private void doEventsTemplateAdder(List<Event> events) {
        Intent intent = new Intent(getActivity(), SaveEventsTemplateActivity.class);
        //Gson gson = new Gson();
        //String objAsJSON = gson.toJson(events);
        intent.putExtra(LIST_OF_EVENTS, (Serializable) events);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ec.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void executeNewEvent(int requestCode, Intent data) {
        DBHandler dbHandler = new DBHandler(getContext());
        if (data.hasExtra(RETURN_EVENT_SERIALIZABLE)) {
            Event event = (Event) data.getSerializableExtra(RETURN_EVENT_SERIALIZABLE);
            dbHandler.addEvent(event);
            addEventToList(event);
        }
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

    /*This is needed since onClick otherwise goes to parent Activity*/
    @Override
    public void onClick(View v) {
        ec.doOnClick(v);
    }

    public void newMealActivity(View view) {
        Intent intent = new Intent(getActivity(), MealActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_MEAL);
    }

    public void newOtherActivity(View v) {
        Intent intent = new Intent(getActivity(), OtherActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_OTHER);
    }

    public void newExerciseActivity(View v) {
        Intent intent = new Intent(getActivity(), ExerciseActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_EXERCISE);
    }

    public void newBmActivity(View v) {
        Intent intent = new Intent(getActivity(), BmActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_BM);
    }

    public void newScoreItem(View view) {
        Intent intent = new Intent(getActivity(), RatingActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_RATING);
    }

    @Override
    public void updateTagsInListOfEventsAfterTagTemplateChange() {
        fillEventListWithDatabase(currentDate);
    }

    private void addDateToNewEventIntent(Intent intent) {
        intent.putExtra(DATE_TO_START_NEW_EVENTACTIVITY, (Serializable) currentDate);
    }

    /*
    Obs krasch om man klickar för snabbt, i alla fall vid ec.adapter.notifyItemRemoved!
     */
    @Override
    public void onItemClicked(View v, int position) {
        final Event pressedEvent = ec.eventList.get(position);
        Log.d("Debug", "inside fragment, item was clicked");
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
        final Event pressedEvent = ec.eventList.get(position);
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
                        //2 remove event from ec.eventList
                        ec.eventList.remove(pressedEvent);
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
        dbHandler.changeEvent(e);
    }

    private void removeBreakFromevent(Event e) {
        e.setHasBreak(false);
        DBHandler dbHandler = new DBHandler(getContext());
        dbHandler.changeEvent(e);
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
            changeToTabbedMenu();

        }
    }

    private List<Event> retrieveMarkedEvents() {
        List<Event> eventsToSend = new ArrayList<>();
        for (int i : eventsMarked) {
            eventsToSend.add(ec.eventList.get(i));
        }
        //

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


    //user requests to change event
    public void changeEventActivity(Event event, Class activityClass, int valueToReturn, int
            posInList) {
        Intent intent = new Intent(getActivity(), activityClass);
        intent.putExtra(EVENT_TO_CHANGE, event);
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
        ec.eventList.clear();
        List<Event> sortedEvents = dbHandler.getAllEventsMinusEventsTemplateSortedFromDay(theDate);
        ec.eventList.addAll(sortedEvents);
        ec.adapter.notifyDataSetChanged();
        //place focus at top (otherwise user has to scroll up, which make time sizeable time for
        // large imports).
        ec.recyclerView.scrollToPosition(ec.eventList.size() - 1);
    }

    public List<Event> getEvents() {
        return ec.eventList;
    }

    public void changeToDate(LocalDate ld) {
        currentDate = ld;
        setDateView(ld);
    }

    private void setDateView(LocalDate ld) {
        dateView.setText(ld.getDayOfWeek().toString() + " " + ld.getDayOfMonth() + " " + ld
                .getMonth().toString() + ", " + Integer.toString(ld.getYear()));
    }

    public void startDatePickerInDiary() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setContext(getContext());
        newFragment.setListener(this);
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //month datepicker +1 == LocalDate.Month
        LocalDate d = LocalDate.of(year, month + 1, dayOfMonth);
        if (d != currentDate) {
            diaryListener.changeDate(d);
        }
    }
}
