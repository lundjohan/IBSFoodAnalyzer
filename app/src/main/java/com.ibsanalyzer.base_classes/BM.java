package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class BM extends Event {
	//private double size;
	private int bristol;
	private int complete = 0;

	public BM(LocalDateTime time,int complete, int bristol) {
		super(time, new ArrayList<Tag>());
		//preliminary solution, better to have complete handling in import.
		//this.size = size;
		this.bristol = bristol;
	}

	/*public double getSize() {
		return size;
	}*/
	public double getBristol() {
		return bristol;
	}

	/**
	 * BM tags are, in general, not input-tags.
	 */
	@Override
	public List<Tag>getInputTags(){
		List<Tag>toReturn = new ArrayList<>();
		List<Tag>allTags = getTags();
		for (Tag t:allTags){
			if (t.getName().equalsIgnoreCase("squat") || t.getName().equalsIgnoreCase("squatting")) {
				toReturn.add(t);
			}
		}
		return toReturn;
	}

	public int getComplete() {
		return complete;
	}

	public static String completenessScoreToText(int score){
		String text = "OUT OF RANGE";
		switch(score){
			case 1:
				text = "Abysmal";
				break;
			case 2:
				text = "Bad";
				break;
			case 3:
				text = "Deficient";
				break;
			case 4:
				text = "Good";
				break;
			case 5:
				text = "Phenomenal";
				break;
		}
		return text;
	}
}
