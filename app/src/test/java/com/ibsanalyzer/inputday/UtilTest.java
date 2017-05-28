package com.ibsanalyzer.inputday;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;
import com.ibsanalyzer.util.InsertPositions;
import com.ibsanalyzer.util.Util;

import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Johan on 2017-05-27.
 */

public class UtilTest {
  /*  @Test
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
        int pos1 = Util.insertEventWithDayMarker(events,rating2);
        assertEquals(0, pos1);  //trivial
        int pos2 = Util.insertEventWithDayMarker(events,rating1);
        assertEquals(0, pos2);  //should have been put before the first insert
        int pos3 = Util.insertEventWithDayMarker(events,rating3);
        assertEquals(2, pos3);  //should been put last of inserts

    }*/
    @Test
    public void dateMarkerEventIsAutomaticallyInsertedAndAlwaysOnRightPlaceInListTest(){
        List<Event> events = new ArrayList<>();
        LocalDate randomDate = LocalDate.of(2017, Month.JANUARY, 1);
        LocalDateTime ldt = LocalDateTime.of(randomDate, LocalTime.MAX);
        int notRelevantRating = 4;
        //1. create a random event (with LocalTime.MAX), for example Rating, insert it with Util.insertEvent...
        Rating rating = new Rating(ldt, notRelevantRating);
        InsertPositions insertPositions = Util.insertEventWithDayMarker(events,rating);

        //2. check that a dateMarkerEvent was created at right position, last amongst events same day.
        assertEquals(2, events.size());
        assertTrue(events.get(1) instanceof DateMarkerEvent);
        assertEquals(rating, events.get(0));

        //3 add another event the same day with same MIN time.
        Util.insertEventWithDayMarker(events,rating);

        //4. check that there is still only one DateMarkerEvent, that it is in first position and that the other two events still exist.
        assertEquals(3, events.size());
        assertTrue(events.get(2) instanceof DateMarkerEvent);
        assertEquals(rating, events.get(0));
        assertEquals(rating, events.get(1));
    }
}
