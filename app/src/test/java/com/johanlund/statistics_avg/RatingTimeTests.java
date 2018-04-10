package com.johanlund.statistics_avg;

import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Other;
import com.johanlund.base_classes.Rating;
import com.johanlund.base_classes.Tag;
import com.johanlund.util.RatingTime;
import com.johanlund.util.TimePeriod;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class RatingTimeTests {

    /*
     * D.
     * ratings      |o-------||         OK! (tps stop hour extends after endOfChunk.
     * tp:              |------|          A trick is used to make this work => we use fraction
     *                                    (==weight) to lower the quantity proportionally to the part
     *                                    that is missing. It is not a perfect solution, but a
     *                                    compromise.
     *                                    The problem with this solution is that an effect of might
     *                                    happen at a certain time later. However it seems
     *                                    a shame to remove data completely if only a small fraction
     *                                    is missing of chunk). Perhaps have a settings option to
     *                                    turn this function on/ off?
     *
     * This method also tests that if a Rating and another event occurs at same time,
     * then that Rating should still give score to the other event.
     */
    @Test
    public void tpExtendsChunkTest() {
        LocalDateTime firstTime = LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0);
        //create a Rating at the beginning
        Rating rStart = new Rating(firstTime, 4);

        //at same time, create tag
        Tag t1 = new Tag(firstTime, "Butter", 1.0);
        List<Tag> tags = new ArrayList<>();
        tags.add(t1);
        //...and add them to an event
        Other oStart = new Other(firstTime, tags);

        //add a third event 10 hours later, just to make endOfChunk
        Other oEnd = new Other(firstTime.plusHours(10), tags);

        /*notice that I add the Other event before the rStart. We test here that this should not
         * matter, they have the same time and the rating should give it's score to the other
         * object.
         */
        List<Event> events = new ArrayList<>();
        events.add(oStart);
        events.add(rStart);
        events.add(oEnd);


        /**
         * proportional scale
         * ratings  |o---------||      length: 10 h
         * tp             |----------| length: 10 h (5 hours before chunkend)
         */
        LocalDateTime startOfTp = firstTime.plusHours(5);
        LocalDateTime endOfTp = firstTime.plusHours(15);
        TimePeriod lineForScore = new TimePeriod(startOfTp, endOfTp);

        //create chunk
        Chunk c = Chunk.makeChunksFromEvents(events, new ArrayList<Break>()).get(0);

        //this is the method we are testing
        double[] calcWeight = RatingTime.calcAvgAndWeight(lineForScore, c.getRatings(), c.getLastTime());
        double avgScore = calcWeight[0];
        assertEquals(4.0, avgScore);
        //1/2 of the time tp is outside of chunk => 0.5 in weight
        double weight = calcWeight[1];

        assertEquals(0.5, weight);
    }

    /* This test is very well written.
     *
     * ratings     |-o----||            OK!
     * tp:         |---------|
     *
     * Above is made up of the 2 scenarios below:
     *
     * ratings     |-o---------||       OK! (at beginning of tp, chunk has no score. The tag is in
     * tp:         |-----------|           in start of chunk)
     *
     * &&
     *
     * ratings     |o------||           OK! endChunk < tp.end
     * tp:         |---------|
     */
    @Test
    public void firstRatingAfterTpStartANDChunkEndBeforeTpEndTest() {
        LocalDateTime firstTime = LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0);

        //start with creating tp (proportional)
        // |-----|
        TimePeriod tp = new TimePeriod(firstTime, firstTime.plusHours(6));

        /**match tp with ratings so we have (proportional scale)
         * ratings     |-o--||
         * tp:         |-----|
         *
         */
        Rating rStart = new Rating(firstTime.plusHours(2),4);
        LocalDateTime chunkEnd = firstTime.plusHours(5); //=> 2 hours before tp.end

        //this is the method we are testing
        double[] avgscoreAndWeight = RatingTime.calcAvgAndWeight(tp, Arrays.asList(rStart), chunkEnd);
        assertEquals(4.0, avgscoreAndWeight[0]);

        /*  weight should equal durationOfRatingsInScopeOfTp/tp.duration
            ratingsInScopeOfTp = 3 hours
            tp.duration = 6 hours.
            weight should be 3/6 = 1/2 = 0.5...
         */

        assertEquals(0.5, avgscoreAndWeight[1]);

    }
}
