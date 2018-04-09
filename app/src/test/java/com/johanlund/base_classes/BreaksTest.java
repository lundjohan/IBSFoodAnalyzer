package com.johanlund.base_classes;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Johan on 2017-10-05.
 */

public class BreaksTest {

    /**
     * this test checks that autoBreaks method in Event class works properly.
     * breaks should be added or removed depending on hours between events.
     *
     * Also checks that Chunk division is made accordingly
     */
    @Test
    public void autoBreaksTest() {
        //empty tags list
        List<Tag> tagsList = new ArrayList<>();

        //create a list with some events
        List<Event> events = new ArrayList<>();

        //start
        Event event1 = new Meal(LocalDateTime.of(2017, 10, 5, 7, 0), tagsList, 1);

        //3 hours after. BREAK at this time
        Event event2 = new Rating(LocalDateTime.of(2017, 10, 5, 10, 0), 3);

        //11 hours after
        Event event3 = new Rating(LocalDateTime.of(2017, 10, 5, 21, 0), 3);

        //1 hour after
        Event event4 = new Rating(LocalDateTime.of(2017, 10, 5, 22, 0), 3);

        //same time. BREAK at this time.
        Event event5 = new Meal(LocalDateTime.of(2017, 10, 5, 22, 0), tagsList, 1);

        //days after, should not have break. It is not needed in end.
        Event event6 = new Rating(LocalDateTime.of(2017, 10, 8, 22, 0), 3);

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);


        List<Break> breaks = Break.makeAutoBreaks(events, 10);


        assertEquals(2, breaks.size());
        assertEquals(LocalDateTime.of(2017, 10, 5, 10, 0), breaks.get(0).getTime());
        assertEquals(LocalDateTime.of(2017, 10, 5, 22, 0), breaks.get(1).getTime());

        //now check that Chunks are made accordingly
        List<Chunk> chunks = Chunk.makeChunksFromEvents(events, breaks);

        //Chunk 1: event1 + event2
        //Chunk 2: event3 + 4 +5
        //Chunk 3: event6

        assertEquals(3, chunks.size());
        assertEquals(2, chunks.get(0).getEvents().size());
        assertEquals(3, chunks.get(1).getEvents().size());
        assertEquals(1, chunks.get(2).getEvents().size());
    }
    @Test
    public void manualBreaksTest() {
        //empty tags list
        List<Tag> tagsList = new ArrayList<>();

        //create a list with some events
        List<Event> events = new ArrayList<>();

        //start
        Event event1 = new Meal(LocalDateTime.of(2017, 10, 5, 7, 0), tagsList, 1);
        event1.setHasBreak(true);

        //3 hours after.
        Event event2 = new Rating(LocalDateTime.of(2017, 10, 5, 10, 0), 3);
        event2.setHasBreak(true);

        //11 hours after
        Event event3 = new Rating(LocalDateTime.of(2017, 10, 5, 21, 0), 3);

        //1 hour after
        Event event4 = new Rating(LocalDateTime.of(2017, 10, 5, 22, 0), 3);
        event4.setHasBreak(true);

        //1 hour after again.
        Event event5 = new Meal(LocalDateTime.of(2017, 10, 5, 23, 0), tagsList, 1);

        //days after
        Event event6 = new Rating(LocalDateTime.of(2017, 10, 8, 22, 0), 3);

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);


        List<Break> manualBreaks = Break.getManualBreaks(events);
        //should be 3 breaks

        assertEquals(3, manualBreaks.size());
        //event 1
        assertEquals(LocalDateTime.of(2017, 10, 5, 7, 0), manualBreaks.get(0).getTime());
        //event 2
        assertEquals(LocalDateTime.of(2017, 10, 5, 10, 0), manualBreaks.get(1).getTime());
        //event4
        assertEquals(LocalDateTime.of(2017, 10, 5, 22, 0), manualBreaks.get(2).getTime());

        //now check that Chunks are made accordingly
        List<Chunk> chunks = Chunk.makeChunksFromEvents(events, manualBreaks);

        //Chunk 1: event1
        //Chunk 2: event2
        //Chunk 3: event3 +4
        //Chunk 4: event5 +6


        assertEquals(4, chunks.size());
        assertEquals(1, chunks.get(0).getEvents().size());
        assertEquals(1, chunks.get(1).getEvents().size());
        assertEquals(2, chunks.get(2).getEvents().size());
        assertEquals(2, chunks.get(3).getEvents().size());
    }
    @Test
    //tests the method that combines autoBreaks and manualBreaks tests commutativity (1+3 == 3+1)
    public void allBreaksTest() {
        //create a list of events with some manual breaks
        Event event1 = new Rating(LocalDateTime.of(2017, 10, 5, 10, 0), 3);
        event1.setHasBreak(true); //MANUAL BREAK

        //1 hours after
        Event event2 = new Rating(LocalDateTime.of(2017, 10, 5, 11, 0), 3);

        //AUTO BREAK

        //11 hour after
        Event event3 = new Rating(LocalDateTime.of(2017, 10, 5, 22, 0), 3);

        //1 hour after
        Event event4 = new Rating(LocalDateTime.of(2017, 10, 5, 23, 0), 3);

        List<Event>events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);


        List<Break>autoBreaks = Break.makeAutoBreaks(events, 5);

        //hours ahead 5 < 11 hours after bteween 2 and 3 event => 1 break
        assertEquals(1, autoBreaks.size());

        //autobreak should occur just after event2.
        assertEquals(LocalDateTime.of(2017, 10, 5, 11, 0), autoBreaks.get(0).getTime());

        //now get the manual break (should be at event1)
        List<Break>manualBreaks = Break.getManualBreaks(events);
        //hours ahead 5 < 11 hours after bteween 2 and 3 event => 1 break
        assertEquals(1, manualBreaks.size());
        assertEquals(LocalDateTime.of(2017, 10, 5, 10, 0), manualBreaks.get(0).getTime());

        //now test that the method for adding manual- and autobreaks works
        List<Break>allBreaks = Break.makeAllBreaks(events, 5);
        assertEquals(2, allBreaks.size());

        //list should be sorted by DateTime in ASC order , check that that is so...
        assertEquals(true, allBreaks.get(0).getTime().isBefore(allBreaks.get(1).getTime()));

        //check commutativity (1+2 == 2+1) for sort order by...
        //...A. ...this time placing manual break AFTER of auto break (before manual was before).
        event1.setHasBreak(false);
        event3.setHasBreak(true);

        List<Break>allBreaksStillInAscOrder = Break.makeAllBreaks(events, 5);
        assertEquals(2, allBreaksStillInAscOrder.size());
        assertEquals(true, allBreaksStillInAscOrder.get(0).getTime().isBefore(allBreaksStillInAscOrder.get(1).getTime()));




    }
}
