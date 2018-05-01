package com.johanlund.stat_classes;

import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Rating;
import com.johanlund.base_classes.Tag;
import com.johanlund.statistics_avg.TagsWrapper;
import com.johanlund.statistics_general.ScoreWrapperBase;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.TagsWrapperBase;

import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

//tests are transerred from Chunktests, just slightly modified.
//One difference from Chunk, is that .makeTagsWrappers requiers a finishing break for last chunkend.

//When it says "Chunk" it should really be "TagsWrapper"
public class TagsWrapperTest {
    private static LocalDateTime newYear = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0);

    @Test
    public void testThatNoChunkIsCreatedIfNoEventsButOneBreak() {
        List<LocalDateTime>breaks = asList(newYear);
        List<TagsWrapperBase>tw = TagsWrapper.makeTagsWrappers(new ArrayList<Tag>(), new ArrayList
                <ScoreTime>(), breaks);
        assertEquals(0, tw.size());
    }
    @Test
    public void testEventsBecomesTwoChunks() {
        ScoreTime beforeSplit = new ScoreTime(newYear, 3);
        LocalDateTime splitter = newYear.plusHours(1);
        ScoreTime afterSplit = new ScoreTime(newYear.plusHours(2), 3);
        LocalDateTime chunkEnd = newYear.plusHours(3);

        List<TagsWrapperBase>chunks = TagsWrapper.makeTagsWrappers(new ArrayList<Tag>(), asList(beforeSplit, afterSplit), asList(splitter, chunkEnd));

        assertEquals(2, chunks.size());
        assertEquals(newYear.plusHours(3), chunks.get(1).getChunkEnd());
    }

}
