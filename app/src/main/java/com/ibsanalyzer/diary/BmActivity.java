package com.ibsanalyzer.diary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Bm;

import static com.ibsanalyzer.constants.Constants.BM;
import static com.ibsanalyzer.constants.Constants.EVENT_TO_CHANGE;

/**
 * Created by Johan on 2017-05-01.
 */

public class BmActivity extends EventActivity {
    TextView bristolName;
    SeekBar bristolBar;
    TextView completeName;
    SeekBar completeBar;

    @Override
    protected int getInfoLayout() {
        return R.layout.activity_bm_info;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bm;
    }

    @Override
    protected void buildEvent() {
        //scoreBar starts from zero
        int complete = completeBar.getProgress() + 1;
        int bristol = bristolBar.getProgress() + 1;
        Bm bm = new Bm(getLocalDateTime(), getComment(), complete, bristol);
        returnEvent(bm);
    }

    @Override
    protected String getTitleStr() {
        return "New BM";
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
                setBristolNrAndText(bristolName, score);
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
            setBristolNrAndText(bristolName, bm.getBristol());
            completeBar.setProgress(bm.getComplete() - 1);
            completeName.setText((Bm.bristolToText(bm.getComplete())));
        }

    }

    @Override
    protected int getEventType() {
        return BM;
    }

    private static void setBristolNrAndText(TextView v, int bristolScore) {
        v.setText("("+bristolScore + ") "+ Bm.bristolToText(bristolScore));
    }
}