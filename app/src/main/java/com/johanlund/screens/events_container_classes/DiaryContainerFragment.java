package com.johanlund.screens.events_container_classes;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.date_time.DatePickerFragment;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.model.EventsTemplate;
import com.johanlund.screens.event_activities.mvc_controllers.EventActivity;
import com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvc;
import com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvcImpl;
import com.johanlund.screens.events_templates.common.EventsTemplateAdapter;
import com.johanlund.screens.info.ActivityInfoContent;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.johanlund.constants.Constants.DATE_TO_START_NEW_EVENTACTIVITY;
import static com.johanlund.constants.Constants.EVENT_TYPE;
import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.LIST_OF_EVENTS;
import static com.johanlund.constants.Constants.LOCALDATE;
import static com.johanlund.constants.Constants.NEW_EVENT;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.johanlund.constants.Constants.SWIPING_TO_DATE;
import static com.johanlund.constants.Constants.TITLE_STRING;
import static com.johanlund.screens.events_container_classes.common.EventsContainer.NEW_EXERCISE;

/**
 * This class uses an adapter that is using DiaryFragment
 * <p>
 * Various handling of dates makes this class very intertwined with DiaryDrawer, and a bit
 * complicated.
 * Which Date should diary start at? It depends. And that makes code more complicated.
 * <p>
 * If a date is choosen from the Calendar, start at that date.
 * => uses restartDiaryAdapterOnDate
 * <p>
 * If a new event (the date can be changed inside EventActivity by user) has been created and
 * Done has been pressed, then the diary should start at that date.
 * =>(This is done in executeNewEvent via mButtonsViewMvc.onActivityResult).
 * <p>
 * On loading from TemplateFragment, diary should start at that date.
 * => not done right now...
 *
 * When importing diary, you want the date to be at the last used day in database.
 * => this is done in in DrawerActivity.startDiaryAtLastDate. OK!
 * <p>
 * On resuming app from inactivity, or turning around phone, or pressing back from
 * EventActivity etc, diary should be back at the same date you were at latest.
 * => savingInstanceState in DrawerActivity
 * <p>
 * <p>
 * On pressing Back from TemplateFragment. Here (since TemplateFragment is a Fragment)
 * onCreate in DrawerActivity is not called. Another solution is needed.
 * => OK! oopBackStack is used in DrawerActivity.
 * <p>
 * First time using app, or if none of the above works, start at the date of the current
 * (real) day.
 * => if-statement in this onCreateView
 */
public class DiaryContainerFragment extends Fragment implements DiaryFragment.DiaryFragmentUser,
        EventButtonsViewMvc.Listener, DatePickerDialog.OnDateSetListener {
    public interface DiaryContainerListener {
        void startTemplateFragment();

        void restartContainerDiary(LocalDate ld);
    }


    //preferably even number, variables used for setting correct date after swipe
    private static int MAX_SLIDES = 1000;
    private static int START_POS_VIEWPAGER = MAX_SLIDES / 2;
    ViewPager pager;
    DiarySlidePagerAdapter adapter;
    //startDate should be initialized only once, and never change. Adapter (positioning) uses it.
    LocalDate startDate;
    LocalDate currentDate;
    TextView dateView;
    private EventButtonsViewMvcImpl mButtonsViewMvc;
    private DiaryContainerListener listener;
    //switcher tab and it's tabs
    ViewSwitcher tabsLayoutSwitcher;

    public DiaryContainerFragment() {
        // Required empty public constructor
    }

    //I need this since pressing back button from TemplateFragment
    @Override
    public void onResume() {
        super.onResume();
        setDateView(currentDate);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_container, container, false);

        //starts as invisible appBarLayout but when user marks something this pops up
        tabsLayoutSwitcher = (ViewSwitcher) view.findViewById(R.id.tabLayoutSwitcher);
        mButtonsViewMvc = new EventButtonsViewMvcImpl(inflater, (ViewGroup) view.findViewById(R.id.buttons));
        setUpMenu(view);

        Bundle args = getArguments();
        //if started from Activity...
        if (args != null && args.getSerializable(LOCALDATE) != null) {
            startDate = (LocalDate) args.getSerializable(LOCALDATE);
            changeToDate(startDate);
        }
        //App is starting from scratch (diary is empty)
        else if (startDate == null) {
            startDate = (LocalDate.now());
            changeToDate(startDate);
        }

        //else, we come back to original place, this is good on backpress for example
        adapter = new DiarySlidePagerAdapter(getChildFragmentManager());
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(MAX_SLIDES / 2);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                //this one returns null for DiaryFragments currentDate
                changeToDate(((DiaryFragment) adapter.getItem(position)).getCurrentDate());
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        mButtonsViewMvc.registerListener(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        mButtonsViewMvc.unregisterListener(this);
    }

    private boolean diaryIsEmpty() {
        final DBHandler dbHandler = new DBHandler(getContext());
        return dbHandler.diaryIsEmpty();
    }

    private LocalDate getDateOfLastEventInDiary() {
        final DBHandler dbHandler = new DBHandler(getContext());
        return dbHandler.getDateOfLastEvent();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DiaryContainerListener) context;
        setHasOptionsMenu(true);
    }

    /**
     * Menu
     * /*
     * When user markes list items for template or copying
     */
    public void changeToMarkedMenu() {
        tabsLayoutSwitcher.showNext();
        //callBack.viewPager do something here to stop swipe
    }

    /*
        When user unmarkes list items
     */
    public void changeToTabbedMenu() {
        tabsLayoutSwitcher.showNext();

    }

    private void setUpMenu(View view) {
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
                listener.startTemplateFragment();
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
                doEventsTemplateAdder(getCurrentDiary().retrieveMarkedEvents());
            }
        });
        ImageButton copyBtn = (ImageButton) view.findViewById(R.id.copy_btn);
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryFragment currentDiary = (DiaryFragment) adapter.instantiateItem(pager, pager
                        .getCurrentItem());
                EventsTemplate et = new EventsTemplate(currentDiary.retrieveMarkedEvents(), "");
                EventsTemplateAdapter.startLoadEventsTemplate(et, getActivity());
            }
        });
        ImageButton cancelBtn = (ImageButton) view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentDiary().unmarkAllEvents();
                changeToTabbedMenu();
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
    public void executeNewEvent(int requestCode, Intent data) {
        DBHandler dbHandler = new DBHandler(getContext());
        if (data.hasExtra(RETURN_EVENT_SERIALIZABLE)) {
            Event event = (Event) data.getSerializableExtra(RETURN_EVENT_SERIALIZABLE);
            dbHandler.addEvent(event);
            getCurrentDiary().addEventToList(event);

            //it can be that the new event should be created at another date.
            LocalDate dateForEventCreation = event.getTime().toLocalDate();
            if (!dateForEventCreation.equals(currentDate)) {
                listener.restartContainerDiary(dateForEventCreation);
            }
        }
    }

    @Override
    public void newEventActivity(int eventType) {
        Intent intent = new Intent(getActivity(), EventActivity.class);
        intent.putExtra(EVENT_TYPE, eventType);
        intent.putExtra(DATE_TO_START_NEW_EVENTACTIVITY, (Serializable) currentDate);
        startActivityForResult(intent, NEW_EXERCISE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data.hasExtra(NEW_EVENT)) {
            executeNewEvent(requestCode, data);
        }

    }

    private void addDateToNewEventIntent(Intent intent) {
        intent.putExtra(DATE_TO_START_NEW_EVENTACTIVITY, (Serializable) currentDate);
    }

    //just testing
    public LocalDate extractDateFromDiary() {
        return currentDate;
    }

    //date
    void changeToDate(LocalDate ld) {
        currentDate = ld;
        setDateView(ld);
    }

    private void restartDiaryAdapterOnDate(LocalDate ld) {
        listener.restartContainerDiary(ld);
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
            restartDiaryAdapterOnDate(d);
        }
    }

    private DiaryFragment getCurrentDiary() {
        return (DiaryFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
    }

    //Internal adapter class
    private class DiarySlidePagerAdapter extends FragmentStatePagerAdapter {
        public DiarySlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * @param position is used to set correct date also after swipe
         * @return
         */
        private DiaryFragment diaryFragment;

        @Override
        public Fragment getItem(int position) {
            diaryFragment = new DiaryFragment();
            Bundle args = new Bundle();
            LocalDate nextDate = startDate.plusDays(position - START_POS_VIEWPAGER);
            args.putSerializable(SWIPING_TO_DATE, nextDate);
            diaryFragment.setArguments(args);
            return diaryFragment;
        }

        @Override
        public int getCount() {
            return MAX_SLIDES;
        }

    }
}
