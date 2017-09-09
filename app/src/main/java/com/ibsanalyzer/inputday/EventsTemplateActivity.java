package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.constants.Constants;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.model.EventsTemplate;

import java.util.Collections;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.EVENT_POSITION;
import static com.ibsanalyzer.constants.Constants.EVENT_TO_CHANGE;
import static com.ibsanalyzer.constants.Constants.ID_OF_EVENT;
import static com.ibsanalyzer.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.ibsanalyzer.constants.Constants.RETURN_BM_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_OTHER_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_RATING_SERIALIZABLE;
import static com.ibsanalyzer.inputday.DiaryFragment.CHANGED_BM;
import static com.ibsanalyzer.inputday.DiaryFragment.CHANGED_EXERCISE;
import static com.ibsanalyzer.inputday.DiaryFragment.CHANGED_MEAL;
import static com.ibsanalyzer.inputday.DiaryFragment.CHANGED_OTHER;
import static com.ibsanalyzer.inputday.DiaryFragment.CHANGED_RATING;
import static com.ibsanalyzer.inputday.EventsContainer.NEW_BM;
import static com.ibsanalyzer.inputday.EventsContainer.NEW_EXERCISE;
import static com.ibsanalyzer.inputday.EventsContainer.NEW_MEAL;
import static com.ibsanalyzer.inputday.EventsContainer.NEW_OTHER;
import static com.ibsanalyzer.inputday.EventsContainer.NEW_RATING;

/**
 * Reuses a lot of code from DiaryFragment.
 * <p>
 * Some implemenations uses a TextView for name and some (1) don't. Be aware of this! => It
 * should be abstracted completely in this parent class.
 */
public abstract class EventsTemplateActivity extends AppCompatActivity implements EventsContainer
        .EventsContainerUser {

    protected EventsContainer ec;


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EventsTemplate toReturn = createEventsTemplateForReturn();
                saveToDB(toReturn);
                finish();
                return true;
            }
        });
        return true;
    }

    private EventsTemplate createEventsTemplateForReturn() {
        String nameOfTemplate = getEndingName();
        return new EventsTemplate(ec.eventList, nameOfTemplate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_template);

        //inflate specifics for heritating class
        ViewGroup upperPart = (ViewGroup) findViewById(R.id.upperPart);
        getLayoutInflater().inflate(getLayoutRes(), upperPart, true);

        setUpNameViewIfExisting();

        ec = new EventsContainer(this);
        ec.eventList = getStartingEvents();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        View buttons = findViewById(R.id.buttons);

        ec.setUpEventButtons(buttons);
        ec.initiateRecyclerView(recyclerView, this);
        ec.adapter.notifyDataSetChanged();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ec.onActivityResult(requestCode, resultCode, data);
    }

    protected abstract int getLayoutRes();

    protected abstract String getStartingName();

    protected abstract String getEndingName();

    /**
     * As hinted, only some implemenations use a nameView.
     */
    protected abstract void setUpNameViewIfExisting();

    protected abstract List<Event> getStartingEvents();

    protected abstract void saveToDB(EventsTemplate et);

/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("eventList", (Serializable) ec.eventList);
        outState.putString("someVarB", someVarB);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        someVarA = savedInstanceState.getInt("someVarA");
        someVarB = savedInstanceState.getString("someVarB");
    }*/


    /**
     * Not that date should not be an issue here, only time of day.
     * <p>
     * There should be ok to have same time of same type of events, then it time is changed
     * automatically. Implement this and test for it!
     *
     * @param event
     */


    @Override
    public void addEventToList(Event event) {
        ec.eventList.add(event);
        //All dates must be the same, becuase dates are irrellevant in a EventsTemplate,
        // only time matter.
        //TODO: implement constriction for above
        Collections.sort(ec.eventList);
        ec.adapter.notifyDataSetChanged();

    }

    @Override
    public void executeNewEvent(int requestCode, Intent data) {
        Event event = null;
        switch (requestCode) {

            case NEW_MEAL:
                if (data.hasExtra(RETURN_MEAL_SERIALIZABLE)) {
                    //add to database
                    event = (Meal) data.getSerializableExtra(RETURN_MEAL_SERIALIZABLE);
                }
                break;
            case NEW_OTHER:
                if (data.hasExtra(RETURN_OTHER_SERIALIZABLE)) {
                    event = (Other) data.getSerializableExtra(RETURN_OTHER_SERIALIZABLE);
                }
                break;
            case NEW_EXERCISE:
                if (data.hasExtra(RETURN_EXERCISE_SERIALIZABLE)) {
                    event = (Exercise) data.getSerializableExtra(RETURN_EXERCISE_SERIALIZABLE);
                }
                break;
            case NEW_BM:
                if (data.hasExtra(RETURN_BM_SERIALIZABLE)) {
                    event = (Bm) data.getSerializableExtra(RETURN_BM_SERIALIZABLE);
                }
                break;
            case NEW_RATING:
                if (data.hasExtra(RETURN_RATING_SERIALIZABLE)) {
                    event = (Rating) data.getSerializableExtra(RETURN_RATING_SERIALIZABLE);
                }
                break;
        }
        addEventToList(event);
    }

    /**
     * TODO RETURN_MEAL_SER... => RETURN_EVENT... should make this method way smaller. But have
     * to check that's all alright in DiaryFragment same name method also (must do meal.getClass
     * == Meal.class checks in there. But that's all)
     *
     * @param requestCode
     * @param data
     */
    @Override
    public void executeChangedEvent(int requestCode, Intent data) {
        int posInList = data.getIntExtra(POS_OF_EVENT_RETURNED, -1);
        if (posInList == -1) {
            throw new RuntimeException("Received no EVENT POSITION from New/Changed Event " +
                    "Activity (MealActivity etc)");
        }
        Event event = null;


        switch (requestCode) {

            case CHANGED_MEAL:
                if (data.hasExtra(RETURN_MEAL_SERIALIZABLE)) {
                    event = (Meal) data.getSerializableExtra(RETURN_MEAL_SERIALIZABLE);
                }
                break;
            case CHANGED_OTHER:
                if (data.hasExtra(RETURN_OTHER_SERIALIZABLE)) {
                    event = (Other) data.getSerializableExtra(RETURN_OTHER_SERIALIZABLE);
                }
                break;
            case CHANGED_EXERCISE:
                if (data.hasExtra(RETURN_EXERCISE_SERIALIZABLE)) {
                    event = (Exercise) data.getSerializableExtra(RETURN_EXERCISE_SERIALIZABLE);
                }
                break;
            case CHANGED_BM:
                if (data.hasExtra(RETURN_BM_SERIALIZABLE)) {
                    event = (Bm) data.getSerializableExtra(RETURN_BM_SERIALIZABLE);
                }
                break;
            case CHANGED_RATING:
                if (data.hasExtra(RETURN_RATING_SERIALIZABLE)) {
                    event = (Rating) data.getSerializableExtra(RETURN_RATING_SERIALIZABLE);
                }
                break;
        }
        ec.changeEventInList(posInList, event);
    }

    public void newMealActivity(View view) {
        Intent intent = new Intent(this, MealActivity.class);
        intent.putExtra(Constants.SHOULD_HAVE_DATE, false);
        startActivityForResult(intent, NEW_MEAL);
    }

    public void newOtherActivity(View v) {
        Intent intent = new Intent(this, OtherActivity.class);
        intent.putExtra(Constants.SHOULD_HAVE_DATE, false);
        startActivityForResult(intent, NEW_OTHER);
    }

    public void newExerciseActivity(View v) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra(Constants.SHOULD_HAVE_DATE, false);
        startActivityForResult(intent, NEW_EXERCISE);
    }

    public void newBmActivity(View v) {
        Intent intent = new Intent(this, BmActivity.class);
        intent.putExtra(Constants.SHOULD_HAVE_DATE, false);
        startActivityForResult(intent, NEW_BM);
    }

    public void newScoreItem(View view) {
        Intent intent = new Intent(this, RatingActivity.class);
        intent.putExtra(Constants.SHOULD_HAVE_DATE, false);
        startActivityForResult(intent, NEW_RATING);
    }


    @Override
    public void onClick(View v) {
        ec.doOnClick(v);
    }

    @Override
    public void onItemClicked(View v, int position) {
        ec.editEvent(position);
    }

    @Override
    public boolean onItemLongClicked(final View v, final int position) {
        //initiate pop-up menu
        PopupMenu popup = new PopupMenu(this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.event_delete_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.deleteEventForEventsTemplate) {
                    ec.eventList.remove(position);
                    ec.adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        popup.show();
        return true;
    }

    //user requests to change event
    public void changeEventActivity(Event event, Class activityClass, int valueToReturn, int
            posInList) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(Constants.SHOULD_HAVE_DATE, false);
        intent.putExtra(EVENT_TO_CHANGE, event);
        intent.putExtra(EVENT_POSITION, posInList);
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        long eventId = dbHandler.getEventId(event);
        intent.putExtra(ID_OF_EVENT, eventId);
        startActivityForResult(intent, valueToReturn);
    }
}
