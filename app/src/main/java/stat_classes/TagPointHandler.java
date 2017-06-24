package stat_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.constants.Constants;
import com.ibsanalyzer.importer.Importer;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.List;
import java.util.Map;


public class TagPointHandler {

	public static Map<String, TagPoint> retrieveTagPoints(String fileName) throws Exception {
		List<Chunk> chunks = Importer.parseToChunks(fileName);
		for (Chunk ch: chunks) {
			if (ch.getDays().isEmpty()) {
				System.out.println("ch is empty");
			}
		
		}
		Map<String, TagPoint> tagPoints = TagPointMaker.doBasicScore(chunks, Constants.HOURS);
		//TagPointJumpHandler.addJumpScore(chunks, tagPoints, Constants.JUMP_HOURS_LIMIT, Constants.JUMP_MIN_SCORE_DIFF);
		TagPointBMHandler.addBMScore(chunks, tagPoints, Constants.HOURS_AHEAD_FOR_BM);
		//TagPointRelationsAdjuster.doRelationsScore(chunks, tagPoints, Constants.HOURS);
		TagPointScoreZonesHandler.addBlueZonesScore(chunks, tagPoints, Constants.SCORE_ABOVE_ARE_BLUEZONES, Constants.BUFFERT_HOURS_BLUEZONES);
		TagPointPortionsHandler.addPortionScore(chunks, tagPoints, Constants.HOURS_COHERENT_TIME_FOR_PORTIONS, Constants.ONE_PORTION);
		TagPointPortionsHandler.addPortionScore(chunks, tagPoints, Constants.HOURS_COHERENT_TIME_FOR_PORTIONS, Constants.TWO_PORTIONS);
		TagPointPortionsHandler.addPortionScore(chunks, tagPoints, Constants.HOURS_COHERENT_TIME_FOR_PORTIONS, Constants.THREE_PORTIONS);
		TagPointPortionsHandler.addPortionScore(chunks, tagPoints, Constants.HOURS_COHERENT_TIME_FOR_PORTIONS, Constants.FOUR_PORTIONS);
		return tagPoints;
	}

    public static List<TagPoint> retrieveAvgScoreTP(List<Chunk> chunks, int hoursAheadForAvg) {
    }

	public static List<TagPoint> retrieveBlueZoneScoreTP(List<Chunk> chunks, double scoreBluezonesFrom, 
											   double scoreBluezonesTo, int hoursAheadForBluezones) {
	}

	public static List<TagPoint> retrieveCompleteScoreTP(List<Chunk> chunks, int hoursAheadForComplete) {
	}

	public static List<TagPoint> retrieveBristolScoreTP(List<Chunk> chunks, int hoursAheadForBristol) {
	}
}
