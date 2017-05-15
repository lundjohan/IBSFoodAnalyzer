package com.ibsanalyzer.inputday;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.model.EventsTemplate;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateFragment extends Fragment {
    private List<EventsTemplate> eventsTemplate;

    public TemplateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_template, container, false);
    }

    public void retrieveEventsAsJSON(String jsonWithEvents) {

    }
}
