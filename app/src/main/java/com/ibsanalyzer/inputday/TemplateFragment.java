package com.ibsanalyzer.inputday;


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
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.model.EventsTemplate;
import com.ibsanalyzer.util.Util;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateFragment extends Fragment {
    private List<EventsTemplate> eventsTemplate;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

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
        Log.d("Debug","width of item in GridLayout: "+width);
        layoutManager = new GridLayoutManager(getActivity(), mNoOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        DBHandler dbHandler = new DBHandler(getActivity());
        Cursor cursor = dbHandler.getCursorToEventsTemplates();
        adapter = new EventsTemplateAdapter(getActivity(), cursor, width);
        recyclerView.setAdapter(adapter);
        return v;
    }
}
