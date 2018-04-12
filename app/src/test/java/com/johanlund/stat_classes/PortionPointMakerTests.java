package com.johanlund.stat_classes;

import com.johanlund.base_classes.Rating;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_portions.PortionTime;
import com.johanlund.statistics_portions.PtRatings;
import com.johanlund.statistics_settings_portions.PortionStatRange;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.Arrays;
import java.util.List;

import static com.johanlund.stat_classes.PortionPointMaker.joinTooClosePortions2;
import static junit.framework.Assert.assertEquals;

public class PortionPointMakerTests {
    final static LocalDateTime newYear = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0);
    final static PortionStatRange range = new PortionStatRange(0.0f, 1.1f, true);

    //==============================================================================================
    // normal use cases tests
    //==============================================================================================
    @Test
    public void testGetPortionPointWithOnePortion() {
        //one only, within range
        PortionTime ptWithinRange = new PortionTime(0.9, newYear);
        Rating rStart = new Rating(newYear, 3);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        PtRatings ptRatings = new PtRatings(Arrays.asList(ptWithinRange), Arrays.asList(rStart),
                newYear.plusHours(10));
        PortionPoint pp = PortionPointMaker.getPPForRange(range, Arrays.asList(ptRatings),
                startHoursAfterMeal, stopHoursAfterMeal);

        assertEquals(3.0, pp.getScore());
        assertEquals(1.0, pp.getQuant());
    }

    @Test
    public void testGetPortionPointWithTwoPortionsAndTwoRatings() {
        //two, both within range
        PortionTime pt1 = new PortionTime(0.8, newYear);
        PortionTime pt2 = new PortionTime(0.4, newYear.plusHours(2));

        Rating rStart = new Rating(newYear, 3);
        Rating rLater = new Rating(newYear.plusHours(2), 5);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        PtRatings ptRatings = new PtRatings(Arrays.asList(pt1, pt2), Arrays.asList(rStart,
                rLater), newYear.plusHours(20));
        PortionPoint pp = PortionPointMaker.getPPForRange(range, Arrays.asList(ptRatings),
                startHoursAfterMeal, stopHoursAfterMeal);


        //pt1 lasts for 2 + 6 hours, pt2 => 8 hours =>
        /*avg score => ((avgScore1 + avgScore2) / totalQuant) => ((2*3. + 6*5.)/8 + 8*5./8) /2. =
            (36/8 + 5) / 2 = (9/2 + 5)/2 = 19/4 = 4.75
         */
        assertEquals(4.75, pp.getScore(), 0.01);
        assertEquals(2.0, pp.getQuant());
    }
    //==============================================================================================
    // shorten timeperiods from left and right
    //==============================================================================================
    @Test
    public void testChunkEndIsEarly() {
        PortionTime pt1 = new PortionTime(0.8, newYear);
        Rating rStart = new Rating(newYear, 3);

        //chunk end < stopHoursAfterMeal
        PtRatings ptRatings = new PtRatings(Arrays.asList(pt1), Arrays.asList(rStart), newYear.plusHours(6));

        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        PortionPoint pp = PortionPointMaker.getPPForRange(range, Arrays.asList(ptRatings),
                startHoursAfterMeal, stopHoursAfterMeal);

        assertEquals(3., pp.getScore(), 0.01);

        //6H 8/H = 3/4 = 0.75
        assertEquals(0.75, pp.getQuant());
    }
    //late start of Rating and early end of chunk end
    @Test
    public void testCutOffTimePeriodFromBothSides() {
        //one only, within range
        PortionTime pt1 = new PortionTime(0.8, newYear);

        Rating rStart = new Rating(newYear.plusHours(2), 3);
        Rating rSecond = new Rating(newYear.plusHours(3), 4);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 6;

        PtRatings ptRatings = new PtRatings(Arrays.asList(pt1), Arrays.asList(rStart, rSecond), newYear.plusHours(5));
        PortionPoint pp = PortionPointMaker.getPPForRange(range, Arrays.asList(ptRatings),
                startHoursAfterMeal, stopHoursAfterMeal);


        //6 hours should be investigated: first 2 hours: no rating (don't count), between 2nd and 3rd hour: 3.0,
        // between 3rd and 5th hour: 4.0. Chunk ends at 5th hour.
        /* avg score for portion => (1h * 3.0 + 2h * 4.0) / 3h
        = 11/ 3 = 3.66666...
           weight for this portion = 3h / 6h = 0.5
         */
        assertEquals(3.6666, pp.getScore(), 0.01);
        assertEquals(0.5, pp.getQuant());

    }

    //==============================================================================================
    // tests close portions
    //==============================================================================================
    /*

     p = portion
        ----p1-p2---- +
        minHoursBetweenMeals > one line (-)  =>
        ----p1p2----- (portions bundled togehter at time for p1)

     */
    @Test
    public void twoClosePortionsJoinToAtPlaceOfFirst(){
        //minMealDist > p2 - p1
        int minMealDist = 4;
        PortionTime p1 = new PortionTime(1.0, newYear);
        PortionTime p2 = new PortionTime(2.0, newYear.plusHours(2));
        List<PortionTime> pts = joinTooClosePortions2(Arrays.asList(p1, p2),minMealDist);
        assertEquals(1, pts.size());
        assertEquals(3.,pts.get(0).getPSize());
        assertEquals(newYear,pts.get(0).getTime());
    }
    @Test
    public void dontJoin(){
        //minMealDist < p2 - p1
        int minMealDist = 1;
        PortionTime p1 = new PortionTime(1.0, newYear);
        PortionTime p2 = new PortionTime(2.0, newYear.plusHours(2));
        List<PortionTime> pts = joinTooClosePortions2(Arrays.asList(p1, p2),minMealDist);
        assertEquals(2, pts.size());

        assertEquals(1.,pts.get(0).getPSize());
        assertEquals(2.,pts.get(1).getPSize());

        assertEquals(newYear,pts.get(0).getTime());
        assertEquals(newYear.plusHours(2),pts.get(1).getTime());
    }

    /*
     * minDist == --
     * A1 (before join)
     * ---p1-p2-p3--
     *
     * A2 (after join)
     * ---p1p2--p3--
     *
     * p3 will not have joined with p1p2.
     */
    @Test
    public void dontJoinThirdP(){
        // p2 - p1 > minMealDist > p3 - p1p2
        int minMealDist = 2;
        PortionTime p1 = new PortionTime(1.0, newYear);
        PortionTime p2 = new PortionTime(2.0, newYear.plusHours(1));
        PortionTime p3 = new PortionTime(6.0, newYear.plusHours(2));
        List<PortionTime> pts = joinTooClosePortions2(Arrays.asList(p1, p2, p3),minMealDist);
        assertEquals(2, pts.size());

        assertEquals(3.,pts.get(0).getPSize());
        assertEquals(6.,pts.get(1).getPSize());

        assertEquals(newYear,pts.get(0).getTime());
        assertEquals(newYear.plusHours(2),pts.get(1).getTime());
    }
    //=> p1p2p3
    @Test
    public void doJoinThirdP(){
        // p1 + minMealDist < p3
        int minMealDist = 3;
        PortionTime p1 = new PortionTime(1.0, newYear);
        PortionTime p2 = new PortionTime(2.0, newYear.plusHours(1));
        PortionTime p3 = new PortionTime(6.0, newYear.plusHours(2));
        List<PortionTime> pts = joinTooClosePortions2(Arrays.asList(p1, p2, p3),minMealDist);

        assertEquals(1, pts.size());
        assertEquals(9.,pts.get(0).getPSize());
        assertEquals(newYear,pts.get(0).getTime());
    }
}
