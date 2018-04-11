package com.johanlund.base_classes;

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
}
