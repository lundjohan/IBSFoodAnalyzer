package com.ibsanalyzer.inputday;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Score;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;

import java.util.Calendar;

/**
 * Created by Johan on 2017-05-01.
 */

public class ScoreActivity extends FragmentActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    TextView date;
    TextView time;
    EditText score;
    Button doneBtn;
    Button dateBtn;
    Button timeBtn;
    LocalTime lt;
    LocalDate ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        doneBtn = (Button) findViewById(R.id.doneBtn);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        timeBtn = (Button) findViewById(R.id.timeBtn);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        date.setText(LocalDate.now().toString());
        time.setText(LocalTime.now().toString());
        score = (EditText) findViewById(R.id.score);
        score.setText("5");
    }

    public void startTimePicker(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
       /* newFragment.getHour();
        lt = newFragment.getTime();
        time.setText(lt.getHour()+':'+lt.getMinute());*/
    }

    public void startDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void doneClicked(View view) {
        finish();
    }

    @Override
    public void finish() {

        LocalDateTime datetime;
        try {
            datetime = LocalDateTime.of(ld, lt);
        } catch (Exception ex) {
            Log.d("Cathed Exception", "LocalDateTime could not be resolved");
            datetime = LocalDateTime.now();
        }
        int after = Integer.parseInt(score.getText().toString());
        Score score = new Score(datetime, after);

        //Put in database here (Android Studio Development Essentials [ASDE] p. 558, 559)


        Gson gson = new Gson();
        String scoreAsJSON = gson.toJson(score);

        Intent data = new Intent();
        data.putExtra("returnScoreJSON", scoreAsJSON);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("Debug", "inuti ScoreActivity.onDateSet");
        date.setText(Integer.toString(view.getYear())+"-"+ Month.of(month)+"-"+String.format("%02d",dayOfMonth));
        ld = LocalDate.of(year, month, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time.setText(String.format("%02d",hourOfDay) +":"+String.format("%02d",minute));
        lt = LocalTime.of(hourOfDay,minute);
    }


    /*
    Denna ska inte vara inner klass i framtiden! (Kommer användas av flera klasser)
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
            ((TimePickerDialog.OnTimeSetListener)getActivity()).onTimeSet(view, hourOfDay, minute);

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
            ((DatePickerDialog.OnDateSetListener)getActivity()).onDateSet(view, year, month, day);
            //  localDate = LocalDate.of(year,month,day);
            // date.setText(Integer.valueOf(ld.getYear())+" "+ld.getMonth().toString()+" "+Integer.valueOf(ld.getDayOfMonth()));
        }
       /* public LocalDate getLocalDate (){
            return localDate;
        }*/
    }
}