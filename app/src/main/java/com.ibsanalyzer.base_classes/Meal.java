package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;;
import java.util.List;

public class Meal extends Event {
	private double portions;

	public Meal(LocalDateTime time, List<Tag> tags, double portions) {
		super(time, tags);
		this.portions = portions;
	}

	public double getPortions() {
		return portions;
	}


}