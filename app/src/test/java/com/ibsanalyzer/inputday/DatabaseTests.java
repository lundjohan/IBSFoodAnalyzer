package com.ibsanalyzer.inputday;

import android.content.Context;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.database.DBHandler;

import org.junit.Before;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Johan on 2017-05-24.
 */

public class DatabaseTests {
    DBHandler dbHandler;

    @Before
    public void initiateDB(){
        //could use Context context = InstrumentationRegistry.getTargetContext(); if in AndroidTestfolder.
        //but actually for testing SQLiteOpenHelper can use null as Context.
        dbHandler = new DBHandler(null);
    }
    public void addAndRetrieveMealTest(){
        //create a meal
        LocalDateTime ldt =  LocalDateTime.of(2017, Month.MAY, 24, 10, 38);
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(ldt, "yoghurt",1));
        Meal meal = new Meal(ldt, tags, 1);

        dbHandler.addMeal(meal);
        List<Event> events = dbHandler.retrieveEventsByTime(ldt);
        assertFalse(events.isEmpty());

        Event event = events.get(0);
        assertNotNull(event);

        Meal mealResult = (Meal) event;

        assertEquals(mealResult.getPortions(), 1);
        assertEquals(mealResult.getTags().get(0).getName(), "yoghurt");
    }
}
