package com.ibsanalyzer.model;

import com.ibsanalyzer.base_classes.InputEvent;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;

import java.util.List;

;

public class MealModel extends InputEvent {
	private double portions;

	public MealModel(LocalDateTime time, List<Tag> tags, double portions) {
		super(time, tags);
		this.portions = portions;
	}

	public double getPortions() {
		return portions;
	}

	public void setPortions(double portions) {
		this.portions = portions;
	}
}