package com.johanlund.util;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import static junit.framework.Assert.assertEquals;

public class TimePeriodTests {

    @Test
    public void getLengthTest() {
        //Make a time period that last for 10 seconds.
        LocalDateTime start = LocalDateTime.of(2018, Month.JANUARY, 1,0,0,0);
        LocalDateTime end = LocalDateTime.of(2018, Month.JANUARY, 1,0,0,10);
        TimePeriod tp = new TimePeriod(start, end);
        assertEquals(10, tp.getLengthSec());
    }
    @Test
    public void getQuoteTest() {
        //Make a time period that last for 10 seconds.
        LocalDateTime aStart = LocalDateTime.of(2018, Month.JANUARY, 1,0,0,0);
        LocalDateTime aEnd = LocalDateTime.of(2018, Month.JANUARY, 1,0,0,10);
        TimePeriod tpAbove = new TimePeriod(aStart, aEnd);

        //Make a time period that last for 20 seconds.
        LocalDateTime bStart = LocalDateTime.of(2018, Month.JANUARY, 1,0,0,0);
        LocalDateTime bEnd = LocalDateTime.of(2018, Month.JANUARY, 1,0,0,20);
        TimePeriod tpBelow = new TimePeriod(bStart, bEnd);

        double quote = TimePeriod.getQuote(tpAbove, tpBelow);

        assertEquals(0.5, quote);
    }
}
