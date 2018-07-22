package com.johanlund.screens.events_templates_actions.mvc_views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public class NewEventsTemplateViewMvc extends EditEventsTemplateViewMvc {

    public NewEventsTemplateViewMvc(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
    }

    @Override
    protected String getBarTitle() {
        return "Save EventsTemplate";
    }
}

