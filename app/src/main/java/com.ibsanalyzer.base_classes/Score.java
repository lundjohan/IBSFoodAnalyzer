package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;;
import java.util.ArrayList;

//this class is an exception from EventModel rule: it does not use Tags.
public class Score extends Event {
	int after;
	public Score(LocalDateTime time, int after) {
		super(time, new ArrayList<Tag>());
		this.after = after;
	}
	public int getAfter() {
		return after;
	}

}
