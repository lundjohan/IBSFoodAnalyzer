package com.ibsanalyzer.statistics;

/**
 * Created by Johan on 2018-02-10.
 */

import com.ibsanalyzer.base_classes.Break;
import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stat_classes.TagPointMaker;

import static junit.framework.Assert.assertEquals;


/**
 * Created by Johan on 2018-02-10.
 */

public class AvgScoreTest {
    /**
     * this test checks that avg rating stat is working properly.
     * The method that in effect is tested is TagPointMaker.doAvgScore(...);
     */
    @Before
    public void init(){

    }

    @Test
    public void avgRatingTest() {
        //empty event list

        //create some tags...
        List<Tag>tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY,1,10,0);
        Tag t1 = new Tag(ldt1 , "Butter", 1.0);
        tags1.add(t1);
        //...and add them to an event
        Other other1 = new Other(ldt1, tags1);


        //create a Rating that appears slightly before
        Rating r1 = new Rating(ldt1.minusHours(1), 3);

        //create an event further on, just to make rating extend to it
        Other other2 = new Other(ldt1.plusHours(10), new ArrayList<Tag>());

        List<Event>events1 = new ArrayList<>();
        events1.add(r1);
        events1.add(other1);
        events1.add(other2);

        List<Chunk>chunks1 = Chunk.makeChunksFromEvents(events1, new ArrayList<Break>());
        int startHoursAfterEvent = 0;
        int stopHoursAfterEvent = 2;
        Map<String, TagPoint> tagPoints = new HashMap<>();

        //Above is a simple one, the score of Butter should be 3.
        tagPoints =  TagPointMaker.doAvgScore(chunks1, startHoursAfterEvent, stopHoursAfterEvent, tagPoints);
        assertEquals(1, tagPoints.size());
        assertEquals(tagPoints.get("Butter").getQuantity(),1.0 );
        assertEquals(tagPoints.get("Butter").getOrigAvgScore(),3.0);
    }
}
