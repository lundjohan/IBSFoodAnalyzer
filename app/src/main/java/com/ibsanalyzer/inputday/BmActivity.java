package com.ibsanalyzer.inputday;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.BM;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.RETURN_BM_JSON;
import static com.ibsanalyzer.constants.Constants.RETURN_BM_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;

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
        Util.eventReturn(bm,RETURN_BM_SERIALIZABLE, this);
        super.finish();
    }
}