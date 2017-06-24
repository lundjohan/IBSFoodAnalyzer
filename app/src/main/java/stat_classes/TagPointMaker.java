package stat_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagPointMaker {
	/**
	 * Returns a map of String, TagPoints.
	 * Each TagPoint consist of name, quantity, orig_tot_points (the latter based on avg score hours ahead).
	 */
	private static void makeTagPoints(Chunk chunk, int hours, Map<String, TagPoint> tagPoints) {
		List<Tag>tagsMaterial = chunk.getTags(hours);
		for (Tag tag: tagsMaterial){
			String name = tag.getName();
			double quantity = tag.getSize();

			double pointsForTag = chunk.calcAvgScoreFromToTime(tag.getTime(), hours);
			TagPoint tpInMap = tagPoints.get(name);
			TagPoint tpToInsert = null;

			if (tpInMap == null){
				tpToInsert = new TagPoint(name,tag.getSize(), pointsForTag*quantity);
			}
			else{
				tpToInsert = new TagPoint (name, tpInMap.getQuantity() + quantity, tpInMap.getOrig_tot_points() + pointsForTag*quantity);
			}
			tagPoints.put(tag.getName(), tpToInsert);
		}
	}

	public static Map<String, TagPoint> makeTagPoints(Chunk chunk, int hours) {
		Map<String, TagPoint> tagPoints = new HashMap<>();
		makeTagPoints(chunk, hours, tagPoints);
		return tagPoints;
	}
	public static Map<String, TagPoint> doBasicScore(List<Chunk> chunks, int hours){
		Map<String, TagPoint> tagPoints = new HashMap<>();
		for (Chunk chunk:chunks){
			makeTagPoints(chunk, hours, tagPoints);
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
	    List<Tag>otherTags = chunk.getSurroundingTags(tag0, tag0.getTime().minusHours(hours), tag0.getTime().plusHours(hours));
	    	    othersAvg = tag0.avgPoints();
	    	    othersQuantity = 0
	    	    for tag in otherTags:
	    	        tagPoint = tagPoint[tag.name]
	    	        weight = calcWeight(tag0.datetime, tag.datetime, timeDeltaSurrounder)
	    	        othersTot = (othersAvg * othersQuantity + tagPoint.avgPoints()-othersAvg)/(othersQuantity+1)
	    	        othersQuantity += 1
	    	    return othersAvg
	}*/
}