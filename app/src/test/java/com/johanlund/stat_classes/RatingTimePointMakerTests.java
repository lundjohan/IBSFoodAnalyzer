package com.johanlund.stat_classes;

import com.johanlund.stat_backend.makers.TimePointMaker;
import com.johanlund.stat_backend.point_classes.TimePoint;
import com.johanlund.stat_backend.stat_util.ScoreTime;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class RatingTimePointMakerTests {
    final static LocalDateTime newYear = LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0);

    /*
    ---------
     */
    @Test
    public void shouldReturnEmptyList(){
        //empty ratings list
        List<TimePoint> tps =  TimePointMaker.doRatingTimePoints(new ArrayList<ScoreTime>(), newYear, 1, 6);
        assertEquals(0, tps.size());
    }
    //this should never happen and will not be tested
    //public void testChunkEnd < firstRating(){
    /*
    >>>>>------>>>>>
     */
    @Test
    public void shouldReturnFirstAndThirdPartOnly(){
        ScoreTime r1 = new ScoreTime(newYear, 6);
        ScoreTime r2 = new ScoreTime(newYear.plusHours(3), 3);
        ScoreTime r3 = new ScoreTime(newYear.plusHours(10), 6);

        List<TimePoint> tps =  TimePointMaker.doRatingTimePoints(Arrays.asList(r1, r2, r3), newYear.plusHours(20), 6, 6);
        assertEquals(2, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(3), tps.get(0).getStopTime());
        assertEquals(3, tps.get(0).getDurationInHours()); //check getDuration is working

        assertEquals(newYear.plusHours(10), tps.get(1).getStartTime());
        assertEquals(newYear.plusHours(20), tps.get(1).getStopTime());
    }
    /*
    ------>>>>>>------
     */
    @Test
    public void shouldReturnMiddlePartOnly(){
        ScoreTime r1 = new ScoreTime(newYear, 6);
        ScoreTime r2 = new ScoreTime(newYear.plusHours(3), 3);
        ScoreTime r3 = new ScoreTime(newYear.plusHours(10), 6);

        List<TimePoint> tps =  TimePointMaker.doRatingTimePoints(Arrays.asList(r1, r2, r3), newYear.plusHours(20), 1, 4);
        assertEquals(1, tps.size());
        assertEquals(newYear.plusHours(3), tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(10), tps.get(0).getStopTime());
    }
    /*
    >>>>>>>>>>>>>------
     */
    @Test
    public void shouldReturnFirstAndSecondPartAsOne(){
        ScoreTime r1 = new ScoreTime(newYear, 6);
        ScoreTime r2 = new ScoreTime(newYear.plusHours(3), 5);
        ScoreTime r3 = new ScoreTime(newYear.plusHours(10), 3);

        List<TimePoint> tps =  TimePointMaker.doRatingTimePoints(Arrays.asList(r1, r2, r3), newYear.plusHours(20), 5, 6);

        assertEquals(1, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(10), tps.get(0).getStopTime());
    }
    /*
    ------>>>>>>>>>>>>>
     */
    @Test
    public void shouldReturnSecondAndThirdPartAsOne(){
        ScoreTime r1 = new ScoreTime(newYear, 6);
        ScoreTime r2 = new ScoreTime(newYear.plusHours(3), 5);
        ScoreTime r3 = new ScoreTime(newYear.plusHours(10), 3);

        List<TimePoint> tps =  TimePointMaker.doRatingTimePoints(Arrays.asList(r1, r2, r3), newYear.plusHours(20), 3, 5);

        assertEquals(1, tps.size());
        assertEquals(newYear.plusHours(3), tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(20), tps.get(0).getStopTime());
    }
    /*
    >>>>>>>>>>>>>>>>>>>>
     */
    @Test
    public void shouldReturnEverythingAsOne(){
        ScoreTime r1 = new ScoreTime(newYear, 6);
        ScoreTime r2 = new ScoreTime(newYear.plusHours(3), 5);
        ScoreTime r3 = new ScoreTime(newYear.plusHours(10), 3);

        List<TimePoint> tps =  TimePointMaker.doRatingTimePoints(Arrays.asList(r1, r2, r3), newYear.plusHours(20), 3, 6);

        assertEquals(1, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(20), tps.get(0).getStopTime());
    }
}
