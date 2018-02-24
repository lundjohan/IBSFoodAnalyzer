package com.johanlund.base_classes;

import com.johanlund.constants.Constants;

import org.threeten.bp.LocalDateTime;

public class Exercise extends Event {
    private Tag typeOfExercise;
    //from 1 to 5
    private int intensity;

    public Exercise(LocalDateTime time, Tag typeOfExercise, int intensity) {
        super(time);
        this.typeOfExercise = typeOfExercise;
        this.intensity = intensity;
    }
    public Exercise(LocalDateTime time, String comment, Tag typeOfExercise, int intensity) {
        super(time, comment);
        this.typeOfExercise = typeOfExercise;
        this.intensity = intensity;
    }

    public Exercise(LocalDateTime ldt, String comment, boolean hasBreak, Tag t, int intensity) {
        super(ldt, comment, hasBreak);
        this.typeOfExercise = t;
        this.intensity = intensity;
    }

    public static String intensityLevelToText(int score) {
        String text = "OUT OF RANGE";
        switch (score) {
            case 1:
                text = "Relaxed";
                break;
            case 2:
                text = "Light";
                break;
            case 3:
                text = "Moderate";
                break;
            case 4:
                text = "Intense";
                break;
            case 5:
                text = "Very Intense";
                break;
        }
        return text;
    }

    public Tag getTypeOfExercise() {
        return typeOfExercise;
    }

    public int getIntensity() {
        return intensity;
    }

    @Override
    public int getType() {
        return Constants.EXERCISE;
    }
}