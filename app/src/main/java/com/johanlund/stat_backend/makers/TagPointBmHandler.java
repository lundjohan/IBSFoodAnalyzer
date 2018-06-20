package com.johanlund.stat_backend.makers;

import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Tag;
import com.johanlund.stat_backend.point_classes.TagPoint;
import com.johanlund.stat_backend.stat_util.ScoreTime;
import com.johanlund.stat_backend.stat_util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;
import java.util.Map;

/**
 * This class is special in that it does not add quantity to tags.
 * Instead of quantity it uses TagPoint.sumBms
 *
 * @author Johan Lund
 */
public class TagPointBmHandler {

    /**
     * the essential thing here is that everything is turned around;
     * instead of counting time from BM to earlier tags, the distance from tags to BM is counted.
     * => variables has been renamed to make this clear (-> hoursAfterTagToStartLookingForBM
     * -> hoursAfterTagToStopLookingForBM)
     *
     * In practice it is the same thing of course.
     * @param chunk
     * @param tagPoints
     * @param furthest_distance_hours_before_bm_limit => this is the longest distance
     * @param shortest_distance_hours_before_bm_limit => this is the shortest distance
     */
    public static void doBmScore(TagsWrapperBase chunk, Map<String, TagPoint> tagPoints,
                                 long furthest_distance_hours_before_bm_limit, long shortest_distance_hours_before_bm_limit) {

        /*rename the variables to tags point of view, so that it easier to get a hang of it.
          Draw distance interval on a paper if you cannot get your head around it!
          The shortest distance remain the shortest distance (the same for the longest difference).
         */
        long hoursAfterTagToStartLookingForBM = shortest_distance_hours_before_bm_limit;
        long hoursAfterTagToStopLookingForBM = furthest_distance_hours_before_bm_limit;


        List<Tag> allTags = chunk.getTags();
        for (Tag tag : allTags) {
            Tag t = (Tag)tag;
            LocalDateTime searchForBMStartTime = t.getTime().plusHours(hoursAfterTagToStartLookingForBM);
            LocalDateTime searchForBMStopTime = t.getTime().plusHours(hoursAfterTagToStopLookingForBM);

                    List<ScoreTime> bmsAhead = Chunk.getBMsBetweenTimes(chunk.getScoreTimes(), searchForBMStartTime, searchForBMStopTime);
            //bristol or completeness
            double sumScore = 0;

            for (ScoreTime bm : bmsAhead) {
                sumScore += bm.getScore();
            }
            String name = t.getName();
            TagPoint tp = tagPoints.get(name);
            if (tp == null) {
                //add no quantity. Bm operates by sumScore
                tp = new TagPoint(name, 0);
                tagPoints.put(name, tp);
            }
            tp.addBM(bmsAhead.size());
            tp.addToBMScore(sumScore);
        }
    }

    public static Map<String, TagPoint> doBmScore(List<TagsWrapperBase> chunks, Map<String, TagPoint> tagPoints,
                                                  long furthest_distance_hours_before_bm_limit, long shortest_distance_hours_before_bm_limit) {
        for (TagsWrapperBase chunk : chunks) {
            doBmScore(chunk, tagPoints, furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit);
        }
        return tagPoints;
    }


}
