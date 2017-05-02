package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDate;;
import java.util.ArrayList;
import java.util.List;

public class Day {
	private LocalDate date;
	private List<Event> events;

	public Day(LocalDate date, List<Event> events) {
		this.date = date;
		this.events = events;
	}
	public List<Tag> getTags() {
		List<Tag> tags = new ArrayList<>();
		for (Event e : this.events) {
			tags.addAll(e.getTags());
		}
		return tags;
	}
	public List<Tag> getInputTags() {
		List<Tag> tags = new ArrayList<>();
		for (Event e : this.events) {
			tags.addAll(e.getInputTags());
		}
		return tags;
	}



	/**
	 * if hoursToCut is minus, then hours will be cut in front, otherwise in end
	 */
	public List<Tag> getTags(int hoursToCut) {
		List<Tag> tags = new ArrayList<>();
		if (hoursToCut < 0) {
			for (Event e : this.events) {
				if (e.getTime().getHour() >= hoursToCut) {
					tags.addAll(e.getTags());
				}
			}
		}

		else {
			for (Event e : this.events) {
				if (e.getTime().getHour() <= 24 - hoursToCut) {
					tags.addAll(e.getTags());
				}
			}
		}
		return tags;
	}
	// ==================HEAVY COPY PASTING OF CODE FOR THESE getEventClasses!!!
	// => CHANGE! ========================
	public List<BM> getBMs() {
		List<BM> BMs = new ArrayList<>();
		for (Event event : events) {
			if (event instanceof BM) {
				BMs.add((BM) event);
			}
		}
		return BMs;
	}
	public List<Meal> getMeals() {
		List<Meal> Meals = new ArrayList<>();
		for (Event event : events) {
			if (event instanceof Meal) {
				Meals.add((Meal) event);
			}
		}
		return Meals;
	}
	public LocalDate getDate() {
		return date;
	}
	public List<Exercise> getExercises() {
		List<Exercise> exercises = new ArrayList<>();
		for (Event event : events) {
			if (event instanceof Exercise) {
				exercises.add((Exercise) event);
			}
		}
		return exercises;
	}
	public List<Other> getOthers() {
		List<Other> others = new ArrayList<>();
		for (Event event : events) {
			if (event instanceof Other) {
				others.add((Other) event);
			}
		}
		return others;
	}
	public List<Score> getDividers() {
		List<Score> divs = new ArrayList<>();
		for (Event event : events) {
			if (event instanceof Score) {
				divs.add((Score) event);
			}
		}
		return divs;
	}
	// ==========================CHANGE
	// ABOVE!!!==============================================
	public List<Tag> getAllTags() {
		List<Tag> allTags = new ArrayList<>();
		for (Event e : events) {
			allTags.addAll(e.getTags());
		}
		return allTags;
	}
	@Override
	public String toString(){
		return date.toString();

	}
}
