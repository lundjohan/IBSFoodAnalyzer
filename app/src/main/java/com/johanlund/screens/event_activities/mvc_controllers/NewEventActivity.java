package com.johanlund.screens.event_activities.mvc_controllers;

import android.content.Intent;
import android.os.Bundle;

import com.johanlund.base_classes.Event;
import com.johanlund.factories.DaggerEventFactoryComponent;
import com.johanlund.factories.EventFactory;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import javax.inject.Inject;

import static com.johanlund.constants.Constants.EVENT_TYPE;
import static com.johanlund.constants.Constants.NEW_EVENT;
import static com.johanlund.constants.Constants.NEW_EVENT_DATE;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;

public class NewEventActivity extends TagEventActivity {
    @Inject
    EventFactory eventFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (!intent.hasExtra(NEW_EVENT_DATE) || !intent.hasExtra(EVENT_TYPE)) {
            finish(); //something is wrong and better go back in hierarchy
        }
        LocalDate ld = (LocalDate) intent.getSerializableExtra(NEW_EVENT_DATE);
        int eventType = intent.getIntExtra(EVENT_TYPE, -1);

        //if savedInstance exists eventToBind will be created from that.
        if (savedInstanceState == null) {
            DaggerEventFactoryComponent.create().inject(this);
            eventToBind = eventFactory.makeDummyEventWithTime(LocalDateTime.of(ld, LocalTime.now()),
                    eventType);
        }
        initMvcView(eventToBind);
    }

    @Override
    public void completeSession(Event e) {
        int type = e.getType();
        if (dao.eventTypeAtSameTimeAlreadyExists(type, e.getTime())) {
            mViewMVC.showEventAlreadyExistsPopUp(type);
        } else {
            returnEventAndFinish(e);
        }
    }

    @Override
    protected void returnEvent(Event event) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RETURN_EVENT_SERIALIZABLE, event);
        Intent data = new Intent();
        bundle.putBoolean(NEW_EVENT, true);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
    }
}
