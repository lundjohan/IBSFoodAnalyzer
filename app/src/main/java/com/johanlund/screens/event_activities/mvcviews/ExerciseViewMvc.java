package com.johanlund.screens.event_activities.mvcviews;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.ibsfoodanalyzer.R;

import org.threeten.bp.LocalDateTime;

import java.util.Arrays;
import java.util.List;

public class ExerciseViewMvc extends EventViewMvcAbstract {
    TextView typeOfExercise;
    TextView intensityName;
    SeekBar intensityBar;

    public ExerciseViewMvc(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
    }

    @Override
    protected void initializeSpecViews() {
        typeOfExercise = (TextView) rootView.findViewById(R.id.exercise_type);
        intensityName = (TextView) rootView.findViewById(R.id.intensityName);
        intensityBar = (SeekBar) rootView.findViewById(R.id.intensityBar);
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
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_exercise;
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.info_exercise;
    }

    @Override
    protected String getBarTitle() {
        return "New Exercise";
    }

    @Override
    protected void bindEventSpecsToView(Event e) {
        Exercise exercise = (Exercise) e;
        intensityBar.setProgress(exercise.getIntensity() - 1);
        intensityName.setText(Exercise.intensityLevelToText(exercise.getIntensity()));
        String type = exercise.getTypeOfExercise().getName();
        typeOfExercise.setText(type);
    }

    @Override
    protected Event makeEventFromView(LocalDateTime ldt, String comment) {
        return new Exercise(ldt, comment, eventHasBreak, new TagWithoutTime(typeOfExercise.getText().toString(),1.0), intensityBar.getProgress());
    }

    @Override
    public void bindAddedTagToView(String tagName) {
        typeOfExercise.setText(tagName);
    }

    @Override
    public void removeTagFromView(String tagName) {
        typeOfExercise.setText(context.getResources().getString(R.string.type_of_exercise));
    }

    @Override
    public List<String> getTagNames() {
        return Arrays.asList(typeOfExercise.getText().toString());
    }
}
