package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;;
import java.util.ArrayList;

import static com.ibsanalyzer.inputday.R.id.scoreName;

//this class is an exception from EventModel rule: it does not use Tags.
public class Score extends Event {
	private int after;
	public Score(LocalDateTime time, int after) {
		super(time, new ArrayList<Tag>());
		this.after = after;
	}
	public int getAfter() {
		return after;
	}
	public static String pointsToText(int score){
		String text = "OUT OF RANGE";
		switch(score){
			case 1:
				text = "Abysmal";
				break;
			case 2:
				text = "Awful";
				break;
			case 3:
				text = "Bad";
				break;
			case 4:
				text = "Deficient";
				break;
			case 5:
				text = "Good";
				break;
			case 6:
				text = "Great";
				break;
			case 7:
				text = "Phenomenal";
				break;
		}
		return text;
	}
}
