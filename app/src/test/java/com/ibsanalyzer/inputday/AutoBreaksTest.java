package com.ibsanalyzer.inputday;

import com.ibsanalyzer.base_classes.Break;
import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Johan on 2017-10-05.
 */

public class AutoBreaksTest {

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


        List<Break> breaks = Event.makeBreaks(events, 10);


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
}
