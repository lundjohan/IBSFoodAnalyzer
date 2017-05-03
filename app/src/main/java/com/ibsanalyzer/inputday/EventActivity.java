package com.ibsanalyzer.inputday;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ibsanalyzer.base_classes.Score;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.Calendar;

import static com.ibsanalyzer.inputday.R.id.scoreBar;
import static com.ibsanalyzer.inputday.R.id.scoreName;

/**
 * Created by Johan on 2017-05-03.
 *
 * Generic class for all the Activities where user puts in data for Events.
 *
 * http://stackoverflow.com/questions/36970142/how-to-display-layout-of-child-activity
 */

public abstract class EventActivity extends FragmentActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    TextView date;
    TextView time;
    Button doneBtn;
    Button dateBtn;
    Button timeBtn;
    LocalTime lt = LocalTime.now();
    LocalDate ld= LocalDate.now();
    LocalDateTime datetime;

    public void doneClicked(View view) {
        Log.d("Debug","inside doneClicked inside abstract class");

        setLocalDate();
        finish();
        super.finish();
    }

    protected abstract int getLayoutRes();
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("localDateStr", (String) ld.toString());
        outState.putString("localTimeStr", (String) lt.toString());
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ViewGroup content = (ViewGroup) findViewById(R.id.appendingLayout);
        getLayoutInflater().inflate(getLayoutRes(),content,true);
        doneBtn = (Button) findViewById(R.id.doneBtn);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        timeBtn = (Button) findViewById(R.id.timeBtn);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        setDate(LocalDate.now());
        setTime(LocalTime.now());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("localDateStr")){
                LocalDate localDate = LocalDate.parse((CharSequence) savedInstanceState.get("localDateStr"));
                setDate(localDate);
            }
            if (savedInstanceState.containsKey("localTimeStr")){
                LocalTime localTime = LocalTime.parse((CharSequence) savedInstanceState.get("localTimeStr"));
                setTime(localTime);
            }
        }
    }
    public void startTimePicker(View view) {
        DialogFragment newFragment = new ScoreActivity.TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
       /* newFragment.getHour();
        lt = newFragment.getTime();
        time.setText(lt.getHour()+':'+lt.getMinute());*/
    }

    public void startDatePicker(View view) {
        DialogFragment newFragment = new ScoreActivity.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("Debug", "inuti ScoreActivity.onDateSet");

        //month datepicker +1 == LocalDate.Month
        ld = LocalDate.of(year, month+1, dayOfMonth);
        setDate(ld);
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        lt = LocalTime.of(hourOfDay, minute);
        setTime(lt);
    }

    private void setDate (LocalDate ld){
        date.setText(ld.getMonth().toString()+" " + String.valueOf(ld.getDayOfMonth()) + ", " + String.valueOf(ld.getYear()));
        this.ld = ld;
    }
    private void setTime (LocalTime lt){
        time.setText(String.format("%02d", lt.getHour()) + ":" + String.format("%02d", lt.getMinute()));
        this.lt = lt;
    }

    public void setLocalDate() {
        try {
            datetime = LocalDateTime.of(ld, lt);
        } catch (Exception ex) {
            Log.d("Cathed Exception", "LocalDateTime could not be resolved");
            datetime = LocalDateTime.now();
        }
    }

    /**
     * INNER PICKER CLASSES
     */

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public LocalTime lt;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //see http://stackoverflow.com/questions/11527051/get-date-from-datepicker-using-dialogfragment accepted answer.
            ((TimePickerDialog.OnTimeSetListener) getActivity()).onTimeSet(view, hourOfDay, minute);

        }

        public LocalTime getTime() {
            return lt;
        }

        /*public Object getHour() {
            return hour;
        }*/
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        //  public LocalDate localDate;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //see http://stackoverflow.com/questions/11527051/get-date-from-datepicker-using-dialogfragment accepted answer.
            ((DatePickerDialog.OnDateSetListener) getActivity()).onDateSet(view, year, month, day);
            //  localDate = LocalDate.of(year,month,day);
            // date.setText(Integer.valueOf(ld.getYear())+" "+ld.getMonth().toString()+" "+Integer.valueOf(ld.getDayOfMonth()));
        }
       /* public LocalDate getLocalDate (){
            return localDate;
        }*/
    }

}
