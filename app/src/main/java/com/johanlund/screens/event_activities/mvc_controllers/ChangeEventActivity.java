package com.johanlund.screens.event_activities.mvc_controllers;

import android.content.Intent;
import android.os.Bundle;

import com.johanlund.base_classes.Event;
import com.johanlund.date_time.DateTimeFormat;

import org.threeten.bp.LocalDateTime;

import static com.johanlund.constants.Constants.CHANGED_EVENT;
import static com.johanlund.constants.Constants.EVENT_POSITION;
import static com.johanlund.constants.Constants.ID_OF_EVENT;
import static com.johanlund.constants.Constants.ID_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.johanlund.constants.Constants.STARTING_DATE_TIME;

public class ChangeEventActivity extends TagEventActivity {
    //variables used for changing events.
    //Connected to isChangingEvent. The variable is later used
    // in database to know which event to be changed.
    protected long eventId = -1;

    //position in eventsOfDay (in DiaryFragment) for changing event
    protected int posOfEvent = -1;

    //this is solely used to see if a ChangingEvent has changed its time during this interaction
    //it should not be used in a context of a new event
    LocalDateTime startingTimeBeforeChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ID_OF_EVENT)) {
                eventId = (long) savedInstanceState.get
                        (ID_OF_EVENT);
            }
            if (savedInstanceState.containsKey(EVENT_POSITION)) {
                posOfEvent = (int) savedInstanceState.get
                        (EVENT_POSITION);
            }
            if (savedInstanceState.containsKey(STARTING_DATE_TIME)) {
                startingTimeBeforeChanges = DateTimeFormat.fromSqLiteFormat((String)
                        savedInstanceState.get
                        (STARTING_DATE_TIME));
            }

        } else if (intent.hasExtra(ID_OF_EVENT) && intent.hasExtra(EVENT_POSITION)) {
            eventId = intent.getLongExtra(ID_OF_EVENT, -1);
            posOfEvent = intent.getIntExtra(EVENT_POSITION, -1);
            eventToBind = dao.fetchEventById(eventId);
            startingTimeBeforeChanges = eventToBind.getTime();
        }
        initMvcView(eventToBind);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ID_OF_EVENT, eventId);
        outState.putInt(EVENT_POSITION, posOfEvent);
        outState.putString(STARTING_DATE_TIME, DateTimeFormat.toSqLiteFormat
                (startingTimeBeforeChanges));
    }

    @Override
    public void completeSession(Event e) {
        int type = e.getType();
        if (dao.eventTypeAtSameTimeAlreadyExists(type, e.getTime()) &&
                changingEventHasDifferentDateTimeThanStart(e)) {
            mViewMVC.showEventAlreadyExistsPopUp(type);
        } else {
            returnEventAndFinish(e);
        }
    }

    @Override
    protected void returnEvent(Event event) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RETURN_EVENT_SERIALIZABLE, event);
        bundle.putLong(ID_OF_EVENT_RETURNED, eventId);
        bundle.putInt(POS_OF_EVENT_RETURNED, posOfEvent);
        bundle.putBoolean(CHANGED_EVENT, true);
        Intent data = new Intent();
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
    }

    /**
     * Prerequisite: startingTimeBeforeChanges must be initatied with a ldt =>
     * this method should only be called if we are in a ChangingEventActivity
     *
     * @return
     */
    private boolean changingEventHasDifferentDateTimeThanStart(Event e) {
        return !e.getTime().isEqual(startingTimeBeforeChanges);
    }
}
