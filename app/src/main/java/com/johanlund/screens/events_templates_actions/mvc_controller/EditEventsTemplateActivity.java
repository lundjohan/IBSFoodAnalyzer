package com.johanlund.screens.events_templates_actions.mvc_controller;

import android.content.Intent;
import android.os.Bundle;

import com.johanlund.database.DBHandler;
import com.johanlund.model.EventsTemplate;
import com.johanlund.screens.events_templates_actions.mvc_views.EditEventsTemplateViewMvc;

import static com.johanlund.constants.Constants.EVENTSTEMPLATE_TO_CHANGE;
import static com.johanlund.constants.Constants.ID_OF_EVENTSTEMPLATE;

public class EditEventsTemplateActivity extends EventsTemplateActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mViewMVC = new EditEventsTemplateViewMvc(getLayoutInflater(), null);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(EVENTSTEMPLATE_TO_CHANGE) && intent.hasExtra(ID_OF_EVENTSTEMPLATE)) {
            EventsTemplate etToBind = (EventsTemplate) intent.getSerializableExtra
                    (EVENTSTEMPLATE_TO_CHANGE);
            idEventsTemplate = intent.getLongExtra(ID_OF_EVENTSTEMPLATE, -1);
            initMvcView(etToBind);
        }
        //else throw exc
    }
    @Override
    protected void saveToDB(EventsTemplate changedET) {
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.editEventsTemplate(idEventsTemplate, changedET);
    }

    /**
     * Not applicable
     * @param et
     */
    @Override
    protected void saveToDiary(EventsTemplate et) {

    }
}
