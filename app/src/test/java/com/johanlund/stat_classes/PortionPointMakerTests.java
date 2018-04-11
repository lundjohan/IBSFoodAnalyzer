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
    public void testGetPortionPointWithOnePortion(){
        LocalDateTime newYear = LocalDateTime.of(2018, Month.JANUARY, 1,0,0);
        PortionStatRange range =  new PortionStatRange(0.0f, 1.1f, true);

        //one only
        PortionTime ptWithinRange = new PortionTime(0.9 ,newYear);
        Rating rStart = new Rating(newYear, 3);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        PtRatings ptRatings = new PtRatings(Arrays.asList(ptWithinRange), Arrays.asList(rStart), newYear.plusHours(10));
        PortionPoint pp = PortionPointMaker.getPPForRange(range, Arrays.asList(ptRatings), startHoursAfterMeal, stopHoursAfterMeal);

        assertEquals(3.0, pp.getScore());
        assertEquals(1.0, pp.getQuant());
    }
    @Test
    public void testGetPortionPointWithTwoPortionsAndTwoRatings(){
        PortionStatRange range =  new PortionStatRange(0.0f, 1.1f, true);
        LocalDateTime newYear = LocalDateTime.of(2018, Month.JANUARY, 1,0,0);

        //two, both within range
        PortionTime pt1 = new PortionTime(0.8 ,newYear);
        PortionTime pt2 = new PortionTime(0.4 ,newYear.plusHours(2));

        Rating rStart = new Rating(newYear, 3);
        Rating rLater = new Rating(newYear.plusHours(2), 5);
        long startHoursAfterMeal = 0;
        long stopHoursAfterMeal = 8;

        PtRatings ptRatings = new PtRatings(Arrays.asList(pt1, pt2), Arrays.asList(rStart, rLater), newYear.plusHours(20));
        PortionPoint pp = PortionPointMaker.getPPForRange(range, Arrays.asList(ptRatings), startHoursAfterMeal, stopHoursAfterMeal);


        //pt1 lasts for 2 + 6 hours, pt2 => 8 hours =>
        /*avg score => (avgScore1 + avgScore2 / totalQuant) => ((2*3. + 6*5.)/8 + 8*5./8) /2. =
            (36/8 + 5) / 2 = (9/2 + 5)/2 = 19/4 = 4.75
         */
        assertEquals(4.75, pp.getScore(), 0.01);
        assertEquals(2.0, pp.getQuant());
    }
    
}
