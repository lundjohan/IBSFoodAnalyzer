package com.johanlund.screens.events_container_classes.common.mvcviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johanlund.common.BaseObservable;
import com.johanlund.constants.Constants;
import com.johanlund.ibsfoodanalyzer.R;

/**
 * Created by Johan on 2018-03-07.
 * <p>
 * This class takes care of the Event Buttons used by several activities.
 * <p>
 * Users of this class must implement functions related to new creations of events.
 */
public class EventButtonsViewMvcImpl extends BaseObservable<EventButtonsViewMvc.Listener> implements EventButtonsViewMvc {

    public EventButtonsViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.event_buttons, container);

        setUpEventButton(container.findViewById(R.id.mealBtn));
        setUpEventButton(container.findViewById(R.id.otherBtn));
        setUpEventButton(container.findViewById(R.id.exerciseBtn));
        setUpEventButton(container.findViewById(R.id.bmBtn));
        setUpEventButton(container.findViewById(R.id.ratingBtn));
    }
    private void setUpEventButton (View eventBtn){
        eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EventButtonsViewMvc.Listener listener : getListeners()) {
                    doOnClick(v, listener);
                }
            }
        });
    }

    public void doOnClick(View v, EventButtonsViewMvc.Listener listener) {
        int eventType = -1;
        switch (v.getId()) {
            case R.id.mealBtn:
                eventType = Constants.MEAL;
                break;
            case R.id.otherBtn:
                eventType = Constants.OTHER;
                break;
            case R.id.exerciseBtn:
                eventType = Constants.EXERCISE;
                break;
            case R.id.bmBtn:
                eventType = Constants.BM;
                break;
            case R.id.ratingBtn:
                eventType = Constants.RATING;
                break;
        }
        listener.newEventActivity(eventType);
    }
}
