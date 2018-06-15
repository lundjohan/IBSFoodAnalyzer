package com.johanlund.screens.event_activities.mvc_controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.johanlund.base_classes.Event;
import com.johanlund.model.EventManager;
import com.johanlund.model.TagType;
import com.johanlund.picker_views.DatePickerFragment;
import com.johanlund.picker_views.TimePickerFragment;
import com.johanlund.screens.event_activities.factories.EventViewFactory;
import com.johanlund.screens.event_activities.factories.EventViewFactoryImpl;
import com.johanlund.screens.event_activities.mvcviews.EventViewMvc;
import com.johanlund.screens.info.ActivityInfoContent;
import com.johanlund.screens.tag_adder.TagAdderActivity;

import static com.johanlund.constants.Constants.EVENT_SAVED_FROM_VIEW;
import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.johanlund.constants.Constants.TAGS_TO_ADD;
import static com.johanlund.constants.Constants.TITLE_STRING;

public abstract class EventActivity extends AppCompatActivity implements EventViewMvc
        .EventActivityViewMvcListener  {
    protected EventViewMvc mViewMVC;
    protected EventManager eventManager;
    protected Event eventToBind = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            eventToBind = (Event)savedInstanceState.getSerializable(EVENT_SAVED_FROM_VIEW );
        }
        eventManager = new EventManager(getApplicationContext());
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EVENT_SAVED_FROM_VIEW, mViewMVC.retrieveEventFromView());
    }

    protected void initMvcView(Event eventToBindToView){
        EventViewFactory viewFactory = new EventViewFactoryImpl();
        mViewMVC = viewFactory.make(LayoutInflater.from(this), null, eventToBindToView.getType());
        mViewMVC.setListener(this);

        // Set the root view of the associated MVC view as the content of this activity
        setContentView(mViewMVC.getRootView());

        mViewMVC.bindEventToView(eventToBindToView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return mViewMVC.createOptionsMenu(menu, getMenuInflater());
    }

    @Override
    public abstract void completeSession(Event e);

    @Override
    public void showInfo(String titleStr, int infoLayout) {
        //move below to controller
        Intent intent = new Intent(this, ActivityInfoContent.class);
        intent.putExtra(LAYOUT_RESOURCE, infoLayout);
        intent.putExtra(TITLE_STRING, titleStr);
        startActivity(intent);
    }

    protected void returnEventAndFinish(Event e) {
        returnEvent(e);
        finish();
    }
    protected abstract void returnEvent(Event event);

    //data coming back from TagAdder
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode != TAGS_TO_ADD) {
            return;
        }
        if (data.hasExtra(RETURN_TAG_TEMPLATE_SERIALIZABLE)) {
            TagType tagType = (TagType) data.getExtras().getSerializable
                    (RETURN_TAG_TEMPLATE_SERIALIZABLE);
            mViewMVC.bindAddedTagToView(tagType.get_tagname());
        }
    }

    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, TAGS_TO_ADD);
    }

    /**
     * DATE AND TIME PICKER
     */
    @Override
    public void startTimePicker(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setContext(this);
        newFragment.setListener(mViewMVC);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void startDatePicker(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setContext(this);
        newFragment.setListener(mViewMVC);
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
