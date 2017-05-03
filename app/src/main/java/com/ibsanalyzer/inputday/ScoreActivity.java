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

public class ScoreActivity extends EventActivity {
    TextView scoreName;
    EditText score;
    SeekBar scoreBar;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_score;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("seekBar", scoreBar.getProgress());
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
        scoreName = (TextView) findViewById(R.id.scoreName);
        scoreBar = (SeekBar) findViewById(R.id.scoreBar);
        scoreBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Debug", "inside onProgressChanged");
                int score = ++progress;
                scoreName.setText(Score.pointsToText(score));
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
        }
    }





    @Override
    public void finish() {
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
    }
}