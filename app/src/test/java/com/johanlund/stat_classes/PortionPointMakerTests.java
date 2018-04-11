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

import static junit.framework.Assert.assertEquals;

public class PortionPointMakerTests {
    @Test
    public void testCorrectPortionPointIsRetrieved(){
        LocalDateTime newYear = LocalDateTime.of(2018, Month.JANUARY, 1,0,0);
        PortionStatRange range =  new PortionStatRange(0.0f, 1.1f, true);
        PortionTime pt = new PortionTime(0.9 ,newYear);
        Rating rStart = new Rating(newYear, 3);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        PtRatings ptRatings = new PtRatings(Arrays.asList(pt), Arrays.asList(rStart), newYear.plusHours(10));
        PortionPoint pp = PortionPointMaker.getPPForRange(range, Arrays.asList(ptRatings), startHoursAfterMeal, stopHoursAfterMeal);

        assertEquals(3.0, pp.getScore());
        assertEquals(1.0, pp.getQuant());
    }
}
