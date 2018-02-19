package com.ibsanalyzer.diary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.date_time.DatePickerFragment;
import com.ibsanalyzer.date_time.DateTimeFormat;
import com.ibsanalyzer.info.ActivityInfoContent;
import com.ibsanalyzer.util.Util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.Calendar;

import static com.ibsanalyzer.constants.Constants.DATE_TO_START_NEW_EVENTACTIVITY;
import static com.ibsanalyzer.constants.Constants.EVENT_POSITION;
import static com.ibsanalyzer.constants.Constants.EVENT_TO_CHANGE;
import static com.ibsanalyzer.constants.Constants.LAYOUT_RESOURCE;
import static com.ibsanalyzer.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.TITLE_STRING;

/**
 * Created by Johan on 2017-05-03.
 * <p>
 * Generic class for all the Activities where user puts in data for Events.
 * <p>
 * http://stackoverflow.com/questions/36970142/how-to-display-layout-of-child-activity
 */

public abstract class EventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    //variables used for changing events.
    //Connected to isChangingEvent. The variable is later used
    // in database to know which event to be changed.
    protected long eventId = -1;
    //position in eventList (in DiaryFragment) for changing event
    protected int posOfEvent = -1;
    TextView dateView;
    TextView timeView;
    TextView commentView;
    Button dateBtn;
    Button timeBtn;

    //this is solely used to see if a ChangingEvent has changed its time during this interaction
    //it should not be used in a context of a new event
    LocalDateTime changingEventStartingDateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setTitle(getTitleStr());
        ViewGroup content = (ViewGroup) findViewById(R.id.appendingLayout);
        getLayoutInflater().inflate(getLayoutRes(), content, true);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        timeBtn = (Button) findViewById(R.id.timeBtn);

        dateView = (TextView) findViewById(R.id.date);
        timeView = (TextView) findViewById(R.id.secondLine);
        commentView = (TextView) findViewById(R.id.commentView);

        //is the event mean to be changed (as opposition to new event to be created)?
        Intent intent = getIntent();
        if (intent.hasExtra(EVENT_TO_CHANGE)) {
            Event e = (Event) intent.getSerializableExtra(EVENT_TO_CHANGE);
            DBHandler dbHandler = new DBHandler(getApplicationContext());
            eventId = dbHandler.getEventId(e);
            posOfEvent = intent.getIntExtra(EVENT_POSITION, -1);
            changingEventStartingDateTime = e.getTime();
            setDateView(e.getTime().toLocalDate());
            setTimeView(e.getTime().toLocalTime());
            commentView.setText(e.getComment());

        }
        //is the event created from scratch, inside diary, the get the start date from open day
        else if (intent.hasExtra(DATE_TO_START_NEW_EVENTACTIVITY)) {
            LocalDate ld = (LocalDate) intent.getSerializableExtra(DATE_TO_START_NEW_EVENTACTIVITY);
            setDateView(ld);
            setTimeView(LocalTime.now()); //must still be set
        } else {
            setDateView(LocalDate.now());
            setTimeView(LocalTime.now());
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("localDateStr")) {
                LocalDate localDate = LocalDate.parse((CharSequence) savedInstanceState.get
                        ("localDateStr"));
                setDateView(localDate);
            }
            if (savedInstanceState.containsKey("localTimeStr")) {
                LocalTime localTime = LocalTime.parse((CharSequence) savedInstanceState.get
                        ("localTimeStr"));
                setTimeView(localTime);
            }
        }
    }
    protected boolean isChangingEvent() {
        return eventId > -1;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_menu, menu);
        inflater.inflate(R.menu.done_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                doneClicked(null);
                return true;
            }
        });
        menu.findItem(R.id.menu_info).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_done){
                    doneClicked(null);
                }
                else if (item.getItemId() == R.id.menu_info){
                    Intent intent = new Intent(getApplicationContext(), ActivityInfoContent.class);
                    intent.putExtra(LAYOUT_RESOURCE, getInfoLayout());
                    intent.putExtra(TITLE_STRING, getTitleStr());
                    startActivity(intent);
                }
                return true;
            }
        });
        return true;
    }
    protected abstract int getInfoLayout();

    protected abstract int getLayoutRes();

    protected abstract void buildEvent();

    protected abstract String getTitleStr();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("localDateStr", (String) dateView.getText().toString());
        outState.putString("localTimeStr", (String) timeView.getText().toString());
        outState.putString("commentStr", (String) commentView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        giveOptionToQuitOrCancel();
    }

    private void giveOptionToQuitOrCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false).
                setTitle("Leave changes undone?").
                setCancelable(true).
                setNegativeButton(android.R.string.cancel, new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).
                setMessage("Are you sure you want to go back to diary without saving changes?").
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish this Activity
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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



    protected abstract int getEventType();

    private boolean eventTypeAtSameTimeAlreadyExists(int type) {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        LocalDateTime ldt = getLocalDateTime();
        Event e = dbHandler.getPartsOfEventIfItExists(type, ldt);
        return e != null;
    }

    private void showEventAlreadyExistsPopUp(int eventType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false).
                setTitle("Event already exists").
                setMessage("A(n) " + Event.getEventTypeStr(eventType) + " at this date and time " +
                        "already exists in diary. Change the date or time of the event.").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //don't do anything
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton
                .getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    public void doneClicked(View view) {
        int type = getEventType();

        /*condition will happen if 1. an event with this eventype already exists and it is not a
        changing event
         2. an event with this eventype already exists, AND is a changing event, datetime has
         been changed to other datetime than start.
          */
        if (eventTypeAtSameTimeAlreadyExists(type) && (!isChangingEvent() ||
                changingEventHasDifferentDateTimeThanStart())) {
            showEventAlreadyExistsPopUp(type);
        } else {
            buildEvent();
            finish();
        }
    }

    /**
     * Prerequisite: changingEventStartingDateTime must be initatied with a ldt =>
     * this method should only be called if we are in a ChangingEventActivity
     *
     * @return
     */
    private boolean changingEventHasDifferentDateTimeThanStart() {
        if (!getIntent().hasExtra(EVENT_TO_CHANGE)) {
            //could throw exception, but feels a bit unecessary. Just use this method with caution!
        }
        return !getLocalDateTime().isEqual(changingEventStartingDateTime);
    }

    public void startTimePicker(View view) {
        DialogFragment newFragment = new RatingActivity.TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
       /* newFragment.getHour();
        lt = newFragment.getTime();
        timeView.setText(lt.getHour()+':'+lt.getMinute());*/
    }

    public void startDatePicker(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setContext(this);
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("Debug", "inuti RatingActivity.onDateSet");

        //month datepicker +1 == LocalDate.Month
        LocalDate ld = LocalDate.of(year, month + 1, dayOfMonth);
        setDateView(ld);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        LocalTime lt = LocalTime.of(hourOfDay, minute);
        setTimeView(lt);
    }

    private void setDateView(LocalDate ld) {
        dateView.setText(DateTimeFormat.toTextViewFormat(ld));
    }

    private void setTimeView(LocalTime lt) {
        timeView.setText(DateTimeFormat.toTextViewFormat(lt));
    }

    //keep this method instead of local variables, it keeps it much less error prone
    protected LocalDateTime getLocalDateTime() {
        String ldStr = (String) dateView.getText();
        String ltStr = (String) timeView.getText();
        LocalDate ld = DateTimeFormat.fromTextViewDateFormat(ldStr);
        LocalTime lt = DateTimeFormat.fromTextViewTimeFormat(ltStr);
        return LocalDateTime.of(ld, lt);
    }

    protected String getComment() {
        return (String) commentView.getText().toString();
    }

    protected void returnEvent(Event event) {
        if (isChangingEvent()) {
            Util.returnChangedEvent(event, RETURN_EVENT_SERIALIZABLE, this, eventId, posOfEvent);
        } else {
            Util.returnNewEvent(event, RETURN_EVENT_SERIALIZABLE, this);
        }
    }

    /**
     *
     */
    public void makeDateInvisible() {
        dateView.setVisibility(View.INVISIBLE);
        dateBtn.setVisibility(View.INVISIBLE);
    }

    /**
     * INNER PICKER CLASSES
     */

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public LocalTime lt;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current timeView as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //see http://stackoverflow
            // .com/questions/11527051/get-date-from-datepicker-using-dialogfragment accepted
            // answer.
            ((TimePickerDialog.OnTimeSetListener) getActivity()).onTimeSet(view, hourOfDay, minute);

        }

        public LocalTime getTime() {
            return lt;
        }

        /*public Object getHour() {
            return hour;
        }*/
    }


}
