package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

;

public class Day {
    private LocalDate date;
    private List<Event> events;

    public Day(LocalDate date, List<Event> events) {
        this.date = date;
        this.events = events;
    }

    public List<Tag> getTags() {
        return Day.getTags(events);
    }


    //before this was different than getTags(), now just for legacy.
    //remove this legacy fix later.
    public List<Tag> getInputTags() {
       return getTags(events);
    }


    /**
     * if hoursToCut is minus, then hours will be cut in front, otherwise in end
     */
    public List<Tag> getTags(int hoursToCut) {
        List<Tag> tags = new ArrayList<>();
        if (hoursToCut < 0) {
            for (Event e : this.events) {
                if (e.getTime().getHour() >= hoursToCut) {
                    tags.addAll(Day.getTags(e));
                }
            }
        } else {
            for (Event e : this.events) {
                if (e.getTime().getHour() <= 24 - hoursToCut) {
                    tags.addAll(Day.getTags(e));
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

    public List<Rating> getDividers() {
        List<Rating> divs = new ArrayList<>();
        for (Event event : events) {
            if (event instanceof Rating) {
                divs.add((Rating) event);
            }
        }
        return divs;
    }

    @Override
    public String toString() {
        return date.toString();

    }

    private static List<Tag> getTags(List<Event> events) {
        List<Tag> inputTags = new ArrayList<>();
        for (Event e : events) {
            if (e instanceof InputEvent) {
                inputTags.addAll(((InputEvent) e).getTags());
            }
        }
        return inputTags;
    }
    private static List<Tag> getTags(Event e) {
        List<Tag> inputTags = new ArrayList<>();
            if (e instanceof InputEvent) {
                inputTags.addAll(((InputEvent) e).getTags());
            }
        return inputTags;
    }
}
