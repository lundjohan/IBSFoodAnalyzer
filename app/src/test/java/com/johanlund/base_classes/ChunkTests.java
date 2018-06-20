package com.johanlund.base_classes;

import com.johanlund.stat_backend.stat_util.Chunk;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ChunkTests {
    private static LocalDateTime newYear = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0);
    @Test
    public void testThatNoChunkIsCreatedIfNoEventsButOneBreak() {
        List<Event>events = new ArrayList<>();
        List<Break>breaks = Arrays.asList(new Break(newYear));
        List<Chunk>chunks = Chunk.makeChunksFromEvents(events, breaks);
        assertEquals(0, chunks.size());
    }
    @Test
    public void testEventsBecomesTwoChunks() {
        Rating beforeSplit = new Rating(newYear, 3);
        Break splitter = new Break(newYear.plusHours(1));
        Rating afterSplit = new Rating(newYear.plusHours(2), 3);

        List<Event>events = new ArrayList<>();
        events.add(beforeSplit);
        events.add(afterSplit);
        List<Break>breaks = Arrays.asList(splitter);
        List<Chunk>chunks = Chunk.makeChunksFromEvents(events, breaks);

        assertEquals(2, chunks.size());
    }
    @Test
    public void testEventsBecomeOneChunk() {
        Rating beforeSplit1 = new Rating(newYear, 3);
        Rating beforeSplit2 = new Rating(newYear.plusHours(1), 3);
        Break splitter = new Break(newYear.plusHours(2));

        List<Event>events = new ArrayList<>();
        events.add(beforeSplit1);
        events.add(beforeSplit2);
        List<Break>breaks = Arrays.asList(splitter);
        List<Chunk>chunks = Chunk.makeChunksFromEvents(events, breaks);

        assertEquals(1, chunks.size());
        assertEquals(2, chunks.get(0).getEvents().size());
    }

    @Test
    public void breakAtSameTimeAsEventActsAsIfAfterEvent() {
        Rating rStart = new Rating(newYear, 3);
        Break bSameTime = new Break(newYear);
        Rating rAfterB = new Rating(newYear.plusHours(1), 3);

        List<Event>events = new ArrayList<>();
        events.add(rStart);
        events.add(rAfterB);
        List<Break>breaks = Arrays.asList(bSameTime);
        List<Chunk>chunks = Chunk.makeChunksFromEvents(events, breaks);

        assertEquals(2, chunks.size());
        assertEquals(1, chunks.get(0).getEvents().size());
        assertEquals(1, chunks.get(1).getEvents().size());
    }
    @Test
    public void canMakeThreeChunks() {
        Rating rStart = new Rating(newYear, 3);
        Break bAfterStart = new Break(newYear.plusHours(1));
        Rating rMiddle = new Rating(newYear.plusHours(2), 3);
        Break bBeforeEnd = new Break(newYear.plusHours(3));
        Rating rEnd = new Rating(newYear.plusHours(4), 3);

        List<Event>events = new ArrayList<>();
        events.add(rStart);
        events.add(rMiddle);
        events.add(rEnd);

        List<Break>breaks = Arrays.asList(bAfterStart, bBeforeEnd);
        List<Chunk>chunks = Chunk.makeChunksFromEvents(events, breaks);

        assertEquals(3, chunks.size());
        assertEquals(1, chunks.get(0).getEvents().size());
        assertEquals(1, chunks.get(1).getEvents().size());
        assertEquals(1, chunks.get(2).getEvents().size());
    }
    //this case can actually happen if 2 events have same time, and they both have breaks set on them.
    @Test
    public void twoBreaksBetweenEventsShouldActLikeTheyWereOne() {
        Rating rStart1 = new Rating(newYear, 3);
        Rating rStart2 = new Rating(newYear, 3);
        Break bAfterStart1 = new Break(newYear);
        Break bAfterStart2 = new Break(newYear);
        Rating rMiddle = new Rating(newYear.plusHours(2), 3);
        Break bBeforeEnd = new Break(newYear.plusHours(3));
        Rating rEnd = new Rating(newYear.plusHours(4), 3);

        List<Event>events = new ArrayList<>();
        events.add(rStart1);
        events.add(rStart2);
        events.add(rMiddle);
        events.add(rEnd);
        List<Break>breaks = Arrays.asList(bAfterStart1, bAfterStart2, bBeforeEnd);
        List<Chunk>chunks = Chunk.makeChunksFromEvents(events, breaks);

        assertEquals(3, chunks.size());
        assertEquals(2, chunks.get(0).getEvents().size());
        assertEquals(1, chunks.get(1).getEvents().size());
        assertEquals(1, chunks.get(2).getEvents().size());
    }
}
