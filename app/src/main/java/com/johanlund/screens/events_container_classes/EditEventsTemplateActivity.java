package com.johanlund.screens.events_container_classes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.model.EventsTemplate;

import java.util.List;

import static com.johanlund.constants.Constants.EVENTSTEMPLATE_TO_CHANGE;
import static com.johanlund.constants.Constants.ID_OF_EVENTSTEMPLATE;

public class EditEventsTemplateActivity extends EventsTemplateActivity {
    TextView nameView;
    EventsTemplate et;
    long id;

    @Override
    protected void saveToDB(EventsTemplate changedET) {
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.editEventsTemplate(id, changedET);
    }

    @Override
    protected void saveToDiary() { }

    @Override
    protected String getTitleStr() {
        return "Edit EventsTemplate";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra(EVENTSTEMPLATE_TO_CHANGE)) {
            et = (EventsTemplate) intent.getSerializableExtra
                    (EVENTSTEMPLATE_TO_CHANGE);
        }
        if (intent.hasExtra(ID_OF_EVENTSTEMPLATE)) {
            id = intent.getLongExtra(ID_OF_EVENTSTEMPLATE, -1);
        }
        super.onCreate(savedInstanceState);
    }

    /*
     * I let this structure stand (for now), it might be a difference of layouts between saving
     * eventtemplate and editing eventstemplate in the future
     */
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_save_events_templates;
    }

    @Override
    protected String getStartingName() {
        return et.getNameOfTemplate();
    }

    @Override
    protected String getEndingName() {
        return nameView.getText().toString();
    }

    @Override
    protected void setUpNameViewIfExisting() {
        nameView = (TextView) findViewById(R.id.template_name);
        nameView.setText(getStartingName());
    }

    @Override
    protected List<Event> getStartingEvents() {

        return et.getEvents();
    }


}
