package com.johanlund.screens.statistics.avg_stat.common;

/**
 * Created by Johan on 2018-02-10.
 */

import com.johanlund.base_classes.Other;
import com.johanlund.base_classes.Tag;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.stat_backend.makers.TagPointMaker;
import com.johanlund.stat_backend.point_classes.TagPoint;
import com.johanlund.stat_backend.stat_util.ScoreTime;
import com.johanlund.stat_backend.stat_util.TagsWrapperBase;

import org.junit.Test;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;


/**
 * Created by Johan on 2018-02-10.
 */
 /*Tag t1n = TagWithoutTime.toTagWithTime(t1,ldt1);
         Tag t2n = TagWithoutTime.toTagWithTime(t2,ldt2);
         List<Tag> tagsn = TagWithoutTime.toTagsWithTime(tags1, ldt1);

         */
public class AvgScoreTest {
    /**
     * this test checks that avg rating stat is working properly.
     * The method that in effect is tested is TagPointMaker.doAvgScore(...);
     */

    /*
    Simple test case with only one TagWithoutTime and one rating (before the tag)
     */
    @Test
    public void simpleAvgRatingTest() {
        //create some tags...
        List<TagWithoutTime> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        TagWithoutTime t1 = new TagWithoutTime( "Butter", 1.0);
        tags1.add(t1);

        //create a Rating that appears slightly before
        ScoreTime r1 = new ScoreTime(ldt1.minusHours(1), 3);

        //make a chunkend 10 hours in the future, just to make rating extend to it
        LocalDateTime breakTime = ldt1.plusHours(10);

        List<TagsWrapperBase> chunks1 = TagsWrapper.makeTagsWrappers(TagWithoutTime.toTagsWithTime(tags1, ldt1), Arrays.asList(r1),
                Arrays.asList(breakTime));
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
        List<TagWithoutTime> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        TagWithoutTime t1 = new TagWithoutTime( "Butter", 1.0);
        tags1.add(t1);
        //...and add them to an TagsWrapper

        //make a chunkend 10 hours in the future, just to make rating extend to it
        LocalDateTime breakTime = ldt1.plusHours(10);
        List<Tag> tagsn = TagWithoutTime.toTagsWithTime(tags1, ldt1);
        List<TagsWrapperBase> chunks1 = TagsWrapper.makeTagsWrappers(tagsn, new
                ArrayList<ScoreTime>(), Arrays.asList(breakTime));
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
        List<TagWithoutTime> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        TagWithoutTime t1 = new TagWithoutTime( "Butter", 1.0);
        tags1.add(t1);

        //create a Rating that appears slightly before
        ScoreTime r1 = new ScoreTime(ldt1.minusHours(1), 3);

        //create an extra butter further on, just to make rating extend to it
        LocalDateTime ldt2 = LocalDateTime.of(2017, Month.JANUARY, 1, 20, 0);
        TagWithoutTime t2 = new TagWithoutTime( "Butter", 1.0);
        List<TagWithoutTime> tags2 = new ArrayList<>();
        tags2.add(t2);

        //join (will be in sort order)
        tags1.addAll(tags2);

        //add a rating at same time as the second Other, and with a higher score
        ScoreTime r2 = new ScoreTime(ldt2, 6);

        //add a break later on just to make sure that chunk isn't cutting off
        LocalDateTime breakTime = ldt2.plusHours(20);
        List<TagsWrapperBase> chunks1 = TagsWrapper.makeTagsWrappers(TagWithoutTime.toTagsWithTime(tags1, ldt1), Arrays.asList(r1, r2)
                , Arrays.asList(breakTime));
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
        List<TagWithoutTime> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        TagWithoutTime t1 = new TagWithoutTime( "Butter", 1.0);
        tags1.add(t1);

        //create a Rating that appears slightly before
        ScoreTime r1 = new ScoreTime(ldt1.minusHours(1), 3);

        //create an event further on, just to make rating extend to it
        LocalDateTime breakTime = ldt1.plusHours(10);
        List<Tag> tagsn = TagWithoutTime.toTagsWithTime(tags1, ldt1);
        List<TagsWrapperBase> chunks1 = TagsWrapper.makeTagsWrappers(tagsn, Arrays.asList(r1),
                Arrays.asList(breakTime));
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
         *
         *   ...--------------| Time for Chunk
         *                 |-----| Time for calculation of score
         *
         *
         *  I think this is a good solution. Before this no TagPoint would have been returned at
         *  all, and we would have lost valuable information.
         */


        assertEquals(0.5, tagPoints.get("Butter").getQuantity());
        assertEquals(3.0, tagPoints.get("Butter").getOrigAvgScore());
    }

    //extremely simliar to another test above
    @Test
    public void startHoursAfterEventWorksProperlyTest() {
        //create some tags...
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        TagWithoutTime t1 = new TagWithoutTime( "Butter", 1.0);

        //create a Rating that appears slightly before
        ScoreTime r1 = new ScoreTime(ldt1.minusHours(1), 3);

        //create an extra butter further on, just to make rating extend to it
        LocalDateTime ldt2 = LocalDateTime.of(2017, Month.JANUARY, 1, 20, 0);
        TagWithoutTime t2 = new TagWithoutTime( "Butter", 1.0);

        //add a rating at same time as the second Other, and with a higher score
        ScoreTime r2 = new ScoreTime(ldt2, 6);

        //add another other later on just to make sure that chunk isn't cutting off
        LocalDateTime breakTime = ldt2.plusHours(20);

        Tag t1n = TagWithoutTime.toTagWithTime(t1,ldt1);
        Tag t2n = TagWithoutTime.toTagWithTime(t2,ldt2);
        List<TagsWrapperBase> chunks1 = TagsWrapper.makeTagsWrappers(Arrays.asList(t1n, t2n),
                Arrays.asList(r1, r2), Arrays.asList(breakTime));
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

    /**
     * |----------------| Time for Chunk
     * |---| Time for calculation of score
     */

    @Test
    public void testThatNoCalculationIsDoneWhenStartHoursAreAfterChunk() {
        int startHoursAfterEvent = 40;
        int stopHoursAfterEvent = 41;

        //create some tags...
        List<TagWithoutTime> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 10, 0);
        TagWithoutTime t1 = new TagWithoutTime( "Butter", 1.0);
        tags1.add(t1);

        //create a Rating that appears slightly before ...
        ScoreTime r1 = new ScoreTime(ldt1.minusHours(1), 3);

        //and a Other event that appears slightly after ...
        LocalDateTime breakTime = ldt1.plusHours(1);
        List<Tag> tagsn = TagWithoutTime.toTagsWithTime(tags1, ldt1);
        List<TagsWrapperBase> chunks = TagsWrapper.makeTagsWrappers(tagsn, Arrays.asList(r1),
                Arrays.asList(breakTime));
        Map<String, TagPoint> tagPoints = new HashMap<>();


        tagPoints = TagPointMaker.doAvgScore(chunks, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints);

        //startHoursAfterEvent is WAAAY after last event in chunk => butter should not even appear
        // in list. Therefore =>
        assertEquals(0, tagPoints.size());

    }

    /**
     * x == NO Rating score there(due to omission by user)
     * <p>
     * |xxx------| Time for Chunk (the TagWithoutTime occurs in the beginning)
     * |------| Time for calculation of score, there is only in 2nd half that score should be
     * assigned
     */

    @Test
    public void testThatScoresAreCalculatedOnlyForPartsOfChunkWhereThereIsRating() {
        //Main story: TagWithoutTime will have 3 hours without score and three hours with score.
        // => quantity should be half, and avg score the same (4.0).

        //create other event...
        List<TagWithoutTime> tags1 = new ArrayList<>();
        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0);
        TagWithoutTime t1 = new TagWithoutTime( "Butter", 2.0);
        tags1.add(t1);
        //...and add them to an event
        Other otherBeforeRating = new Other(ldt1, tags1);

        //create first rating event 3 hours AFTER otherBeforeRating...
        ScoreTime firstRating = new ScoreTime(ldt1.plusHours(3), 4);

        //create later event just to expand chunk after stop hours
        LocalDateTime breakTime = ldt1.plusHours(10);

        int startHoursAfterEvent = 0;
        int stopHoursAfterEvent = 6;
        List<Tag> tagsn = TagWithoutTime.toTagsWithTime(tags1, ldt1);
        List<TagsWrapperBase> chunks = TagsWrapper.makeTagsWrappers(tagsn, Arrays.asList
                (firstRating), Arrays.asList(breakTime));
        Map<String, TagPoint> tagPoints = new HashMap<>();
        tagPoints = TagPointMaker.doAvgScore(chunks, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints);


        assertEquals(1, tagPoints.size());
        //half the time => half the quantity
        assertEquals(1., tagPoints.get("Butter").getQuantity());
        //avg score is not affected
        assertEquals(4.0, tagPoints.get("Butter").getOrigAvgScore());


    }

    /**
     * x == NO Rating score there(due to omission by user)
     * <p>
     * |xxx| Time for Chunk
     * |----| Time for calculation of score )
     */

    @Test
    public void two() {

    }

    /**
     * x == NO Rating score there(due to omission by user)
     * <p>
     * |xxx| Time for Chunk
     * |----| Time for calculation of score )
     */

    @Test
    public void three() {

    }
}

