package com.johanlund.base_classes;

import com.johanlund.constants.Constants;

import org.jetbrains.annotations.Nullable;
import org.threeten.bp.LocalDateTime;

/**
 * typeOfExercise is nullable, and design elsewhere in code be reflected that.
 */
public class Exercise extends Event {
    @Nullable
    private TagWithoutTime typeOfExercise;
    //from 1 to 5
    private int intensity;

    public Exercise(LocalDateTime time, TagWithoutTime typeOfExercise, int intensity) {
        super(time);
        this.typeOfExercise = typeOfExercise;
        this.intensity = intensity;
    }

    public Exercise(LocalDateTime time, String comment, TagWithoutTime typeOfExercise, int
            intensity) {
        super(time, comment);
        this.typeOfExercise = typeOfExercise;
        this.intensity = intensity;
    }

    public Exercise(LocalDateTime ldt, String comment, boolean hasBreak, TagWithoutTime t, int
            intensity) {
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

    @Nullable
    public TagWithoutTime getTypeOfExercise() {
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