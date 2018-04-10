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
    /* Normal case
     *
     * ratings     |o-------||
     * tp:          |-------|
     *
     */
    @Test
    public void scopeOfRatingsIsCoveringTp() {
        LocalDateTime firstTime = LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0);

        //start with creating tp (proportional), a little bit ahead
        // |------|
        TimePeriod tp = new TimePeriod(firstTime.plusHours(1), firstTime.plusHours(8));



        /**match tp with ratings and chunkEnd so we have (proportional scale)
         * ratings     |o---------||
         * tp:          |-------|
         *
         */
        Rating rStart = new Rating(firstTime, 6);
        LocalDateTime chunkEnd = firstTime.plusHours(10); //=> several hours after tp.end

        //this is the method we are testing
        double[] avgscoreAndWeight = RatingTime.calcAvgAndWeight(tp, Arrays.asList(rStart), chunkEnd);
        assertEquals(6.0, avgscoreAndWeight[0]);

        /*  weight should equal durationOfRatingsInScopeOfTp/tp.duration
            ratingsInScopeOfTp = 7 hours
            tp.duration = 7 hours.
            weight should be 7/7 = 1.0
         */

        assertEquals(1.0, avgscoreAndWeight[1], 0.01);
    }
    /*
     *
     * ratings     |--o----||
     * tp:         |-------|
     *
     */
    @Test
    public void firstRatingsIsLate() {
        LocalDateTime firstTime = LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0);

        //start with creating tp (proportional)
        // |------|
        TimePeriod tp = new TimePeriod(firstTime.plusHours(0), firstTime.plusHours(7));



        /**match tp with ratings and chunkEnd so we have (proportional scale)
         * ratings     |--o----||
         * tp:         |-------|
         *
         */
        Rating rStart = new Rating(firstTime.plusHours(2), 3);
        LocalDateTime chunkEnd = firstTime.plusHours(7); //=> same as tp.end

        //this is the method we are testing
        double[] avgscoreAndWeight = RatingTime.calcAvgAndWeight(tp, Arrays.asList(rStart), chunkEnd);
        assertEquals(3.0, avgscoreAndWeight[0]);

        /*  weight should equal durationOfRatingsInScopeOfTp/tp.duration
            timeOfRatingsInScopeOfTp = 5 hours
            tp.duration = 7 hours.
            weight should be 5/7
         */

        assertEquals(5./7., avgscoreAndWeight[1], 0.01);
    }
    /*
     *
     * ratings      |o----||
     * tp:          |------|
     *
     */
    @Test
    public void tpExtendsChunkEndTest() {
        LocalDateTime firstTime = LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0);

        //start with creating tp (proportional)
        // |------|
        TimePeriod tp = new TimePeriod(firstTime, firstTime.plusHours(6));



        /**match tp with ratings and chunkEnd so we have (proportional scale)
         * ratings     |o----||
         * tp:         |------|
         *
         */
        Rating rStart = new Rating(firstTime, 4);
        LocalDateTime chunkEnd = firstTime.plusHours(5); //=> 1 hour before tp.end

        //this is the method we are testing
        double[] avgscoreAndWeight = RatingTime.calcAvgAndWeight(tp, Arrays.asList(rStart), chunkEnd);
        assertEquals(4.0, avgscoreAndWeight[0]);

        /*  weight should equal durationOfRatingsInScopeOfTp/tp.duration
            ratingsInScopeOfTp = 5 hours
            tp.duration = 6 hours.
            weight should be 5/6
         */

        assertEquals(5./6., avgscoreAndWeight[1], 0.01);
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
        LocalDateTime chunkEnd = firstTime.plusHours(5); //=> 1 hour before tp.end

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
