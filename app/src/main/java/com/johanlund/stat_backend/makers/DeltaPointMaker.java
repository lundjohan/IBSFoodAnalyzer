package com.johanlund.stat_backend.makers;

import com.johanlund.base_classes.Tag;
import com.johanlund.stat_backend.point_classes.TagPoint;
import com.johanlund.util.RatingTime;
import com.johanlund.util.TagsWrapperBase;
import com.johanlund.util.TimePeriod;

import java.util.List;
import java.util.Map;

public class DeltaPointMaker {
    public static Map<String,TagPoint> doDeltaScore(List<TagsWrapperBase>chunks, int startHoursAfterEvent, int
            stopHoursAfterEvent, Map<String, TagPoint> tagPoints) {
        for (TagsWrapperBase chunk : chunks) {
            makeDeltaTagPoints(chunk, startHoursAfterEvent, stopHoursAfterEvent, tagPoints);
        }
        return tagPoints;
    }
    //notice minusMinutes 1.
    /*Extremely ineffective to calculate avg twice, but I just want to see if delta stat give meaningful results...*/
    private static void makeDeltaTagPoints(TagsWrapperBase chunk, int startHoursAfterEvent, int
            stopHoursAfterEvent, Map<String, TagPoint> tagPoints) {
        List<Tag> tagsMaterial = chunk.getTags();
        for (Tag tag : tagsMaterial) {
            Tag t = (Tag)tag;
//1. Calculate normal avg===========================================================================
            TimePeriod tp = new TimePeriod(t.getTime().plusHours(startHoursAfterEvent), t.getTime().plusHours(stopHoursAfterEvent));

            double[] scoreQuant = RatingTime.calcAvgAndWeight(tp, chunk.getScoreTimes(), chunk.getChunkEnd());
            //better being on the safe side
            if (scoreQuant[1] != 1.0) {
                continue;
            }

//2. Calculate the score 1 min before===============================================================
            TimePeriod tpMinBefore = new TimePeriod(t.getTime().minusMinutes(1), t.getTime());
            //TimePeriod tpMinBefore = new TimePeriod(t.getTime().plusHours(startHoursAfterEvent).minusMinutes(1), t.getTime().plusHours(startHoursAfterEvent));
            double[] scoreQuantMinBefore = RatingTime.calcAvgAndWeight(tpMinBefore, chunk.getScoreTimes(), chunk.getChunkEnd());

            //better being on the safe side
            if (scoreQuantMinBefore[1] != 1.0) {
                continue;
            }
//3. Bring it together==============================================================================
            String name = t.getName();

            TagPoint tpInMap = tagPoints.get(name);
            TagPoint tpToInsert = null;

            //max: +5.0 min: -5.0
            double pointsForTag = scoreQuant[0] - scoreQuantMinBefore[0];

            //we have only allowed weight with 1.0
            double quant = 1.0;
            
            if (tpInMap == null) {
                tpToInsert = new TagPoint(name, quant, pointsForTag * quant);
            } else {
                tpToInsert = new TagPoint(name, tpInMap.getQuantity() + quant, tpInMap
                        .getOrig_tot_points() + pointsForTag * quant);
            }
            tagPoints.put(t.getName(), tpToInsert);
        }
    }
}
