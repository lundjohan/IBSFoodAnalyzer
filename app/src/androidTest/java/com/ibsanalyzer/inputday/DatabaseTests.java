package com.ibsanalyzer.inputday;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.model.TagTemplate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Johan on 2017-05-24.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTests {
    DBHandler dbHandler;

    @Before
    public void setUp() {
        //clean database
        dbHandler = new DBHandler(InstrumentationRegistry.getTargetContext());
        dbHandler.deleteAllTablesRows();
    }

    @Test
    public void addAndRetrieveTagTemplateTest(){
        //create a TagTemplate
        TagTemplate tt = new TagTemplate("yoghurt");
        dbHandler.addTagTemplate(tt);

        //check that TagTemplate was added.
        TagTemplate ttRetrieved = dbHandler.findTagTemplate("yoghurt");
        assertNotNull(ttRetrieved);

        //check that null is returned if not added tag is searched
        TagTemplate ttWrong = dbHandler.findTagTemplate("not_existing_tag");
        assertNull(ttWrong);

    }
    @Test
    public void addAndRetrieveMealTest(){
        //create a TagTemplate (needed for below)
        TagTemplate tt = new TagTemplate("yoghurt");
        dbHandler.addTagTemplate(tt);

        //create a meal
        LocalDateTime ldt =  LocalDateTime.of(2017, Month.MAY, 24, 10, 38);
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(ldt, "yoghurt",1));
        Meal meal = new Meal(ldt, tags, 1);

        dbHandler.addMeal(meal);
        Meal retrievedMeal = dbHandler.retrieveMealByTime(ldt);

        assertNotNull(retrievedMeal);
        assertEquals(retrievedMeal.getPortions(), 1.);
        assertEquals(retrievedMeal.getTags().get(0).getName(), "yoghurt");
    }

}
