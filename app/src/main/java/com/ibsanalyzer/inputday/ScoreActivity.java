package com.ibsanalyzer.inputday;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Score;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.RETURN_SCORE_JSON;

/**
 * Created by Johan on 2017-05-01.
 */

public class ScoreActivity extends EventActivity {
    TextView scoreName;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scoreName = (TextView) findViewById(R.id.intensityName);
        scoreBar = (SeekBar) findViewById(R.id.intensityBar);
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
        //scoreBar starts from zero
        int after = scoreBar.getProgress() + 1;
        Score score = new Score(datetime, after);

        //Put in database here (Android Studio Development Essentials [ASDE] p. 558, 559)

        Util.jsonAndMoreFinishingData(score,RETURN_SCORE_JSON, this);

    }
}