package stat_classes;

import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Tag;
import com.johanlund.tagpoint_classes.TagPoint;

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
    public static void addBmScore(Chunk chunk, Map<String, TagPoint> tagPoints,
                                  long furthest_distance_hours_before_bm_limit, long shortest_distance_hours_before_bm_limit) {

        /*rename the variables to tags point of view, so that it easier to get a hang of it.
          Draw distance interval on a paper if you cannot get your head around it!
          The shortest distance remain the shortest distance (the same for the longest difference).
         */
        long hoursAfterTagToStartLookingForBM = shortest_distance_hours_before_bm_limit;
        long hoursAfterTagToStopLookingForBM = furthest_distance_hours_before_bm_limit;


        List<Tag> allTags = chunk.getTags();
        for (Tag t : allTags) {
            LocalDateTime searchForBMStartTime = t.getTime().plusHours(hoursAfterTagToStartLookingForBM);
            LocalDateTime searchForBMStopTime = t.getTime().plusHours(hoursAfterTagToStopLookingForBM);

                    List<Bm> bmsAhead = chunk.getBMsBetweenTimes(searchForBMStartTime, searchForBMStopTime);
            int completeness = 0;
            double sumBristol = 0;

            for (Bm bm : bmsAhead) {
                completeness += bm.getComplete();
                sumBristol += bm.getBristol();
            }
            String name = t.getName();
            TagPoint tp = tagPoints.get(name);
            if (tp == null) {
                //add no quantity. Bm operates by sumBristol
                tp = new TagPoint(name, 0);
                tagPoints.put(name, tp);
            }
            tp.addBM(bmsAhead.size());
            tp.addCompleteness(completeness);
            tp.addToBristol(sumBristol);

        }

    }

    public static Map<String, TagPoint> addBmScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints,
                                                   long furthest_distance_hours_before_bm_limit, long shortest_distance_hours_before_bm_limit) {
        for (Chunk chunk : chunks) {
            addBmScore(chunk, tagPoints, furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit);
        }
        return tagPoints;
    }


}
