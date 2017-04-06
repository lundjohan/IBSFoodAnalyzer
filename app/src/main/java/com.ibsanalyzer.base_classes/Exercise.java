package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;;
import java.util.List;

public class Exercise extends Event {
	private double intensity;

	public Exercise(){
		super(null,null);
		intensity = 0;
	}
	public Exercise(LocalDateTime time, List<Tag> tags, double intensity) {
		super(time, tags);
		this.intensity = intensity;
	}
	public double getIntensity() {
		return intensity;
	}
}