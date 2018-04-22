package com.johanlund.stat_classes;

import com.johanlund.base_classes.Bm;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.CompleteTime;

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
        CompleteTime bm = new CompleteTime(newYear, 4);
        List <CompleteTime> bms = new ArrayList<>();
        bms.add(bm);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(bms, 4, 4);

        assertEquals(1, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear, tps.get(0).getStopTime());
        assertEquals(0, tps.get(0).getDurationInHours());
    }
    @Test
    public void middleBmShouldBeAdded(){
        CompleteTime bmFirst = new CompleteTime(newYear, 4);
        CompleteTime bmMiddle = new CompleteTime(newYear.plusHours(2), 2);
        CompleteTime bmLast = new CompleteTime(newYear.plusHours(5), 1);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle, bmLast), 2, 3);

        assertEquals(1, tps.size());
        assertEquals(newYear.plusHours(2), tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(2), tps.get(0).getStopTime());
    }
    @Test
    public void twoInMiddleShouldBeAdded(){
        CompleteTime bmFirst = new CompleteTime(newYear, 4);
        CompleteTime bmMiddle = new CompleteTime(newYear.plusHours(2), 2);
        CompleteTime bmMiddle2 = new CompleteTime(newYear.plusHours(4), 3);
        CompleteTime bmLast = new CompleteTime(newYear.plusHours(5), 1);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle,bmMiddle2, bmLast), 2, 3);

        assertEquals(1, tps.size());
        assertEquals(newYear.plusHours(2), tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(4), tps.get(0).getStopTime());
        assertEquals(2, tps.get(0).getDurationInHours());
    }
    @Test
    public void firstAndtwoLastsShouldBeAdded(){
        CompleteTime bmFirst = new CompleteTime(newYear, 5);
        CompleteTime bmMiddle = new CompleteTime(newYear.plusHours(2), 2);
        CompleteTime bmMiddle2 = new CompleteTime(newYear.plusHours(4), 5);
        CompleteTime bmLast = new CompleteTime(newYear.plusHours(5), 5);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle,bmMiddle2, bmLast), 5, 5);

        assertEquals(2, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear, tps.get(0).getStopTime());
        assertEquals(newYear.plusHours(4), tps.get(1).getStartTime());
        assertEquals(newYear.plusHours(5), tps.get(1).getStopTime());
    }
}
