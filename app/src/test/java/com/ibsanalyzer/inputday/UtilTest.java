package com.ibsanalyzer.inputday;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.util.Util;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

import static android.R.string.no;
import static org.junit.Assert.assertEquals;

/**
 * Created by Johan on 2017-05-27.
 */

public class UtilTest {
    @Test
    public void listGetInsertedByDateTimeOrder(){

        //Create some dates in order
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1,1,0);
        LocalDateTime ldt2 = LocalDateTime.of(2017, Month.FEBRUARY, 1,1,0);
        LocalDateTime ldt3 = LocalDateTime.of(2017, Month.MARCH, 1,1,0);

        int notRelevantRating = 4;

        //create a couple of Events, for example Rating because it is simple
        Rating rating1 = new Rating(ldt1, notRelevantRating);
        Rating rating2 = new Rating(ldt2, notRelevantRating);
        Rating rating3 = new Rating(ldt3, notRelevantRating);

        List<Event> events = new ArrayList<>();
        int pos1 = Util.insertEventByDateTimeOrder(events,rating2);
        assertEquals(0, pos1);  //trivial
        int pos2 = Util.insertEventByDateTimeOrder(events,rating1);
        assertEquals(0, pos2);  //should have been put before the first insert
        int pos3 = Util.insertEventByDateTimeOrder(events,rating3);
        assertEquals(2, pos3);  //should been put last of inserts

    }

}
