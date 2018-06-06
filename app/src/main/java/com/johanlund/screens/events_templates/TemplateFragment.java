package com.johanlund.screens.events_templates;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.events_templates.common.EventsTemplateAdapter;
import com.johanlund.screens.info.ActivityInfoContent;
import com.johanlund.util.Util;

import java.util.List;

import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.TITLE_STRING;


public class TemplateFragment extends Fragment {
    final String TITLE_STR = "EventsTemplates";
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
        layoutManager = new GridLayoutManager(getActivity(), mNoOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        DBHandler dbHandler = new DBHandler(getActivity());
        Cursor cursor = dbHandler.getCursorToEventsTemplates();
        adapter = new EventsTemplateAdapter(this, cursor, width);
        recyclerView.setAdapter(adapter);
        callback = (TemplateFragmentListener) getActivity();

        ImageButton infoBtn = (ImageButton) v.findViewById(R.id.info_eventsTemplate_btn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityInfoContent.class);
                intent.putExtra(LAYOUT_RESOURCE, R.layout.info_events_template_fragment);
                intent.putExtra(TITLE_STRING, TITLE_STR);
                startActivity(intent);
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(TITLE_STR);
        //Toolbar eventsTemplateToolBar = (Toolbar)v.findViewById(R.id.toolbar4);
        //eventsTemplateToolBar.setTitle(TITLE_STR);
        //v.setSupportActionBar(eventsTemplateToolBar);
        //callback.fixToolBarForTemplateFragment();
        return v;
    }

    public interface TemplateFragmentListener {
        /**
         * This method pushes events from here => MainActivity => DiaryFragment
         *
         * @param events
         */
        void addEventsFromEventsTemplateToDiary(List<Event> events);
        void fixToolBarForTemplateFragment();
    }
}
