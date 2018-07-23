package com.johanlund.screens.event_activities.mvc_controllers;

import android.content.Intent;
import android.os.Bundle;

import com.johanlund.date_time.DateTimeFormat;

import static com.johanlund.constants.Constants.CHANGED_EVENT;
import static com.johanlund.constants.Constants.EVENT_POSITION;
import static com.johanlund.constants.Constants.EVENT_TO_CHANGE;
import static com.johanlund.constants.Constants.ID_OF_EVENT;
import static com.johanlund.constants.Constants.ID_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.johanlund.constants.Constants.STARTING_DATE_TIME;
import com.johanlund.base_classes.Event;

public class ChangeEventInsideEtActivity extends TagEventActivity{
    protected int posOfEvent = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EVENT_POSITION)) {
                posOfEvent = (int) savedInstanceState.get
                        (EVENT_POSITION);
            }

        } else if (intent.hasExtra(EVENT_TO_CHANGE) && intent.hasExtra(EVENT_POSITION)) {
            eventToBind = (Event)intent.getSerializableExtra(EVENT_TO_CHANGE);
            posOfEvent = intent.getIntExtra(EVENT_POSITION, -1);
        }
        initMvcView(eventToBind);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       //Todo  outState.putLong(EVENT, eventId);
        outState.putInt(EVENT_POSITION, posOfEvent);
    }

    @Override
    public void completeSession(Event e) {
            returnEventAndFinish(e);
    }

    @Override
    protected void returnEvent(Event event) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RETURN_EVENT_SERIALIZABLE, event);
        bundle.putInt(POS_OF_EVENT_RETURNED, posOfEvent);
        bundle.putBoolean(CHANGED_EVENT, true);
        Intent data = new Intent();
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
    }
}
