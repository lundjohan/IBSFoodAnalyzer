package com.ibsanalyzer.statistics;

/**
 * Created by Johan on 2018-02-10.
 */

import com.google.gson.annotations.Since;
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
    public void init() {

    }

    /*
    Simple test case with only one tag and one rating (before the tag)
     */
    @Test
    public void simpleAvgRatingTest() {
        //create some tags...
        List<Tag> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        Tag t1 = new Tag(ldt1, "Butter", 1.0);
        tags1.add(t1);
        //...and add them to an event
        Other other1 = new Other(ldt1, tags1);


        //create a Rating that appears slightly before
        Rating r1 = new Rating(ldt1.minusHours(1), 3);

        //create an event further on, just to make rating extend to it
        Other other2 = new Other(ldt1.plusHours(10), new ArrayList<Tag>());

        List<Event> events1 = new ArrayList<>();
        events1.add(r1);
        events1.add(other1);
        events1.add(other2);

        List<Chunk> chunks1 = Chunk.makeChunksFromEvents(events1, new ArrayList<Break>());
        int startHoursAfterEvent = 0;
        int stopHoursAfterEvent = 2;
        Map<String, TagPoint> tagPoints = new HashMap<>();

        //Above is a simple one, the score of Butter should be 3.
        tagPoints = TagPointMaker.doAvgScore(chunks1, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints);
        assertEquals(1, tagPoints.size());
        assertEquals(1.0, tagPoints.get("Butter").getQuantity());
        assertEquals(3.0, tagPoints.get("Butter").getOrigAvgScore());
    }

    /**
     * Here, no rating has been added. But it still shouldnt crasch. TagPointMaker.doAvgScore
     * should just return an empty list.
     */

    @Test
    public void noRatingInAvgStatTest() {
        //create some tags...
        List<Tag> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        Tag t1 = new Tag(ldt1, "Butter", 1.0);
        tags1.add(t1);
        //...and add them to an event
        Other other1 = new Other(ldt1, tags1);

        //create an event further on, just to make rating extend to it
        Other other2 = new Other(ldt1.plusHours(10), new ArrayList<Tag>());

        List<Event> events1 = new ArrayList<>();
        events1.add(other1);
        events1.add(other2);
        List<Chunk> chunks1 = Chunk.makeChunksFromEvents(events1, new ArrayList<Break>());
        int startHoursAfterEvent = 0;
        int stopHoursAfterEvent = 2;
        Map<String, TagPoint> tagPoints = new HashMap<>();

        //Above is a simple one, the score of Butter should be 3.
        tagPoints = TagPointMaker.doAvgScore(chunks1, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints);
        assertEquals(0, tagPoints.size());

    }

    //a lot of copying and pasting from above
    @Test
    public void twoTagsAndTwoRatingsAvgStatTest() {
        //create some tags...
        List<Tag> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        Tag t1 = new Tag(ldt1, "Butter", 1.0);
        tags1.add(t1);
        //...and add them to an event
        Other other1 = new Other(ldt1, tags1);


        //create a Rating that appears slightly before
        Rating r1 = new Rating(ldt1.minusHours(1), 3);


        //create an extra butter further on, just to make rating extend to it
        LocalDateTime ldt2 = LocalDateTime.of(2017, Month.JANUARY, 1, 20, 0);
        Tag t2 = new Tag(ldt2, "Butter", 1.0);
        List<Tag> tags2 = new ArrayList<>();
        tags2.add(t2);
        Other other2 = new Other(ldt2, tags2);

        //add a rating at same time as the second Other, and with a higher score
        Rating r2 = new Rating(ldt2, 6);

        //add another other later on just to make sure that chunk isn't cutting off
        Other other3 = new Other(ldt2.plusHours(20), new ArrayList<Tag>());

        List<Event> events1 = new ArrayList<>();
        events1.add(r1);
        events1.add(other1);
        events1.add(other2);
        events1.add(r2);
        events1.add(other3);

        List<Chunk> chunks1 = Chunk.makeChunksFromEvents(events1, new ArrayList<Break>());
        int startHoursAfterEvent = 0;
        int stopHoursAfterEvent = 20;
        Map<String, TagPoint> tagPoints = new HashMap<>();

        //notice that
        //the score of first Butter should be (3.0*10h + 6.0*10h)/20h = 4.5.
        //the score of the second Butter should be (6*20h)/20h = 6.0
        //the total score should be (6.0+4.5)/2 = 5.25
        tagPoints = TagPointMaker.doAvgScore(chunks1, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints);
        assertEquals(1, tagPoints.size());
        assertEquals(2.0, tagPoints.get("Butter").getQuantity());
        assertEquals(5.25, tagPoints.get("Butter").getOrigAvgScore());
    }

    /**
     * hur ska detta se ut i algoritmen? En lägre viktning?
     * Jag tycker det verkar rimligt. For example: if a chunk ends 5 times after butter, but
     * algorithm is told that stopHours is 10, it would be reasonable to add to Butter TagPoint
     * this Butters Rating with half its weight.
     */
    @Test
    public void ratingsAvgStatStillGivesScoreEvenIfHoursIsntEnoughTest() {
        //create some tags...
        List<Tag> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        Tag t1 = new Tag(ldt1, "Butter", 1.0);
        tags1.add(t1);
        //...and add them to an event
        Other other1 = new Other(ldt1, tags1);


        //create a Rating that appears slightly before
        Rating r1 = new Rating(ldt1.minusHours(1), 3);

        //create an event further on, just to make rating extend to it
        Other other2 = new Other(ldt1.plusHours(10), new ArrayList<Tag>());

        List<Event> events1 = new ArrayList<>();
        events1.add(r1);
        events1.add(other1);
        events1.add(other2);

        List<Chunk> chunks1 = Chunk.makeChunksFromEvents(events1, new ArrayList<Break>());
        int startHoursAfterEvent = 0;

        //notice that 20 hours before stop, even though Chunk will be truncated
        int stopHoursAfterEvent = 20;
        Map<String, TagPoint> tagPoints = new HashMap<>();

        //Above is a simple one, the score of Butter should be 3.
        tagPoints = TagPointMaker.doAvgScore(chunks1, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints);
        assertEquals(1, tagPoints.size());

        /**this is the interesting one. Since only half of the hours has been fullfilled after
         *  tag, only half of the quantity is put.
         *
         *  I think this is a good solution. Before this no TagPoint would have been returned at
         *  all, and we would have lost valuable information.
         */


        assertEquals(0.5, tagPoints.get("Butter").getQuantity());
        assertEquals(3.0, tagPoints.get("Butter").getOrigAvgScore());
    }
    //extremely simliar to another test above
    @Test
    public void startHoursAfterEventWorksProperlyTest(){
        //create some tags...
        List<Tag> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        Tag t1 = new Tag(ldt1, "Butter", 1.0);
        tags1.add(t1);
        //...and add them to an event
        Other other1 = new Other(ldt1, tags1);


        //create a Rating that appears slightly before
        Rating r1 = new Rating(ldt1.minusHours(1), 3);


        //create an extra butter further on, just to make rating extend to it
        LocalDateTime ldt2 = LocalDateTime.of(2017, Month.JANUARY, 1, 20, 0);
        Tag t2 = new Tag(ldt2, "Butter", 1.0);
        List<Tag> tags2 = new ArrayList<>();
        tags2.add(t2);
        Other other2 = new Other(ldt2, tags2);

        //add a rating at same time as the second Other, and with a higher score
        Rating r2 = new Rating(ldt2, 6);

        //add another other later on just to make sure that chunk isn't cutting off
        Other other3 = new Other(ldt2.plusHours(20), new ArrayList<Tag>());

        List<Event> events1 = new ArrayList<>();
        events1.add(r1);
        events1.add(other1);
        events1.add(other2);
        events1.add(r2);
        events1.add(other3);

        List<Chunk> chunks1 = Chunk.makeChunksFromEvents(events1, new ArrayList<Break>());

        //HERE, startHoursAfterEvent is NOT zero this time
        int startHoursAfterEvent = 5;
        int stopHoursAfterEvent = 20;
        Map<String, TagPoint> tagPoints = new HashMap<>();

        //notice that
        //the score of first Butter should be (3.0*5h + 6.0*10h)/15h = (15 + 60)/ 15 = 75/15 = 5.0.
        //the score of the second Butter should be (6*15h)/15h = 6.0
        //the total score should be (6.0+5.0)/2 = 5.5
        tagPoints = TagPointMaker.doAvgScore(chunks1, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints);
        assertEquals(1, tagPoints.size());
        assertEquals(2.0, tagPoints.get("Butter").getQuantity());
        assertEquals(5.5, tagPoints.get("Butter").getOrigAvgScore());

    }
}

