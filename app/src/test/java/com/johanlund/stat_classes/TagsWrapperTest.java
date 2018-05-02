package com.johanlund.stat_classes;

import com.johanlund.base_classes.Tag;
import com.johanlund.statistics_avg.TagsWrapper;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.TagsWrapperBase;

import org.junit.Test;
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
    //remember that for TagsWrapper, a requirement for the function makeTagsWrappers is that the "chunk collection" ends with a break.
    @Test
    public void testEventsBecomesTwoChunks() {
        ScoreTime beforeSplit = new ScoreTime(newYear, 3);
        LocalDateTime splitter = newYear.plusHours(1);
        ScoreTime afterSplit = new ScoreTime(newYear.plusHours(2), 3);
        LocalDateTime chunkEnd = newYear.plusHours(3);

        List<TagsWrapperBase>chunks = TagsWrapper.makeTagsWrappers(new ArrayList<Tag>(), asList(beforeSplit, afterSplit), asList(splitter, chunkEnd));

        assertEquals(2, chunks.size());
        assertEquals(newYear.plusHours(1), chunks.get(0).getChunkEnd());
        assertEquals(newYear.plusHours(3), chunks.get(1).getChunkEnd());
    }
    @Test
    public void testEventsBecomeOneChunk() {
        ScoreTime beforeSplit1 = new ScoreTime(newYear, 3);
        ScoreTime beforeSplit2 = new ScoreTime(newYear.plusHours(1), 3);
        LocalDateTime splitter = newYear.plusHours(2);
        List<TagsWrapperBase>chunks = TagsWrapper.makeTagsWrappers(new ArrayList<Tag>(), asList(beforeSplit1, beforeSplit2), asList(splitter));


        assertEquals(1, chunks.size());
        assertEquals(newYear.plusHours(2), chunks.get(0).getChunkEnd());
        assertEquals(0, chunks.get(0).getTags().size());
        assertEquals(2, chunks.get(0).getScoreTimes().size());

        assertEquals(newYear.plusHours(1).toString(), chunks.get(0).getScoreTimes().get(1).toString());
    }

    @Test
    public void canMakeThreeChunks() {
        Tag tStart = new Tag(newYear,"", 3.);
        LocalDateTime bAfterStart = newYear.plusHours(1);
        ScoreTime rMiddle = new ScoreTime(newYear.plusHours(2), 3);
        LocalDateTime bBeforeEnd = newYear.plusHours(3);
        Tag tLast = new Tag(newYear.plusHours(4),"", 3.);
        LocalDateTime chunkEnd = newYear.plusHours(5);

        List<TagsWrapperBase>chunks = TagsWrapper.makeTagsWrappers(Arrays.asList(tStart, tLast), asList(rMiddle), asList(bAfterStart, bBeforeEnd, chunkEnd));


        assertEquals(3, chunks.size());
        assertEquals(0, chunks.get(0).getScoreTimes().size());
        assertEquals(1, chunks.get(0).getTags().size());
        assertEquals(newYear.toString(), chunks.get(0).getTags().get(0).getTime().toString());
        assertEquals(newYear.plusHours(1), chunks.get(0).getChunkEnd());
        assertEquals(newYear.plusHours(3), chunks.get(1).getChunkEnd());
        assertEquals(newYear.plusHours(5), chunks.get(2).getChunkEnd());
    }

    //this crashed before.
    @Test
    public void testLastChunkHasNoMoreScoreTimesThanInShould() {
        ScoreTime rStart1 = new ScoreTime(newYear, 3);
        LocalDateTime bAfterStart1 = newYear;
        ScoreTime rMiddle = new ScoreTime(newYear.plusHours(2), 3);
        LocalDateTime chunkEnd = newYear.plusHours(3);

        List<TagsWrapperBase>chunks = TagsWrapper.makeTagsWrappers(new ArrayList<Tag>(), asList(rStart1, rMiddle), asList(bAfterStart1, chunkEnd));

        assertEquals(2, chunks.size());
        assertEquals(1, chunks.get(0).getScoreTimes().size());
        assertEquals(newYear.toString(), chunks.get(0).getScoreTimes().get(0).getTime().toString());
        assertEquals(newYear, chunks.get(0).getChunkEnd());
        assertEquals(1, chunks.get(1).getScoreTimes().size());
    }
    //this case can actually happen if 2 events have same time, and they both have breaks set on them.
    //except for dublette same as above test
    @Test
    public void dubletteBreakShouldActLikeOne() {
        ScoreTime rStart1 = new ScoreTime(newYear, 3);
        LocalDateTime bAfterStart1 = newYear;
        LocalDateTime bAfterStart2 = newYear;
        ScoreTime rMiddle = new ScoreTime(newYear.plusHours(2), 3);
        LocalDateTime chunkEnd = newYear.plusHours(3);

        List<TagsWrapperBase>chunks = TagsWrapper.makeTagsWrappers(new ArrayList<Tag>(), asList(rStart1, rMiddle), asList(bAfterStart1, bAfterStart2, chunkEnd));

        assertEquals(2, chunks.size());
        assertEquals(1, chunks.get(0).getScoreTimes().size());
        assertEquals(newYear.toString(), chunks.get(0).getScoreTimes().get(0).getTime().toString());
        assertEquals(newYear, chunks.get(0).getChunkEnd());
        assertEquals(1, chunks.get(1).getScoreTimes().size());
    }
}
