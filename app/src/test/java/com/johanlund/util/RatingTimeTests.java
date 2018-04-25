package com.johanlund.util;

import com.johanlund.base_classes.Rating;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.Arrays;
import java.util.List;

import static com.johanlund.util.RatingTime.getRatingsBetweenAndSometimesOneBefore;
import static junit.framework.Assert.assertEquals;

/**
 * Tests for RatingTime class
 */

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
        ScoreTime rStart = new ScoreTime(firstTime, 6);
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
        ScoreTime rStart = new ScoreTime(firstTime.plusHours(2), 3);
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
        ScoreTime rStart = new ScoreTime(firstTime, 4);
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
        ScoreTime rStart = new ScoreTime(firstTime.plusHours(2),4);
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

    //testing if getRatingsBetweenAndSometimesOneBefore is returning the a rating if it occurs at same time as start of tp. This is important because if it does it means that a rating and a tag/portion can be at same time with no worries that the tag/ portion will not get the score from that rating.
    @Test
    public void ratingAtSameTimeAsStartIsReturned() {
        LocalDateTime firstTime = LocalDateTime.of(2017, Month.JANUARY, 1, 0,0);

        TimePeriod tp = new TimePeriod(firstTime, firstTime.plusHours(4));
        ScoreTime r1 = new ScoreTime (firstTime, 3);
        ScoreTime r2 = new ScoreTime (firstTime.plusHours(3),3);

        List<ScoreTime> returnedList = getRatingsBetweenAndSometimesOneBefore(tp, Arrays.asList(r1,r2));
        assertEquals(2, returnedList.size());
        assertEquals(r1.getTime(), returnedList.get(0).getTime());
        assertEquals(r2.getTime(), returnedList.get(1).getTime());
    }
}
