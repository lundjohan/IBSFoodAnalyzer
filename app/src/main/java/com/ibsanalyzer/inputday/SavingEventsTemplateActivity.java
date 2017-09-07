package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.model.EventsTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.LIST_OF_EVENTS;

public class SavingEventsTemplateActivity extends EventsTemplateActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_save_events_templates;
    }

    @Override
    protected String getStartingName() {
        return "";
    }

    @Override
    protected List<Event> getStartingEvents() {
        List<Event>events = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.hasExtra(LIST_OF_EVENTS)) {
            events = (List<Event>) intent.getSerializableExtra(LIST_OF_EVENTS);
        }
        return events;
    }

    @Override
    protected void saveToDB(EventsTemplate et) {
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.addEventsTemplate(et);
    }
}
