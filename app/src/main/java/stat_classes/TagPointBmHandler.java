package stat_classes;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.List;
import java.util.Map;

/**
 * This class is special in that it does not add quantity to tags.
 * Instead of quantity it uses TagPoint.sumBms
 *
 * @author Johan Lund
 */
public class TagPointBmHandler {

    public static void addBmScore(Chunk chunk, Map<String, TagPoint> tagPoints,
                                  long hoursAheadForBm) {
        List<Tag> allTags = chunk.getTags();
        for (Tag t : allTags) {
            List<Bm> bmsAhead = chunk.getBMsAfterTime(chunk, t.getTime(),
                    hoursAheadForBm);
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

    public static Map<String, TagPoint> addBmScore(List<Chunk> chunks,
                                                   Map<String, TagPoint> tagPoints, long
                                                           hoursAheadForBm) {
        for (Chunk chunk : chunks) {
            addBmScore(chunk, tagPoints, hoursAheadForBm);
        }
        return tagPoints;
    }


}
