package com.ibsanalyzer.importer;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;

import org.junit.Test;
import org.threeten.bp.Month;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Johan on 2017-06-14.
 */

public class ImportFromTxtTest {

    //2017-04-28T10:50|0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0
    @Test
    public void mealIsCreatedFromLineTest() {
        String mealLine = "2017-04-28T10:50|0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris" +
                "|1.0";
        Meal meal = Importer.lineToMeal(mealLine);
        assertEquals(50, meal.getTime().getMinute());
        assertEquals(0.9, meal.getPortions());
        List<Tag> tags = meal.getTags();
        Tag spenat = tags.get(0);
        assertEquals("spenat", spenat.getName());
        assertEquals(2017, spenat.getTime().getYear());
        assertEquals(1.0, spenat.getSize());
        Tag ris = tags.get(1);
        assertEquals(50, ris.getTime().getMinute());
    }

    //2017-04-27T09:30|2017-04-27T09:30|myntate|1.0|2017-04-27T09:30|ört_te|1.0
    @Test
    public void otherIsCreatedFromLineTest() {
        String line = "2017-04-27T09:30|2017-04-27T09:30|myntate|1.0|2017-04-27T09:30|ört_te|1.0";
        Other other = Importer.lineToOther(line);
        assertEquals(9, other.getTime().getHour());
        List<Tag> tags = other.getTags();
        Tag örtte = tags.get(1);
        assertEquals("ört_te", örtte.getName());
        assertEquals(2017, örtte.getTime().getYear());
        assertEquals(1.0, örtte.getSize());
    }

    //2017-04-27T18:30|2017-04-27T18:30|springer|1.0
    @Test
    public void exerciseIsCreatedFromLineTest() {
        String line = "2017-04-27T18:30|2017-04-27T18:30|springer|1.0";
        Exercise exercise = Importer.lineToExercise(line);
        assertEquals(18, exercise.getTime().getHour());
        Tag t = exercise.getTypeOfExercise();
        assertEquals("springer", t.getName());
        assertEquals(30, t.getTime().getMinute());
        assertEquals(2017, t.getTime().getYear());
        assertEquals(1, exercise.getIntensity());
    }

    //2017-04-27T17:00|7|3
    @Test
    public void bmIsCreatedFromLineTest() {
        String line = "2017-04-27T17:00|7|3";
        Bm bm = Importer.lineToBm(line);
        assertEquals(17, bm.getTime().getHour());
        int complete = bm.getComplete();
        assertEquals(3, complete);
        int bristol = bm.getBristol();
        assertEquals(7, bristol);
    }

    //2017-05-01T17:00|5
    @Test
    public void ratingIsCreatedFromLineTest() {
        String line = "2017-05-01T17:00|5";
        Rating rating = Importer.lineToRating(line);
        assertEquals(Month.MAY, rating.getTime().getMonth());
        int after = rating.getAfter();
        assertEquals(5, after);
    }
}