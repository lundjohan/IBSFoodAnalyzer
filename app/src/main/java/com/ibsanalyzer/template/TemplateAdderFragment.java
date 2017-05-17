package com.ibsanalyzer.template;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.inputday.DiaryFragment;
import com.ibsanalyzer.inputday.R;
import com.ibsanalyzer.model.EventsTemplate;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.LIST_OF_EVENTS;
import static com.ibsanalyzer.constants.Constants.POSITION_IN_DIARY;

public class TemplateAdderFragment extends Fragment {
    private List<Event> events = new ArrayList<>();
    public TemplateAdderListener callback;

    public interface TemplateAdderListener {
        void startTemplateFragment();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }
    //not finished, see http://stackoverflow.com/questions/15653737/oncreateoptionsmenu-inside-fragments
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.template_adder_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //lägg in switch här
                saveToDB(null);
                callback.startTemplateFragment();
                return true;
            }

        });
    }
    private void saveToDB(View v) {
        EventsTemplate et = new EventsTemplate(events, "TestnameOfTemplate"+ LocalDateTime.now().toString());
        DBHandler dbHandler = new DBHandler(getActivity(), null, null,1);
        dbHandler.addEventsTemplate(et);
        Log.d("Debug","Inside TemplateAdderFragment: EventsTemplate "+et.getNameOfTemplate() + "has been added to database");
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
       events = (List<Event>) b.getSerializable(LIST_OF_EVENTS);
        Log.d("Debug","inside TemplateAdderFragment events: "+events.toString());  //hit kommer jag.
        return inflater.inflate(R.layout.activity_template_adder, container, false);
    }
    //used by TabPagerAdapter to interchange fragments
    public static TemplateAdderFragment newInstance(List<Event>events) {

        Bundle args = new Bundle();
        args.putSerializable(LIST_OF_EVENTS, (Serializable)events);

        TemplateAdderFragment fragment = new TemplateAdderFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
