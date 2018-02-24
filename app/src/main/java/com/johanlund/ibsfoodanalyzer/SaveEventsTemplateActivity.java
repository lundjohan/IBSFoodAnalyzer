package com.johanlund.ibsfoodanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.model.EventsTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.johanlund.constants.Constants.LIST_OF_EVENTS;

public class SaveEventsTemplateActivity extends EventsTemplateActivity {
    TextView nameView;

    @Override
    protected String getTitleStr() {
        return "Save EventsTemplate";
    }

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
        List<Event> events = new ArrayList<>();
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

    @Override
    protected void saveToDiary() {}

    @Override
    protected String getEndingName() {
        return nameView.getText().toString();
    }

    @Override
    protected void setUpNameViewIfExisting() {
        nameView = (TextView) findViewById(R.id.template_name);
        nameView.setText(getStartingName());
    }
}
