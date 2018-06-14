package com.johanlund.screens.event_activities.mvc_controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.johanlund.base_classes.Event;
import com.johanlund.date_time.DatePickerFragment;
import com.johanlund.factories.EventFactory;
import com.johanlund.factories.EventFactoryImpl;
import com.johanlund.model.EventManager;
import com.johanlund.model.TagType;
import com.johanlund.screens.event_activities.factories.EventViewFactory;
import com.johanlund.screens.event_activities.factories.EventViewFactoryImpl;
import com.johanlund.screens.event_activities.mvcviews.EventViewMvc;
import com.johanlund.screens.info.ActivityInfoContent;
import com.johanlund.screens.tag_adder.TagAdderActivity;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.io.Serializable;
import java.util.Calendar;

import static com.johanlund.constants.Constants.CHANGED_EVENT;
import static com.johanlund.constants.Constants.DATE_TO_START_NEW_EVENTACTIVITY;
import static com.johanlund.constants.Constants.EVENT_POSITION;
import static com.johanlund.constants.Constants.EVENT_TO_CHANGE;
import static com.johanlund.constants.Constants.EVENT_TYPE;
import static com.johanlund.constants.Constants.ID_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.NEW_EVENT;
import static com.johanlund.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.johanlund.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.johanlund.constants.Constants.TAGS_TO_ADD;
import static com.johanlund.constants.Constants.TITLE_STRING;

//this will replace EventActivity
public class EventActivity extends AppCompatActivity implements EventViewMvc
        .EventActivityViewMvcListener, DatePickerDialog.OnDateSetListener, TimePickerDialog
        .OnTimeSetListener {
    private EventViewMvc mViewMVC;
    private EventManager eventManager;
    //variables used for changing events.
    //Connected to isChangingEvent. The variable is later used
    // in database to know which event to be changed.
    protected long eventId = -1;

    //position in eventsOfDay (in DiaryFragment) for changing event
    protected int posOfEvent = -1;

    //this is solely used to see if a ChangingEvent has changed its time during this interaction
    //it should not be used in a context of a new event
    LocalDateTime changingEventStartingDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();


        //is the event meant to be changed (as opposition to new event to be created)?
        int eventType = -1;


        //get the event type
        if (savedInstanceState != null && savedInstanceState.containsKey(EVENT_TYPE)) {
            eventType = ((int) savedInstanceState.get(EVENT_TYPE));
        } else if (intent.hasExtra(EVENT_TYPE)) {
            eventType = intent.getIntExtra(EVENT_TYPE, -1);
        }

        eventManager = new EventManager(getApplicationContext());
        Event eventToBindToView = null;
        EventFactory eventFactory = new EventFactoryImpl();
        if (savedInstanceState != null) {
            LocalDate ld = null;
            LocalTime lt = null;

            if (savedInstanceState.containsKey("idOfEvent")) {
                eventId = (int) savedInstanceState.get
                        ("idOfEvent");
            }
            if (savedInstanceState.containsKey(EVENT_POSITION)) {
                posOfEvent = (int) savedInstanceState.get
                        (EVENT_POSITION);
            }
            if (savedInstanceState.containsKey("localDateStr")) {
                ld = LocalDate.parse((CharSequence) savedInstanceState.get
                        ("localDateStr"));
            }
            if (savedInstanceState.containsKey("localTimeStr")) {
                lt = LocalTime.parse((CharSequence) savedInstanceState.get
                        ("localTimeStr"));
            }
            eventToBindToView = eventFactory.makeDummyEventWithTime(LocalDateTime.of(ld, lt), eventType);
        } else if (intent.hasExtra(EVENT_TO_CHANGE)) {
            eventToBindToView = (Event) intent.getSerializableExtra(EVENT_TO_CHANGE);
            eventId = eventManager.getEventIdOutsideEventsTemplate(eventToBindToView);
            posOfEvent = intent.getIntExtra(EVENT_POSITION, -1);
            changingEventStartingDateTime = eventToBindToView.getTime();
        }

        //is the event created from scratch, inside diary, then get the start date from open day
        else if (intent.hasExtra(DATE_TO_START_NEW_EVENTACTIVITY)) {
            LocalDate ld = (LocalDate) intent.getSerializableExtra(DATE_TO_START_NEW_EVENTACTIVITY);
            eventToBindToView = eventFactory.makeDummyEventWithTime(LocalDateTime.of(ld, LocalTime.now()),
                    eventType);
        }

        // Instantiate MVC view associated with this activity
        EventViewFactory viewFactory = new EventViewFactoryImpl();
        mViewMVC = viewFactory.make(LayoutInflater.from(this), null, eventType);
        mViewMVC.setListener(this);

        // Set the root view of the associated MVC view as the content of this activity
        setContentView(mViewMVC.getRootView());

        mViewMVC.bindEventToView(eventToBindToView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return mViewMVC.createOptionsMenu(menu, getMenuInflater());
    }

    @Override
    public void completeSession(Event e) {
        int type = e.getType();

        /*condition will happen if
         1. an event with this eventype already exists and it is not a
          changing event
         2. an event with this eventype already exists, AND is a changing event, datetime has
           been changed to other datetime than start.
        */
        if (eventManager.eventTypeAtSameTimeAlreadyExists(type, e.getTime()) &&
                (!isChangingEvent() ||
                changingEventHasDifferentDateTimeThanStart(e))) {
            showEventAlreadyExistsPopUp(type);
        } else {
            returnEvent(e);
            finish();
        }
    }

    @Override
    public void showInfo(String titleStr, int infoLayout) {
        //move below to controller
        Intent intent = new Intent(this, ActivityInfoContent.class);
        intent.putExtra(LAYOUT_RESOURCE, infoLayout);
        intent.putExtra(TITLE_STRING, titleStr);
        startActivity(intent);
    }

    protected void returnEvent(Event event) {
        if (isChangingEvent()) {
            returnChangedEvent(event, RETURN_EVENT_SERIALIZABLE, eventId, posOfEvent);
        } else {
            returnNewEvent(event, RETURN_EVENT_SERIALIZABLE);
        }
    }

    private void returnChangedEvent(Serializable serializable, String putExtraString, long
            eventId, int
            posInList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(putExtraString, serializable);
        bundle.putLong(ID_OF_EVENT_RETURNED, eventId);
        bundle.putInt(POS_OF_EVENT_RETURNED, posInList);
        bundle.putBoolean(CHANGED_EVENT, true);
        Intent data = new Intent();
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
    }

    private void returnNewEvent(Serializable serializable, String putExtraString) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(putExtraString, serializable);
        Intent data = new Intent();
        bundle.putBoolean(NEW_EVENT, true);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
    }

    protected boolean isChangingEvent() {
        return eventId > -1;
    }

    /**
     * Prerequisite: changingEventStartingDateTime must be initatied with a ldt =>
     * this method should only be called if we are in a ChangingEventActivity
     *
     * @return
     */
    private boolean changingEventHasDifferentDateTimeThanStart(Event e) {
        if (!getIntent().hasExtra(EVENT_TO_CHANGE)) {
            //could throw exception, but feels a bit unecessary. Just use this method with caution!
        }
        return !e.getTime().isEqual(changingEventStartingDateTime);
    }

    //this should be in view
    private void showEventAlreadyExistsPopUp(int eventType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false).
                setTitle("Event already exists").
                setMessage("A(n) " + Event.getEventTypeStr(eventType) + " at this date and time " +
                        "already exists in diary. Change the date or time of the event.").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //don't do anything
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton
                .getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    //data coming back from TagAdder
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode != TAGS_TO_ADD) {
            return;
        }
        if (data.hasExtra(RETURN_TAG_TEMPLATE_SERIALIZABLE)) {
            TagType tagType = (TagType) data.getExtras().getSerializable
                    (RETURN_TAG_TEMPLATE_SERIALIZABLE);
            mViewMVC.bindAddedTagToView(tagType.get_tagname());
        }
    }

    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, TAGS_TO_ADD);
    }


    /**
     * DATE AND TIME PICKER
     * <p>
     * Would have been cleaner if they were entirely in MvcView,
     * however TimePicker class is dependent on Context which MvcView should be agnostic about.
     * <p>
     * TODO Later note. I think these classes still should be placed in View class, fix
     * transmission of context.
     */
    @Override
    public void startTimePicker(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void startDatePicker(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setContext(this);
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //month datepicker +1 == LocalDate.Month
        LocalDate ld = LocalDate.of(year, month + 1, dayOfMonth);
        mViewMVC.setDateView(ld);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        LocalTime lt = LocalTime.of(hourOfDay, minute);
        mViewMVC.setTimeView(lt);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public LocalTime lt;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current timeView as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //see http://stackoverflow
            // .com/questions/11527051/get-date-from-datepicker-using-dialogfragment accepted
            // answer.
            ((TimePickerDialog.OnTimeSetListener) getActivity()).onTimeSet(view, hourOfDay, minute);

        }

        public LocalTime getTime() {
            return lt;
        }
    }


}
