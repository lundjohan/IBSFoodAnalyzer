package com.johanlund.stat_classes;

import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.ScoreTime;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CompleteTimePointMakerTests {
    LocalDateTime newYear = LocalDateTime.of(2018, Month.JANUARY, 1 , 0, 0);
    @Test
    public void testOnlyOneBm(){
        ScoreTime bm = new ScoreTime(newYear, 4);
        List <ScoreTime> bms = new ArrayList<>();
        bms.add(bm);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(bms, 4, 4);

        assertEquals(1, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear, tps.get(0).getStopTime());
        assertEquals(0, tps.get(0).getDurationInHours());
    }
    @Test
    public void middleBmShouldBeAdded(){
        ScoreTime bmFirst = new ScoreTime(newYear, 4);
        ScoreTime bmMiddle = new ScoreTime(newYear.plusHours(2), 2);
        ScoreTime bmLast = new ScoreTime(newYear.plusHours(5), 1);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle, bmLast), 2, 3);

        assertEquals(1, tps.size());
        assertEquals(newYear.plusHours(2), tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(2), tps.get(0).getStopTime());
    }
    @Test
    public void twoInMiddleShouldBeAdded(){
        ScoreTime bmFirst = new ScoreTime(newYear, 4);
        ScoreTime bmMiddle = new ScoreTime(newYear.plusHours(2), 2);
        ScoreTime bmMiddle2 = new ScoreTime(newYear.plusHours(4), 3);
        ScoreTime bmLast = new ScoreTime(newYear.plusHours(5), 1);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle,bmMiddle2, bmLast), 2, 3);

        assertEquals(1, tps.size());
        assertEquals(newYear.plusHours(2), tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(4), tps.get(0).getStopTime());
        assertEquals(2, tps.get(0).getDurationInHours());
    }
    @Test
    public void firstAndtwoLastsShouldBeAdded(){
        ScoreTime bmFirst = new ScoreTime(newYear, 5);
        ScoreTime bmMiddle = new ScoreTime(newYear.plusHours(2), 2);
        ScoreTime bmMiddle2 = new ScoreTime(newYear.plusHours(4), 5);
        ScoreTime bmLast = new ScoreTime(newYear.plusHours(5), 5);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle,bmMiddle2, bmLast), 5, 5);

        assertEquals(2, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear, tps.get(0).getStopTime());
        assertEquals(newYear.plusHours(4), tps.get(1).getStartTime());
        assertEquals(newYear.plusHours(5), tps.get(1).getStopTime());
    }
}
