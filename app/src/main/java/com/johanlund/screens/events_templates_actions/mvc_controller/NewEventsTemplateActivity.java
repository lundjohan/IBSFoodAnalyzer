package com.johanlund.screens.events_templates_actions.mvc_controller;

import android.content.Intent;
import android.os.Bundle;

import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.model.EventsTemplate;
import com.johanlund.screens.events_templates_actions.mvc_views.NewEventsTemplateViewMvc;

import java.util.List;

import static com.johanlund.constants.Constants.LIST_OF_EVENTS;

public class NewEventsTemplateActivity extends EventsTemplateActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mViewMVC = new NewEventsTemplateViewMvc(getLayoutInflater(), null);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(LIST_OF_EVENTS)) {
            List<Event> events = (List<Event>) intent.getSerializableExtra(LIST_OF_EVENTS);
            EventsTemplate etToBind = new EventsTemplate(events, "");
            initMvcView(etToBind);
        }
        //else throw exc
    }

    @Override
    protected void saveToDB(EventsTemplate et) {
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.addEventsTemplate(et);
    }

    /**
     * Not applicable
     * @param et
     */
    @Override
    protected void saveToDiary(EventsTemplate et) {

    }
}
