package com.johanlund.diary;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
import com.johanlund.event_activities.BmActivity;
import com.johanlund.event_activities.ExerciseActivity;
import com.johanlund.event_activities.MealActivity;
import com.johanlund.event_activities.OtherActivity;
import com.johanlund.event_activities.RatingActivity;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.info.ActivityInfoContent;
import com.johanlund.model.EventsTemplate;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.util.List;

import static com.johanlund.constants.Constants.DATE_TO_START_NEW_EVENTACTIVITY;
import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.LIST_OF_EVENTS;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.johanlund.constants.Constants.SWIPING_TO_DATE;
import static com.johanlund.constants.Constants.TITLE_STRING;
import static com.johanlund.diary.EventsContainer.NEW_BM;
import static com.johanlund.diary.EventsContainer.NEW_EXERCISE;
import static com.johanlund.diary.EventsContainer.NEW_MEAL;
import static com.johanlund.diary.EventsContainer.NEW_OTHER;
import static com.johanlund.diary.EventsContainer.NEW_RATING;

/**
 * This class uses an adapter that is using DiaryFragment
 */
public class DiaryContainerFragment extends Fragment implements DiaryFragment.DiaryFragmentUser, EventButtonsContainer
        .EventButtonContainerUser, DatePickerDialog.OnDateSetListener {
    public interface DiaryContainerListener {
        void startTemplateFragment();
    }


    //preferably even number, variables used for setting correct date after swipe
    private static int MAX_SLIDES = 1000;
    private static int START_POS_VIEWPAGER = MAX_SLIDES / 2;
    ViewPager pager;
    DiarySlidePagerAdapter adapter;
    LocalDate currentDate;
    TextView dateView;
    private EventButtonsContainer buttons;
    private DiaryContainerListener listener;
    //switcher tab and it's tabs
    ViewSwitcher tabsLayoutSwitcher;
    public DiaryContainerFragment() {
        // Required empty public constructor
    }

    /**
     * Which Date should diary start at? It depends.
     * <p>
     * If a date is choosen from the Calendar, start at that date.
     * <p>
     * If a new event (the date can be changed inside EventActivity by user) has been created and
     * Done has been pressed, then the diary should start at that date. (This is done in
     * onActivityResult).
     * <p>
     * When importing diary, you want the date to be at the last used day in database.
     * <p>
     * On resuming app from inactivity, or turning around phone, or pressing back from
     * EventActivity etc => start at the same date you were at latest.
     * <p>
     * First time using app, or if none of the above works, start at the date of the current
     * (real) day.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_container, container, false);

        //starts as invisible appBarLayout but when user marks something this pops up
        tabsLayoutSwitcher = (ViewSwitcher) view.findViewById(R.id.tabLayoutSwitcher);
        buttons = new EventButtonsContainer(this);
        buttons.setUpEventButtons(view);
        setUpMenu(view);

        //App is starting from scratch (diary is empty)
        if (diaryIsEmpty()) {
            changeToDate(LocalDate.now());
        }
        //App is starting at the time of the date with the last events.
        else {
            changeToDate(getDateOfLastEventInDiary());
        }
        adapter = new DiarySlidePagerAdapter(getChildFragmentManager());
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(MAX_SLIDES / 2);
        return view;
    }
    private boolean diaryIsEmpty(){
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
    /*
        When user markes list items for template or copying
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
                DiaryFragment currentDiary = (DiaryFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
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

    /*This is needed since onClick otherwise goes to parent Activity*/
    @Override
    public void onClick(View v) {
        buttons.doOnClick(v);
    }

    @Override
    public void executeNewEvent(int requestCode, Intent data) {
        DBHandler dbHandler = new DBHandler(getContext());
        if (data.hasExtra(RETURN_EVENT_SERIALIZABLE)) {
            Event event = (Event) data.getSerializableExtra(RETURN_EVENT_SERIALIZABLE);
            dbHandler.addEvent(event);
            getCurrentDiary().addEventToList(event);
        }
    }
    @Override
    public void newMealActivity(View view) {
        Intent intent = new Intent(getActivity(), MealActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_MEAL);
    }
    @Override
    public void newOtherActivity(View v) {
        Intent intent = new Intent(getActivity(), OtherActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_OTHER);
    }
    @Override
    public void newExerciseActivity(View v) {
        Intent intent = new Intent(getActivity(), ExerciseActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_EXERCISE);
    }
    @Override
    public void newBmActivity(View v) {
        Intent intent = new Intent(getActivity(), BmActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_BM);
    }
    @Override
    public void newScoreItem(View view) {
        Intent intent = new Intent(getActivity(), RatingActivity.class);
        addDateToNewEventIntent(intent);
        startActivityForResult(intent, NEW_RATING);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        buttons.onActivityResult(requestCode, resultCode, data);
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
    private void changeDiaryToDate(LocalDate ld){
        changeToDate(ld);
        //is this working?
        adapter = new DiarySlidePagerAdapter(getFragmentManager());
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
            changeDiaryToDate(d);
        }
    }
    private DiaryFragment getCurrentDiary(){
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
            LocalDate nextDate = currentDate.plusDays(position - START_POS_VIEWPAGER);
            args.putSerializable(SWIPING_TO_DATE, nextDate);
            diaryFragment.setArguments(args);

            //change currentDate to the new date
            changeToDate(nextDate);
            return diaryFragment;
        }

        @Override
        public int getCount() {
            return MAX_SLIDES;
        }

    }
}
