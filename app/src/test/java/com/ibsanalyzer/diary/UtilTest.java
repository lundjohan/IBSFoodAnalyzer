package com.ibsanalyzer.diary;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;
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
    public void dateMarkerEventIsAutomaticallyInsertedAndAlwaysOnRightPlaceInListTest() {
        List<Event> events = new ArrayList<>();
        LocalDate randomDate = LocalDate.of(2017, Month.JANUARY, 1);
        LocalDateTime ldt = LocalDateTime.of(randomDate, LocalTime.MAX);
        int notRelevantRating = 4;
        //1. create a random event (with LocalTime.MAX), for example Rating, insert it with Util
        // .insertEvent...
        Rating rating = new Rating(ldt, notRelevantRating);
        InsertPositions insertPositions = Util.insertEventWithDayMarker(events, rating);

        //2. check that a dateMarkerEvent was created at right position, last amongst events same
        // day.
        assertEquals(2, events.size());
        assertTrue(events.get(1) instanceof DateMarkerEvent);
        assertEquals(rating, events.get(0));

        //3 add another event the same day with same MIN time.
        Util.insertEventWithDayMarker(events, rating);

        //4. check that there is still only one DateMarkerEvent, that it is in first position and
        // that the other two events still exist.
        assertEquals(3, events.size());
        assertTrue(events.get(2) instanceof DateMarkerEvent);
        assertEquals(rating, events.get(0));
        assertEquals(rating, events.get(1));
    }

    @Test
    public void addDateEventToListTest() {
        //Create 4 Rating events, with 3 different dates.
        // => 3 DateMarkerEvent should be insserted

        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.AUGUST, 1, 14, 0);
        LocalDateTime ldt2 = LocalDateTime.of(2017, Month.AUGUST, 4, 14, 0);
        LocalDateTime ldt3 = LocalDateTime.of(2017, Month.AUGUST, 10, 14, 0);
        //1. Rating
        Rating r1 = new Rating(ldt1, 4);
        Rating r2 = new Rating(ldt1, 4);
        Rating r3 = new Rating(ldt2, 4);
        Rating r4 = new Rating(ldt3, 4);

        List<Event> eventList = new ArrayList<>();
        eventList.add(r1);
        eventList.add(r2);
        eventList.add(r3);
        eventList.add(r4);

        Util.addDateEventsToList(eventList);

        //size ok?
        //4 rating + 3 datemarkers = 7 events
        assertEquals(eventList.size(), 7);

        //at right place? DateMarkers comes at latest place of day (just before 24:00)
        assertEquals(eventList.size(), 7);

        //index 0, 1 are rating events
        assertEquals(eventList.get(2).getClass(), DateMarkerEvent.class);
        //3 rating event
        assertEquals(eventList.get(4).getClass(), DateMarkerEvent.class);
        //5 rating event
        assertEquals(eventList.get(6).getClass(), DateMarkerEvent.class);
    }
}
