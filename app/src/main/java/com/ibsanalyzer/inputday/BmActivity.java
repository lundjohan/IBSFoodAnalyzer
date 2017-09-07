package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Bm;

import static com.ibsanalyzer.constants.Constants.EVENT_TO_CHANGE;
import static com.ibsanalyzer.constants.Constants.RETURN_BM_SERIALIZABLE;

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
                bristolName.setText(Bm.bristolToText(score));
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
                completeName.setText(Bm.completenessScoreToText(score));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (savedInstanceState != null) {//startvalue is set to 5 if no value exists in
            // savedInstance
            if (savedInstanceState.containsKey("bristolBar")) {
                bristolBar.setProgress(savedInstanceState.getInt("bristolBar"));
            }
            if (savedInstanceState.containsKey("completeBar")) {
                completeBar.setProgress(savedInstanceState.getInt("completeBar"));
            }

        }
        //event to be changed?
        Intent intent = getIntent();
        if (intent.hasExtra(EVENT_TO_CHANGE)) {
            Bm bm = (Bm) intent.getSerializableExtra(EVENT_TO_CHANGE);
            bristolBar.setProgress(bm.getBristol() - 1);
            bristolName.setText((Bm.bristolToText(bm.getBristol())));
            completeBar.setProgress(bm.getComplete() - 1);
            completeName.setText((Bm.bristolToText(bm.getComplete())));
        }
    }

    @Override
    public void finish() {
        //scoreBar starts from zero
        int complete = completeBar.getProgress() + 1;
        int bristol = bristolBar.getProgress() + 1;
        Bm bm = new Bm(getLocalDateTime(), complete, bristol);
        returnEvent(bm, RETURN_BM_SERIALIZABLE);
    }
}