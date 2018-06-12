package com.johanlund.screens.event_activities.factories;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.johanlund.exceptions.InvalidEventType;
import com.johanlund.screens.event_activities.mvcviews.BmViewMvc;
import com.johanlund.screens.event_activities.mvcviews.EventViewMvc;
import com.johanlund.screens.event_activities.mvcviews.ExerciseViewMvc;
import com.johanlund.screens.event_activities.mvcviews.MealViewMvc;
import com.johanlund.screens.event_activities.mvcviews.OtherViewMvc;
import com.johanlund.screens.event_activities.mvcviews.RatingViewMvc;

import static com.johanlund.constants.Constants.BM;
import static com.johanlund.constants.Constants.EXERCISE;
import static com.johanlund.constants.Constants.MEAL;
import static com.johanlund.constants.Constants.OTHER;
import static com.johanlund.constants.Constants.RATING;

public class EventViewFactoryImpl implements EventViewFactory {
    public EventViewMvc make(LayoutInflater inflater, ViewGroup container, int eventType) throws InvalidEventType {
        switch (eventType) {
            case MEAL: {
                return new MealViewMvc(inflater, container);
            }
            case OTHER: {
                return new OtherViewMvc(inflater, container);
            }
            case EXERCISE: {
                return new ExerciseViewMvc(inflater, container);
            }
            case BM: {
                return new BmViewMvc(inflater, container);
            }
            case RATING: {
                return new RatingViewMvc(inflater, container);
            }
            default:{
                throw new InvalidEventType(Integer.toString(eventType));
            }
        }
    }
}
