package com.johanlund.screens.event_activities.mvc_controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.johanlund.base_classes.Event;
import com.johanlund.dao.Dao;
import com.johanlund.dao.SqLiteDao;
import com.johanlund.picker_views.DatePickerFragment;
import com.johanlund.picker_views.TimePickerFragment;
import com.johanlund.screens.event_activities.factories.EventViewFactory;
import com.johanlund.screens.event_activities.factories.EventViewFactoryImpl;
import com.johanlund.screens.event_activities.mvcviews.EventViewMvc;
import com.johanlund.screens.info.ActivityInfoContent;

import static com.johanlund.constants.Constants.EVENT_SAVED_FROM_VIEW;
import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.TITLE_STRING;

public abstract class EventActivity extends AppCompatActivity implements EventViewMvc.Listener {
    protected EventViewMvc mViewMVC;
    protected Dao dao;
    protected Event eventToBind = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            eventToBind = (Event) savedInstanceState.getSerializable(EVENT_SAVED_FROM_VIEW);
        }
        dao = new SqLiteDao(getApplicationContext());
    }
    @Override
    public void showInfo(String titleStr, int infoLayout) {
        //move below to controller
        Intent intent = new Intent(this, ActivityInfoContent.class);
        intent.putExtra(LAYOUT_RESOURCE, infoLayout);
        intent.putExtra(TITLE_STRING, titleStr);
        startActivity(intent);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EVENT_SAVED_FROM_VIEW, mViewMVC.retrieveEventFromView());
    }

    protected void initMvcView(Event eventToBindToView) {
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

    protected void returnEventAndFinish(Event e) {
        returnEvent(e);
        finish();
    }

    protected abstract void returnEvent(Event event);

    /**
     * =============================================================================================
     * DATE AND TIME PICKER
     * =============================================================================================
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

    @Override
    public void onBackPressed() {
        mViewMVC.giveOptionToQuitOrCancel();
    }

    //in case API<21 onBackPressed is not called
    //this is blocking natural behavoiur of backbutton
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }
}
