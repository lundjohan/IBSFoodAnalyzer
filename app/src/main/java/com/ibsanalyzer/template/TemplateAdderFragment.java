package com.ibsanalyzer.template;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.inputday.R;
import com.ibsanalyzer.model.EventsTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.MARKED_EVENTS_JSON;

public class TemplateAdderFragment extends Fragment {
    List<Event> events = new ArrayList<>();
    TemplateAdderListener callBack;

    public interface TemplateAdderListener {
        public void eventsTemplateToTemplateFragment(EventsTemplate et);
    }

    //not finished, see http://stackoverflow.com/questions/15653737/oncreateoptionsmenu-inside-fragments
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.to_template_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                callBack.eventsTemplateToTemplateFragment(new EventsTemplate(events));
                return true;
            }

        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        //extract data received, see p. 392
        /*Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String eventListJson = extras.getString(MARKED_EVENTS_JSON);
            Log.d("Debug", eventListJson);
        }*/
        return inflater.inflate(R.layout.activity_template_adder, container, false);
    }

    private void doneClicked() {
        //starta TemplateFragment, hur? => måste accessa mainActivity och därifrån
        //skicka events som data.
        //det är allt.

    }
}
