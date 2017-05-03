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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
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
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    TextView date;
    TextView time;
    TextView scoreName;
    EditText score;
    Button doneBtn;
    Button dateBtn;
    Button timeBtn;
    LocalTime lt = LocalTime.now();
    LocalDate ld= LocalDate.now();

    SeekBar scoreBar;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("seekBar", scoreBar.getProgress());
        outState.putString("localDateStr", (String) ld.toString());
        outState.putString("localTimeStr", (String) lt.toString());
        super.onSaveInstanceState(outState);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        doneBtn = (Button) findViewById(R.id.doneBtn);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        timeBtn = (Button) findViewById(R.id.timeBtn);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        scoreName = (TextView) findViewById(R.id.scoreName);
        setDate(LocalDate.now());
        setTime(LocalTime.now());
        scoreBar = (SeekBar) findViewById(R.id.scoreBar);
        scoreBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Debug", "inside onProgressChanged");
                int score = ++progress;
                switch(score){
                    case 1:
                        scoreName.setText("Abysmal");
                        break;
                    case 2:
                        scoreName.setText("Awful");
                        break;
                    case 3:
                        scoreName.setText("Bad");
                        break;
                    case 4:
                        scoreName.setText("Deficient");
                        break;
                    case 5:
                        scoreName.setText("Good");
                        break;
                    case 6:
                        scoreName.setText("Great");
                        break;
                    case 7:
                        scoreName.setText("Phenomenal");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (savedInstanceState != null) {//startvalue is set to 5 if no value exists in savedInstance
            if (savedInstanceState.containsKey("seekBar")) {
                scoreBar.setProgress(savedInstanceState.getInt("seekBar"));
            }
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
        //scoreBar starts from zero
        int after = scoreBar.getProgress() + 1;
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