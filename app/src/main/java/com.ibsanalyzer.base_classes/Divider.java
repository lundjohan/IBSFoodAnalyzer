package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;;
import java.util.ArrayList;

//this class is an exception from Event rule: it does not use Tags.
public class Divider extends Event {
	double after;
	public Divider(LocalDateTime time, double after) {
		super(time, new ArrayList<Tag>());
		this.after = after;
	}
	public double getAfter() {
		return after;
	}

}
