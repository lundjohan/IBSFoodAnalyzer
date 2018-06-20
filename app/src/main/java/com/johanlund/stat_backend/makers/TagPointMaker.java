package com.johanlund.stat_backend.makers;

import com.johanlund.base_classes.Tag;
import com.johanlund.stat_backend.point_classes.TagPoint;
import com.johanlund.stat_backend.stat_util.RatingTime;
import com.johanlund.stat_backend.stat_util.TagsWrapperBase;
import com.johanlund.stat_backend.stat_util.TimePeriod;

import java.util.List;
import java.util.Map;

public class TagPointMaker {

    private static void makeTagPoints(TagsWrapperBase chunk, int startHoursAfterEvent, int
            stopHoursAfterEvent, Map<String, TagPoint> tagPoints) {
        List<Tag> tagsMaterial = chunk.getTags();
        for (Tag tTemp : tagsMaterial) {
            Tag t = (Tag)tTemp;
            TimePeriod tp = new TimePeriod(t.getTime().plusHours(startHoursAfterEvent), t.getTime().plusHours(stopHoursAfterEvent));

            /*very waste of resources giving all Ratings everytime (since tagsMaterial and Ratings
            are sorted, perhaps a cache can be used. A wrapper class called RatingCache perhaps?)*/

            double[] scoreQuant = RatingTime.calcAvgAndWeight(tp, chunk.getScoreTimes(), chunk.getChunkEnd());
            if (scoreQuant[1] == 0.0) {
                continue;
            }
            String name = t.getName();

            TagPoint tpInMap = tagPoints.get(name);
            TagPoint tpToInsert = null;

            double pointsForTag = scoreQuant[0];
            //multiply tag size with factor
            double quant = t.getSize()*scoreQuant[1];

            if (tpInMap == null) {
                tpToInsert = new TagPoint(name, quant, pointsForTag * quant);
            } else {
                tpToInsert = new TagPoint(name, tpInMap.getQuantity() + quant, tpInMap
                        .getOrig_tot_points() + pointsForTag * quant);
            }
            tagPoints.put(t.getName(), tpToInsert);
        }
    }

    public static Map<String, TagPoint> doAvgScore(List<TagsWrapperBase> chunks, int waitHoursAfterEvent,
                                                   int stopHoursAfterEvent, Map<String,
            TagPoint> tagPoints) {
        for (TagsWrapperBase chunk : chunks) {
            makeTagPoints(chunk, waitHoursAfterEvent, stopHoursAfterEvent, tagPoints);
        }
        return tagPoints;
    }
    /*public static Map<String, TagPoint> calcTagPoints(List<Chunk> chunks,
            int buffertHoursEnd) {
		Map<String, TagPoint> tagPoints = initTagPoints(chunks);
		int i = Constants.LOOPS_FOR_TAGPOINTS;
		while (i > 0) {
			changeTagPointsOnRelations(tagPoints, chunks);
			i--;
		}
		return tagPoints;
	}

	private static Map<String, TagPoint> initTagPoints(List<Chunk> chunks) {
		Map<String, TagPoint> tagPoints = new HashMap<>();
		for (Chunk chunk : chunks) {
			addTagPoints(tagPoints, chunk);
		}
		// TODO Auto-generated method stub
		return tagPoints;
	}
	private static void changeTagPointsOnRelations(
			Map<String, TagPoint> tagPoints, List<Chunk> chunks) {

		Map<String, TagPoint> original = new HashMap<>();
		for (Map.Entry<String, TagPoint> entry : tagPoints.entrySet()) {
			entry.getValue().setPlusMinus(0); // zero out plus minus in dict
			original.put(entry.getKey(), entry.getValue().copy());
		}

		for (Chunk chunk : chunks) {
			for (Tag tag : chunk.getTags()) {
				double surroundingTagsAvgScore = getAvgSurroundingPointsForTags(
						chunk, tag, Constants.HOURS, original);
				boolean tagsScoreShouldBeHigher = original.get(tag.getName())
						.avgPointsAfter() < surroundingTagsAvgScore;
				if (tagsScoreShouldBeHigher) { // "good" thing for tag score,
												// since it seems to be
												// underrated.
					tagPoints.get(tag.getName()).addPlusMinus(1);
				} else if (!tagsScoreShouldBeHigher) {
					tagPoints.get(tag.getName()).addPlusMinus(-1);
				}
			}
		}

	}

	private static double getAvgSurroundingPointsForTags(Chunk chunk, Tag tag0,
			long hours, Map<String, TagPoint> original) {
	    List<Tag>otherTags = chunk.getSurroundingTags(tag0, tag0.getTime().minusHours(hours),
	    tag0.getTime().plusHours(hours));
	    	    othersAvg = tag0.avgPoints();
	    	    othersQuantity = 0
	    	    for tag in otherTags:
	    	        tagPoint = tagPoint[tag.name]
	    	        weight = calcWeight(tag0.datetime, tag.datetime, timeDeltaSurrounder)
	    	        othersTot = (othersAvg * othersQuantity + tagPoint.avgPoints()-othersAvg)/
	    	        (othersQuantity+1)
	    	        othersQuantity += 1
	    	    return othersAvg
	}*/
}