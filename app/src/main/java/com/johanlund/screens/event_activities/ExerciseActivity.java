package com.johanlund.screens.event_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.Tag;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.event_activities.common.EventActivity;
import com.johanlund.screens.tag_adder.TagAdderActivity;
import com.johanlund.model.TagType;

import static com.johanlund.constants.Constants.EVENT_TO_CHANGE;
import static com.johanlund.constants.Constants.EXERCISE;
import static com.johanlund.constants.Constants.RETURN_TAG_TEMPLATE_SERIALIZABLE;
import static com.johanlund.constants.Constants.TAGS_TO_ADD;

public class ExerciseActivity extends EventActivity {
    TextView typeOfExercise;
    TextView intensityName;
    SeekBar intensityBar;


    @Override
    protected int getInfoLayout() {
        return R.layout.info_exercise;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_exercise;
    }

    @Override
    protected void buildEvent() {
        //scoreBar starts from zero
        int intensity = intensityBar.getProgress() + 1;
        Tag typeOfExercise = new Tag(getLocalDateTime(), (String) this.typeOfExercise.getText(),
                1.0);
        Exercise event = new Exercise(getLocalDateTime(), getComment(), typeOfExercise, intensity);
        returnEvent(event);
    }

    @Override
    protected String getTitleStr() {
        return "New Exercise";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeOfExercise = (TextView) findViewById(R.id.exercise_type);
        intensityName = (TextView) findViewById(R.id.intensityName);
        intensityBar = (SeekBar) findViewById(R.id.intensityBar);
        intensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int intensity = ++progress;
                intensityName.setText(Exercise.intensityLevelToText(intensity));
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
            if (savedInstanceState.containsKey("seekBar")) {
                intensityBar.setProgress(savedInstanceState.getInt("seekBar"));
            }
        }
        Intent intent = getIntent();
        if (intent.hasExtra(EVENT_TO_CHANGE)) {
            Exercise exercise = (Exercise) intent.getSerializableExtra(EVENT_TO_CHANGE);
            intensityBar.setProgress(exercise.getIntensity() - 1);
            intensityName.setText(Exercise.intensityLevelToText(exercise.getIntensity()));
            String type = exercise.getTypeOfExercise().getName();
            typeOfExercise.setText(type);
        }
    }

    @Override
    protected int getEventType() {
        return EXERCISE;
    }

    public void newTagAdderActivity(View view) {
        Intent intent = new Intent(this, TagAdderActivity.class);
        startActivityForResult(intent, TAGS_TO_ADD);
    }

    //data coming back from TagAdder
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode != TAGS_TO_ADD) {
            return;
        }

        if (data.hasExtra(RETURN_TAG_TEMPLATE_SERIALIZABLE)) {
            TagType tagType = (TagType) data.getExtras().getSerializable
                    (RETURN_TAG_TEMPLATE_SERIALIZABLE);
            typeOfExercise.setText(tagType.get_tagname());
        }
    }
}
