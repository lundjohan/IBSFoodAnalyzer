package com.johanlund.stat_classes;

import com.johanlund.base_classes.Tag;
import com.johanlund.screens.statistics.avg_stat.common.TagsWrapper;
import com.johanlund.stat_backend.point_classes.PortionPoint;
import com.johanlund.screens.statistics.portions_settings.PortionStatRange;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.TagsWrapperBase;
import com.johanlund.util.TimePeriod;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.johanlund.stat_backend.makers.PortionPointMaker.getPPForRange;
import static com.johanlund.stat_backend.makers.PortionPointMaker.joinTooClosePortions2;
import static com.johanlund.stat_backend.makers.PortionPointMaker.leftExceptRights;
import static com.johanlund.stat_backend.makers.PortionPointMaker.makeExceptTps;
import static com.johanlund.stat_backend.makers.PortionPointMaker.toReplaceCalcPoints;
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
        Tag ptWithinRange = new Tag(newYear,"",0.9);
        ScoreTime rStart = new ScoreTime(newYear, 3);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        TagsWrapperBase ptRatings = new TagsWrapper(Arrays.asList(ptWithinRange), Arrays.asList(rStart),
                newYear.plusHours(10));
        PortionPoint pp = getPPForRange(range, Arrays.asList(ptRatings),
                startHoursAfterMeal, stopHoursAfterMeal);

        assertEquals(3.0, pp.getScore());
        assertEquals(1.0, pp.getQuant());
    }

    @Test
    public void testGetPortionPointWithTwoPortionsAndTwoRatings() {
        //two, both within range
        Tag pt1 = new Tag(newYear,"",0.8);
        Tag pt2 = new Tag(newYear.plusHours(2),"",0.4);

        ScoreTime rStart = new ScoreTime(newYear, 3);
        ScoreTime rLater = new ScoreTime(newYear.plusHours(2), 5);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        TagsWrapperBase ptRatings = new TagsWrapper(Arrays.asList(pt1, pt2), Arrays.asList(rStart,
                rLater), newYear.plusHours(20));
        PortionPoint pp = getPPForRange(range, Arrays.asList(ptRatings),
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
        Tag pt1 = new Tag(newYear,"",0.8);
        ScoreTime rStart = new ScoreTime(newYear, 3);

        //chunk end < stopHoursAfterMeal
        TagsWrapperBase ptRatings = new TagsWrapper(Arrays.asList(pt1), Arrays.asList(rStart), newYear.plusHours(6));

        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        PortionPoint pp = getPPForRange(range, Arrays.asList(ptRatings),
                startHoursAfterMeal, stopHoursAfterMeal);

        assertEquals(3., pp.getScore(), 0.01);

        //6H 8/H = 3/4 = 0.75
        assertEquals(0.75, pp.getQuant());
    }
    //late start of Rating and early end of chunk end
    @Test
    public void testCutOffTimePeriodFromBothSides() {
        //one only, within range
        Tag pt1 = new Tag(newYear,"",0.8);

        ScoreTime rStart = new ScoreTime(newYear.plusHours(2), 3);
        ScoreTime rSecond = new ScoreTime(newYear.plusHours(3), 4);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 6;

        TagsWrapperBase ptRatings = new TagsWrapper(Arrays.asList(pt1), Arrays.asList(rStart, rSecond), newYear.plusHours(5));
        PortionPoint pp = getPPForRange(range, Arrays.asList(ptRatings),
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
    // tests join p1->p2 (join goes in right direction)
    //==============================================================================================
    /*

     p = portion
        ----p1-p2---- +
        minHoursBetweenMeals > one line (-)  =>
        -------p2---- (portions bundled togehter at time for p1)
               p1
     */
    @Test
    public void p1_And_p2_JoinToAtPlaceOf_p2(){
        //minMealDist > p2 - p1
        int minMealDist = 4;
        Tag p1 = new Tag(newYear,"",1.0);
        Tag p2 = new Tag(newYear.plusHours(2),"",2.0);
        List<Tag> pts = joinTooClosePortions2(Arrays.asList(p1, p2),minMealDist);
        assertEquals(1, pts.size());
        assertEquals(3.,pts.get(0).getSize());
        assertEquals(newYear.plusHours(2),pts.get(0).getTime());
    }
    @Test
    public void dontJoin(){
        //minMealDist < p2 - p1
        int minMealDist = 1;
        Tag p1 = new Tag(newYear,"",1.0);
        Tag p2 = new Tag(newYear.plusHours(2),"",2.0);
        List<Tag> pts = joinTooClosePortions2(Arrays.asList(p1, p2),minMealDist);
        assertEquals(2, pts.size());

        assertEquals(1.,pts.get(0).getSize());
        assertEquals(2.,pts.get(1).getSize());

        assertEquals(newYear,pts.get(0).getTime());
        assertEquals(newYear.plusHours(2),pts.get(1).getTime());
    }

    /*//adjusted for join right
     * minDist == --
     * A1 (before join)
     * ---p1-p2-p3--
     *
     * A2 (after join)
     * ------p2-p3--
     *       p1
     *
     */
    @Test
    public void join_p1p2_butNot_p3(){
        int minMealDist = 2;
        Tag p1 = new Tag(newYear,"",1.0);
        Tag p2 = new Tag(newYear.plusHours(1),"",2.0);
        Tag p3 = new Tag(newYear.plusHours(2),"",6.0);
        List<Tag> pts = joinTooClosePortions2(Arrays.asList(p1, p2, p3),minMealDist);
        assertEquals(2, pts.size());

        assertEquals(3.,pts.get(0).getSize());
        assertEquals(6.,pts.get(1).getSize());

        assertEquals(newYear.plusHours(1),pts.get(0).getTime());
        assertEquals(newYear.plusHours(2),pts.get(1).getTime());
    }
    //adjusted for join right
    //=> p1p2p3
    @Test
    public void doJoinThirdP(){
        // p1 + minMealDist < p3
        int minMealDist = 3;
        Tag p1 = new Tag(newYear,"",1.0);
        Tag p2 = new Tag(newYear.plusHours(1),"",2.0);
        Tag p3 = new Tag(newYear.plusHours(2),"",6.0);
        List<Tag> pts = joinTooClosePortions2(Arrays.asList(p1, p2, p3),minMealDist);

        assertEquals(1, pts.size());
        assertEquals(9.,pts.get(0).getSize());
        assertEquals(newYear.plusHours(2),pts.get(0).getTime());
    }
    //=> p1p2p3
    @Test
    public void testHigherUpWithJoinThirdP() {
        //from test closest above
        // p1 + minMealDist < p3
        int minMealDist = 20;
        //total portion size will be 3*0,3 = 0.9 == within range
        Tag p1 = new Tag(newYear,"",.3);
        Tag p2 = new Tag(newYear.plusHours(1),"",.3);
        Tag p3 = new Tag(newYear.plusHours(2),"",.3);

        ScoreTime rStart = new ScoreTime(newYear.minusHours(1), 3);
        ScoreTime r2 = new ScoreTime(newYear.plusHours(6), 5);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        TagsWrapperBase ptRatings = new TagsWrapper(Arrays.asList(p1, p2, p3), Arrays.asList(rStart, r2), newYear.plusHours(20));
        List<PortionPoint> pps = toReplaceCalcPoints(Arrays.asList(ptRatings), Arrays.asList(range),startHoursAfterMeal, stopHoursAfterMeal, 20 );


         //2 meals has moved to place of third
        assertEquals(1,pps.size());
         assertEquals(4., pps.get(0).getScore(), 0.01);
        assertEquals(1.0, pps.get(0).getQuant());
    }
    @Test
    public void joinTooClosePortionsDoesntCrashWithSmallListAsParameter(){
        //this can happen in app
        List<Tag>emptyList = new ArrayList<>();
        List<Tag> returned = joinTooClosePortions2(emptyList, 3);
        assertEquals(0, returned.size());
    }
    //==============================================================================================
    // simple extract time periods
    //==============================================================================================
    @Test
    public void testSimpleCase_simpleExtractTimePeriods(){
        Tag p1 =  new Tag(newYear,"",0.9);
        List<TimePeriod> tps = makeExceptTps(range, Arrays.asList(p1),0,8);
        assertEquals(1, tps.size());
        assertEquals(newYear,tps.get(0).getStart());
        assertEquals(newYear.plusHours(8),tps.get(0).getEnd());
    }
    //==============================================================================================
    // test in-range portions EXCEPT too-big portions
    // Given for app logic and for tests:
    // 1. right ALWAYS >= left (don't test for anything else)
    // 2. rights are occurring in asc order
    //==============================================================================================
    @Test
    public void cutFromWest(){
        //same length (here: 10) all tps in the beginning
        TimePeriod left = new TimePeriod(newYear, newYear.plusHours(10));
        TimePeriod r1 = new TimePeriod(newYear.minusHours(4), newYear.minusHours(4).plusHours(10));

        //pardon the pun, variable name should be leftRemains
        TimePeriod leftover = leftExceptRights(left, Arrays.asList(r1));
        assertEquals(newYear.plusHours(6),leftover.getStart());
        assertEquals(newYear.plusHours(10),leftover.getEnd());
    }
    @Test
    public void cutFromWestAndRight(){
        TimePeriod left = new TimePeriod(newYear, newYear.plusHours(10));
        //end == 6 hours into the new year
        TimePeriod r1 = new TimePeriod(newYear.minusHours(4), newYear.minusHours(4).plusHours(10));
        //start == 8 hours into the new year
        TimePeriod r2 = new TimePeriod(newYear.plusHours(8), newYear.plusHours(8).plusHours(10));

        TimePeriod leftRemains = leftExceptRights(left, Arrays.asList(r1, r2));
        assertEquals(newYear.plusHours(6),leftRemains.getStart());
        assertEquals(newYear.plusHours(8),leftRemains.getEnd());
    }
    @Test
    public void rightCoversAndErasesAllOfLeft(){
        TimePeriod left = new TimePeriod(newYear, newYear.plusHours(4));
        TimePeriod r1 = new TimePeriod(newYear, newYear.plusHours(6));
        TimePeriod leftRemains = leftExceptRights(left, Arrays.asList(r1));
        assertEquals(0,leftRemains.getLengthSec());
    }
    //this can happen (rights are occurring in asc order)!
    @Test
    public void cutFromWestTwice(){
        TimePeriod left = new TimePeriod(newYear, newYear.plusHours(10));
        TimePeriod rEast1 = new TimePeriod(newYear.minusHours(7), newYear.plusHours(3));
        TimePeriod rEast2 = new TimePeriod(newYear.minusHours(2), newYear.plusHours(8));

        //pardon the pun, variable name should be leftRemains
        TimePeriod leftRemains = leftExceptRights(left, Arrays.asList(rEast1, rEast2));
        assertEquals(newYear.plusHours(8),leftRemains.getStart());
        assertEquals(newYear.plusHours(10),leftRemains.getEnd());
    }
    //cutFromEastTwice never happens (rights are occurring in asc order)!
    //@Test
    // /public void cutFromEastTwice() {}


    //chunk end is far away.
    //the only thing that happens is that p1's tp is shortened by a portion larger than allowed range
    @Test
    public void EXCEPTInHighHierarchyTest(){
        //within range
        Tag p1 = new Tag(newYear,"",0.9);
        //too large
        Tag p2 = new Tag(newYear.plusHours(4),"",1.5);
        ScoreTime r1 = new ScoreTime(newYear, 3);
        ScoreTime r2 = new ScoreTime(newYear.plusHours(4), 3);
        TagsWrapperBase ptr = new TagsWrapper(Arrays.asList(p1, p2), Arrays.asList(r1, r2), newYear.plusHours(20));


        PortionPoint pp = getPPForRange(range, Arrays.asList(ptr), 0, 10);

        assertEquals(3.,pp.getScore(), 0.001);
        //quant == weight ==  4H / 10H = 2/5 = 0.4
        assertEquals(0.4,pp.getQuant(), 0.0001);
    }
    //lets complicate things a little bit
    @Test
    public void EXCEPT_And_LateRating_InHighHierarchyTest(){
        //within range
        Tag p1 = new Tag(newYear,"",0.9);
        //too large
        Tag p2 = new Tag(newYear.plusHours(4),"",1.5);
        ScoreTime rLate = new ScoreTime(newYear.plusHours(1), 3);
        TagsWrapperBase ptr = new TagsWrapper(Arrays.asList(p1,p2), Arrays.asList(rLate), newYear.plusHours(20));

        PortionPoint pp = getPPForRange(range, Arrays.asList(ptr), 0, 10);

        //quant == weight ==  3H / 10H = 0.333..
        assertEquals(0.3,pp.getQuant(), 0.01);
    }
}
