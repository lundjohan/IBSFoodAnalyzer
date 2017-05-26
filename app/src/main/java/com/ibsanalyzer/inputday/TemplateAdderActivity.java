package com.ibsanalyzer.inputday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.model.EventsTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.LIST_OF_EVENTS;

public class TemplateAdderActivity extends AppCompatActivity {
    private List<Event> events = new ArrayList<>();
    String nameOfTemplate;
    TextView nameView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.template_adder_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //lägg in switch här så att de andra items också kan användas.
                nameOfTemplate = (String) nameView.getText().toString();
                saveToDB(nameOfTemplate);
                finish();
                return true;
            }
        });
        return true;
    }
    private void saveToDB(String name) {
        EventsTemplate et = new EventsTemplate(events, name);
        DBHandler dbHandler = new DBHandler(this, null, null,1);
        dbHandler.addEventsTemplate(et);
        Log.d("Debug","Inside TemplateAdderActivity: EventsTemplate "+et.getNameOfTemplate() + "has been added to database");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_adder_activity);
        nameView = (TextView)findViewById(R.id.template_name);
        Bundle extras = getIntent().getExtras();
        events = (List<Event>) extras.getSerializable(LIST_OF_EVENTS);
    }
}
