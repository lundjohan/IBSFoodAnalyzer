package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.model.EventsTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.LIST_OF_EVENTS;

public class SavingEventsTemplateActivity extends EventsTemplateActivity {
    String nameOfTemplate;
    TextView nameView;

    @Override
    protected void doneClicked() {
        nameOfTemplate = (String) nameView.getText().toString();
        saveToDB(nameOfTemplate);
        finish();
    }

    private void saveToDB(String name) {
        EventsTemplate et = new EventsTemplate(ec.eventList, name);
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.addEventsTemplate(et);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nameView = (TextView)findViewById(R.id.template_name);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_save_events_templates;
    }


}
