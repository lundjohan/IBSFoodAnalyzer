package com.johanlund.screens.events_templates_actions.mvc_controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.InputEvent;
import com.johanlund.base_classes.Meal;
import com.johanlund.base_classes.Other;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.constants.Constants;
import com.johanlund.dao.Dao;
import com.johanlund.dao.SqLiteDao;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.model.EventsTemplate;
import com.johanlund.screens.event_activities.mvc_controllers.ChangeEventActivity;
import com.johanlund.screens.event_activities.mvc_controllers.NewEventActivity;
import com.johanlund.screens.events_container_classes.common.EventsContainer;
import com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvcImpl;
import com.johanlund.screens.events_templates_actions.mvc_views.EventsTemplateViewMvc;
import com.johanlund.screens.info.ActivityInfoContent;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import static com.johanlund.constants.Constants.EVENT_POSITION;
import static com.johanlund.constants.Constants.EVENT_TYPE;
import static com.johanlund.constants.Constants.ID_OF_EVENT;
import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;
import static com.johanlund.constants.Constants.NEW_EVENT;
import static com.johanlund.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.johanlund.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.johanlund.constants.Constants.TITLE_STRING;
import static com.johanlund.screens.events_container_classes.common.EventsContainer.EVENT_NEW;

/**
 * Reuses a lot of code from DiaryFragment.
 * <p>
 * Some implemenations uses a TextView for name and some (1) don't. Be aware of this! => It
 * should be abstracted completely in this parent class.
 */
public abstract class EventsTemplateActivity extends AppCompatActivity
        implements EventsTemplateViewMvc.Listener {

    protected EventsContainer ec;
    protected EventButtonsViewMvcImpl mButtonsViewMvc;
    protected EventsTemplateViewMvc mViewMVC;


    public boolean onCreateOptionsMenu(Menu menu) {
        return mViewMVC.createOptionsMenu(menu, getMenuInflater());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the root view of the associated MVC view as the content of this activity
        setContentView(mViewMVC.getRootView());
        mButtonsViewMvc = new EventButtonsViewMvcImpl(getLayoutInflater(), (ViewGroup) mViewMVC.getRootView().findViewById(R
                .id.buttons));
        ec = mViewMVC.getEventsContainer();
    }

    protected void initMvcView(EventsTemplate et) {
        mViewMVC.setListener(this);
        mViewMVC.bindEventsTemplateToView(et);
        mViewMVC.bindDateToView(LocalDate.now());
    }

    @Override
    public void onStart() {
        super.onStart();
        mButtonsViewMvc.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mButtonsViewMvc.unregisterListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data.hasExtra(NEW_EVENT)) {
            executeNewEvent(requestCode, data);
        }
        ec.onActivityResult(requestCode, resultCode, data);
    }

    protected abstract void saveToDB(EventsTemplate et);

    protected abstract void saveToDiary(EventsTemplate et);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //in case API<21 onBackPressed is not called
    //this is blocking natural behavoiur of backbutton
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    /**
     * Function that removes tagtypes in EventsTemplate if there is no find for it in database
     *
     * @param et
     * @return
     */

    public EventsTemplate removeTagTypesThatDontExist(final EventsTemplate et) {
        List<Event> cleansedEvents = new ArrayList<>();
        Dao dao = new SqLiteDao(this.getApplicationContext());
        for (Event e : et.getEvents()) {
            Event cleansedEvent = null;
            if (e instanceof InputEvent) {
                List<TagWithoutTime> tags = ((InputEvent) e).getTagsWithoutTime();
                List<TagWithoutTime> cleansedTags = new ArrayList<>();
                for (TagWithoutTime t : tags) {
                    if (dao.tagTypeExists(t.getName())) {
                        cleansedTags.add(t);
                    }
                }
                if (e instanceof Meal) {
                    Meal m = ((Meal) e);
                    cleansedEvent = new Meal(m.getTime(), m.getComment(), m.hasBreak(),
                            cleansedTags, m.getPortions());
                } else if (e instanceof Other) {
                    Other o = ((Other) e);
                    cleansedEvent = new Other(o.getTime(), o.getComment(), o.hasBreak(),
                            cleansedTags);
                }
            } else if (e instanceof Exercise) {
                Exercise exercise = ((Exercise) e);
                TagWithoutTime t = exercise.getTypeOfExercise();
                if (t != null && dao.tagTypeExists(t.getName())) {
                    cleansedEvent = exercise;
                } else {
                    cleansedEvent = new Exercise(exercise.getTime(), exercise.getComment(),
                            exercise.hasBreak(), null, exercise.getIntensity());
                }
            } else {
                cleansedEvent = e;
            }
            cleansedEvents.add(cleansedEvent);
        }
        return new EventsTemplate(cleansedEvents, et.getNameOfTemplate());
    }

    @Override
    public void completeSession(EventsTemplate et) {
        saveToDB(et);
        saveToDiary(et);
        finish();
    }

    @Override
    public void showInfo(String titleStr, int infoLayout) {
        //move below to controller
        Intent intent = new Intent(this, ActivityInfoContent.class);
        intent.putExtra(LAYOUT_RESOURCE, infoLayout);
        intent.putExtra(TITLE_STRING, titleStr);
        startActivity(intent);
    }


    /*
    --------------------------------------------------------------------------------------------
    EventButtonsUser.Listener methods
    --------------------------------------------------------------------------------------------
    */
    @Override
    public void newEventActivity(int eventType) {
        Intent intent = new Intent(this, NewEventActivity.class);
        intent.putExtra(Constants.EVENT_TYPE, eventType);
        // intent.putExtra(Constants.NEW_EVENT_DATE, getDate() eller annan lösning);
        startActivityForResult(intent, EVENT_NEW);
    }

    //TODO code is extremly similar to DiaryFragment (except for database handling)
    @Override
    public void executeNewEvent(int requestCode, Intent data) {
        if (data.hasExtra(RETURN_EVENT_SERIALIZABLE)) {
            Event event = (Event) data.getSerializableExtra(RETURN_EVENT_SERIALIZABLE);
            mViewMVC.bindEventToList(event);
        }
    }

    /*
       --------------------------------------------------------------------------------------------
       EventsContainerUser.Listener methods
       --------------------------------------------------------------------------------------------
       */
    //user requests to change event
    @Override
    public void changeEventActivity(Event event, int eventType, int valueToReturn, int
            posInList) {
        Intent intent = new Intent(this, ChangeEventActivity.class);
        intent.putExtra(EVENT_TYPE, eventType);
        intent.putExtra(Constants.SHOULD_HAVE_DATE, false);
        intent.putExtra(EVENT_POSITION, posInList);
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        long eventId = dbHandler.getEventIdOutsideEventsTemplate(event);    //this is crazy,
        // should be idINSIDEEventsTemplate
        intent.putExtra(ID_OF_EVENT, eventId);
        startActivityForResult(intent, valueToReturn);
    }

    //TODO code is extremly similar to DiaryFragment (except for database handling)
    @Override
    public void executeChangedEvent(int requestCode, Intent data) {
        int posInList = data.getIntExtra(POS_OF_EVENT_RETURNED, -1);
        if (posInList == -1) {
            throw new RuntimeException("Received no EVENT POSITION from New/Changed Event " +
                    "Activity (MealActivity etc)");
        }
        if (data.hasExtra(RETURN_EVENT_SERIALIZABLE)) {
            Event event = (Event) data.getSerializableExtra(RETURN_EVENT_SERIALIZABLE);
            mViewMVC.bindChangedEventToList(event, posInList);
        }
    }

}
