package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.model.TagTemplate;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_JSON;
import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.TAGS_TO_ADD;

public class ExerciseActivity extends EventActivity {
    TextView typeOfExercise;
    TextView intensityName;
    SeekBar intensityBar;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_exercise;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeOfExercise = (TextView)findViewById(R.id.exercise_type);
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

        if (savedInstanceState != null) {//startvalue is set to 5 if no value exists in savedInstance
            if (savedInstanceState.containsKey("seekBar")) {
                intensityBar.setProgress(savedInstanceState.getInt("seekBar"));
            }
        }
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
        Gson gson = new Gson();
        if (data.hasExtra("returnTagTemplateJSON")) {
            String tagJSONData = data.getExtras().getString("returnTagTemplateJSON");
            TagTemplate tagTemplate = gson.fromJson(tagJSONData, TagTemplate.class);
            typeOfExercise.setText(tagTemplate.get_tagname());
        }
    }


    @Override
    public void finish() {
        //scoreBar starts from zero
        int intensity = intensityBar.getProgress() + 1;
        Tag typeOfExercise = new Tag(datetime, (String) this.typeOfExercise.getText(), 1.0);
        Exercise exercise = new Exercise(datetime, typeOfExercise, intensity);
        Util.eventReturn(exercise,RETURN_EXERCISE_SERIALIZABLE, this);
    }
}
