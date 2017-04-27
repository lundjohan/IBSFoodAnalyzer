package com.ibsanalyzer.model;

import android.os.Parcel;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;

import java.util.List;

;

public class MealModel extends Event {
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