package com.johanlund.screens.event_activities.factories;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.johanlund.exceptions.InvalidEventType;
import com.johanlund.screens.event_activities.mvcviews.EventViewMvc;

public interface EventViewFactory {
    EventViewMvc make(LayoutInflater inflater, ViewGroup container, int eventType) throws
            InvalidEventType;
}
