package com.johanlund.database;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.johanlund.base_classes.Meal;
import com.johanlund.base_classes.Tag;
import com.johanlund.model.TagType;

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

    private void addTagTemplate(String tagName) {
        TagType tt = new TagType(tagName);
        dbHandler.addTagTemplate(tt);
    }

    private Meal createAMeal(LocalDateTime ldt, String tagName) {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(ldt, "yoghurt", 1));
        return new Meal(ldt, tags, 1);
    }

    @Before
    public void setUp() {
        //clean database
        dbHandler = new DBHandler(InstrumentationRegistry.getTargetContext());
        dbHandler.deleteAllTablesRows();
    }

    @Test
    public void onlyOneTagTemplateWithUniqueNameIsAddedTest() {
        //create a TagType
        TagType tt = new TagType("milk");
        dbHandler.addTagTemplate(tt);

        //TagType Table should only accepts unique names, this should therefore not be added
        TagType tt2 = new TagType("milk");
        dbHandler.addTagTemplate(tt2);

        List<TagType> allTagTypes = dbHandler.getAllTagTemplates();
        assertEquals(1, allTagTypes.size());


    }

    @Test
    public void addAndRetrieveTagTemplateTest() {
        //create a TagType
        addTagTemplate("yoghurt");

        //check that TagType was added.
        TagType ttRetrieved = dbHandler.findTagTemplate("yoghurt");
        assertNotNull(ttRetrieved);

        //check that null is returned if not added tag is searched
        TagType ttWrong = dbHandler.findTagTemplate("not_existing_tag");
        assertNull(ttWrong);
    }

    @Test
    public void addAndRetrieveMealTest() {
        //create a TagType (needed for below)
        addTagTemplate("yoghurt");

        //create a meal
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 24, 10, 38);
        Meal meal = createAMeal(ldt, "yoghurt");

        dbHandler.addMeal(meal);
        Meal retrievedMeal = dbHandler.retrieveMealByTime(ldt);

        assertNotNull(retrievedMeal);
        assertEquals(retrievedMeal.getPortions(), 1.);
        assertEquals(retrievedMeal.getTags().get(0).getName(), "yoghurt");
    }

    @Test
    public void testGetDateFromEvent() {
        //add a meal to database
        addTagTemplate("yoghurt");
        LocalDateTime ldt = LocalDateTime.of(2017, Month.JUNE, 1, 18, 46);
        Meal meal = createAMeal(ldt, "yoghurt");
        dbHandler.addMeal(meal);
        LocalDateTime ldtRetrieved = dbHandler.getDateFromEvent(1);
        assertNotNull(ldtRetrieved);
        assertEquals(ldtRetrieved, LocalDateTime.of(2017, Month.JUNE, 1, 18, 46));
    }

    @Test
    public void testGetAllMeals() {
        //add some meals to database
        addTagTemplate("yoghurt");
        LocalDateTime ldt = LocalDateTime.of(2017, Month.JUNE, 1, 18, 46);
        Meal meal1 = createAMeal(ldt, "yoghurt");
        LocalDateTime ldt2 = LocalDateTime.of(2017, Month.AUGUST, 1, 18, 46);
        Meal meal2 = createAMeal(ldt, "yoghurt");
        LocalDateTime ldt3 = LocalDateTime.of(2017, Month.SEPTEMBER, 1, 18, 46);
        Meal meal3 = createAMeal(ldt, "yoghurt");

        dbHandler.addMeal(meal1);
        dbHandler.addMeal(meal2);
        dbHandler.addMeal(meal3);

        List<Meal> meals = dbHandler.getAllMeals();

        assertEquals(3, meals.size());
        //perhaps some more tests here that confirms that each unique meal has come back
    }

}
