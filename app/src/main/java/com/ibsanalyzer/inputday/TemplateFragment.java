package com.ibsanalyzer.inputday;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibsanalyzer.adapters.EventsTemplateAdapter;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.util.Util;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ibsanalyzer.constants.Constants.CHANGED_EVENT;
import static com.ibsanalyzer.constants.Constants.EVENTSTEMPLATE_TO_LOAD;
import static com.ibsanalyzer.constants.Constants.EVENTS_TO_LOAD;
import static com.ibsanalyzer.constants.Constants.LOAD_EVENTS_FROM_EVENTSTEMPLATE;


public class TemplateFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    TemplateFragmentListener callback;

    public TemplateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_template, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.templateList);

        //CHANGE parameters.
        //see http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns

        int mNoOfColumns = Util.calculateNoOfColumns(getActivity().getApplicationContext(), 180);
        int width = Util.calculateWidthOfItem(getActivity().getApplicationContext(), mNoOfColumns);
        Log.d("Debug", "width of item in GridLayout: " + width);
        layoutManager = new GridLayoutManager(getActivity(), mNoOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        DBHandler dbHandler = new DBHandler(getActivity());
        Cursor cursor = dbHandler.getCursorToEventsTemplates();
        adapter = new EventsTemplateAdapter(this, cursor, width);
        recyclerView.setAdapter(adapter);
        callback = (TemplateFragmentListener) getActivity();
        return v;
    }

    /**
     * Currently only used for transfering events from EventsTemplate to MainActivity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Event>eventsToReturn = null;
        if (resultCode != RESULT_OK) {
            return;
        }
        if (resultCode != LOAD_EVENTS_FROM_EVENTSTEMPLATE) {
            return;
        }
        if (data.hasExtra(EVENTS_TO_LOAD)) {
            eventsToReturn = (List<Event>)data.getSerializableExtra(EVENTS_TO_LOAD);
        }
        callback.addEventsFromEventsTemplateToDiary(eventsToReturn);
    }

    public interface TemplateFragmentListener {
        /**
         * This method pushes events from here => MainActivity => DiaryFragment
         *
         * @param events
         */
        void addEventsFromEventsTemplateToDiary(List<Event> events);
    }
}
