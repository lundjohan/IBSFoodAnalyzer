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
import com.ibsanalyzer.base_classes.BM;
import com.ibsanalyzer.base_classes.Score;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;

import java.util.Calendar;

import static android.R.attr.x;
import static com.ibsanalyzer.inputday.R.id.scoreBar;
import static com.ibsanalyzer.inputday.R.id.scoreName;

/**
 * Created by Johan on 2017-05-01.
 */

public class BmActivity extends EventActivity {
    TextView bristolName;
    SeekBar bristolBar;
    TextView completeName;
    SeekBar completeBar;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bm;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("bristolBar", bristolBar.getProgress());
        outState.putInt("completeBar", completeBar.getProgress());
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
        bristolName = (TextView) findViewById(R.id.bristolName);
        bristolBar = (SeekBar) findViewById(R.id.bristolBar);
        completeName = (TextView) findViewById(R.id.completeName);
        completeBar = (SeekBar) findViewById(R.id.completeBar);

        bristolBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int score = ++progress;
                bristolName.setText(BM.bristolToText(score));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        completeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int score = ++progress;
                completeName.setText(BM.completenessScoreToText(score));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (savedInstanceState != null) {//startvalue is set to 5 if no value exists in savedInstance
            if (savedInstanceState.containsKey("bristolBar")) {
                bristolBar.setProgress(savedInstanceState.getInt("bristolBar"));
            }
            if (savedInstanceState.containsKey("completeBar")) {
                completeBar.setProgress(savedInstanceState.getInt("completeBar"));
            }

        }
    }

    @Override
    public void finish() {
        //scoreBar starts from zero
        int complete = completeBar.getProgress() + 1;
        int bristol = bristolBar.getProgress() + 1;
        BM bm = new BM(datetime, complete, bristol);

        //Put in database here (Android Studio Development Essentials [ASDE] p. 558, 559)


        Gson gson = new Gson();
        String bmAsJSON = gson.toJson(bm);

        Intent data = new Intent();
        data.putExtra("returnBmJSON", bmAsJSON);
        setResult(RESULT_OK, data);
        super.finish();
    }
}