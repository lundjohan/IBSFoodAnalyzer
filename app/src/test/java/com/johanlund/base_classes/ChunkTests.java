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
    public void testThatNoChunkIsCreatedIfNoEvents() {
        List<Event>events = new ArrayList<>();
        List<Break>breaks = Arrays.asList(new Break(newYear));
        List<Chunk>chunks = Chunk.makeChunksFromEvents(events, breaks);
        assertEquals(0, chunks.size());
    }
}
