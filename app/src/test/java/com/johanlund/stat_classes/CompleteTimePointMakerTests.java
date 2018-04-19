package com.johanlund.stat_classes;

import com.johanlund.base_classes.Bm;
import com.johanlund.statistics_point_classes.TimePoint;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
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
}
