package com.johanlund.screens.events_templates_actions.mvc_controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.johanlund.base_classes.Event;
import com.johanlund.model.EventModel;
import com.johanlund.model.EventsTemplate;
import com.johanlund.picker_views.DatePickerFragment;
import com.johanlund.screens.events_templates_actions.mvc_views.LoadEventsTemplateViewMvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.johanlund.constants.Constants.EVENTSTEMPLATE_TO_LOAD;
import static com.johanlund.constants.Constants.EVENTS_TO_LOAD;

public class LoadEventsTemplateActivity extends EventsTemplateActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mViewMVC = new LoadEventsTemplateViewMvc(getLayoutInflater(), null);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(EVENTSTEMPLATE_TO_LOAD)) {
            EventsTemplate et = (EventsTemplate) intent.getSerializableExtra
                    (EVENTSTEMPLATE_TO_LOAD);
            initMvcView(et);
        }
        //else{pop-up or error msg declaring something is wrong};
    }

    /**
     * in this class, there should be no save to db.
     *
     * @param et
     */
    @Override
    protected void saveToDB(EventsTemplate et) {
    }

    @Override
    protected void saveToDiary(EventsTemplate et) {
        List<Event> eventsFromTemplate = et.getEvents();
        List<Event> eventsToReturnWithUniqueTypeAndTimes = new ArrayList<>();

        for (Event e : eventsFromTemplate) {
            if (!EventModel.eventTypeAtSameTimeAlreadyExists(e.getType(), e.getTime(),
                    getApplicationContext())) {
                eventsToReturnWithUniqueTypeAndTimes.add(e);
            }
        }
        Intent intent = new Intent();
        intent.putExtra(EVENTS_TO_LOAD, (Serializable) eventsToReturnWithUniqueTypeAndTimes);
        setResult(RESULT_OK, intent);
    }

    /* =============================================================================================
     * DATE PICKER (duplicate from EventActivity. )
     * =============================================================================================
     */
    public void startDatePicker(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setContext(this);
        newFragment.setListener((LoadEventsTemplateViewMvc)mViewMVC);
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
