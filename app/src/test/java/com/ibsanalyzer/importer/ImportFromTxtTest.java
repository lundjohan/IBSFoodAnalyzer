package com.ibsanalyzer.importer;

import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.importer.Importer;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Johan on 2017-06-14.
 */

public class ImportFromTxtTest {

    //2017-04-28T10:50|0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0
    @Test
    public void mealIsCreatedFromLineTest() {
        String mealLine = "2017-04-28T10:50|0.9|2017-04-28T10:50|spenat|1.0|2017-04-28T10:50|ris|1.0";
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
}