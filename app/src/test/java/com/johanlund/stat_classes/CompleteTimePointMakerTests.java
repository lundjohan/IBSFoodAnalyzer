package com.johanlund.stat_classes;

import com.johanlund.base_classes.Bm;
import com.johanlund.statistics_point_classes.TimePoint;

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
        Bm bm = new Bm(newYear, 4, 4);
        List <Bm> bms = new ArrayList<>();
        bms.add(bm);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(bms, 4, 4);

        assertEquals(1, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear, tps.get(0).getStopTime());
        assertEquals(0, tps.get(0).getDurationInHours());
    }
    @Test
    public void middleBmShouldBeAdded(){
        Bm bmFirst = new Bm(newYear, 4, 4);
        Bm bmMiddle = new Bm(newYear.plusHours(2), 2, 4);
        Bm bmLast = new Bm(newYear.plusHours(5), 1, 4);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle, bmLast), 2, 3);

        assertEquals(1, tps.size());
        assertEquals(newYear.plusHours(2), tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(2), tps.get(0).getStopTime());
    }
    @Test
    public void twoInMiddleShouldBeAdded(){
        Bm bmFirst = new Bm(newYear, 4, 4);
        Bm bmMiddle = new Bm(newYear.plusHours(2), 2, 4);
        Bm bmMiddle2 = new Bm(newYear.plusHours(4), 3, 4);
        Bm bmLast = new Bm(newYear.plusHours(5), 1, 4);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle,bmMiddle2, bmLast), 2, 3);

        assertEquals(1, tps.size());
        assertEquals(newYear.plusHours(2), tps.get(0).getStartTime());
        assertEquals(newYear.plusHours(4), tps.get(0).getStopTime());
        assertEquals(2, tps.get(0).getDurationInHours());
    }
    @Test
    public void firstAndtwoLastsShouldBeAdded(){
        Bm bmFirst = new Bm(newYear, 5, 4);
        Bm bmMiddle = new Bm(newYear.plusHours(2), 2, 4);
        Bm bmMiddle2 = new Bm(newYear.plusHours(4), 5, 4);
        Bm bmLast = new Bm(newYear.plusHours(5), 5, 4);
        List<TimePoint> tps = TimePointMaker.doBMTimePoints(Arrays.asList(bmFirst, bmMiddle,bmMiddle2, bmLast), 5, 5);

        assertEquals(2, tps.size());
        assertEquals(newYear, tps.get(0).getStartTime());
        assertEquals(newYear, tps.get(0).getStopTime());
        assertEquals(newYear.plusHours(4), tps.get(1).getStartTime());
        assertEquals(newYear.plusHours(5), tps.get(1).getStopTime());
    }
}
