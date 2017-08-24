package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

public class Exercise extends Event {
	private Tag typeOfExercise;
	//from 1 to 5
	private int intensity;

	public Exercise(LocalDateTime time, Tag typeOfExercise, int intensity){
		super(time);
		this.typeOfExercise = typeOfExercise;
		this.intensity = intensity;
	}

    public Tag getTypeOfExercise() {
        return typeOfExercise;
    }

    public int getIntensity() {
		return intensity;
	}

	public static String intensityLevelToText(int score){
		String text = "OUT OF RANGE";
		switch(score){
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
}