package stat_classes;

import java.util.List;
import java.util.Map;

import base_classes.BM;
import base_classes.Chunk;
import base_classes.Tag;
/**
 * This class is special in that it does not add quantity to tags.
 * Instead of quantity it uses TagPoint.sumBMs
 * @author Johan Lund
 *
 */
public class TagPointBMHandler {

	public static void addBMScore(Chunk chunk, Map<String, TagPoint> tagPoints,
			long hoursAheadForBM) {
		List<Tag> allTags = chunk.getTags();
		for (Tag t : allTags) {
			List<BM> bmsAhead = chunk.getBMsAfterTime(chunk, t.getTime(),
					hoursAheadForBM);
			int completeness = 0;
			double sumBristol = 0;
			for (BM bm : bmsAhead) {
				completeness += bm.getCompleteness();
				sumBristol += bm.getBristol();
			}
			String name = t.getName();
			TagPoint tp = tagPoints.get(name);
			if (tp == null) {
				//add no quantity. BM operates by sumBristol
				tp = new TagPoint(name, 0);
				tagPoints.put(name, tp);
			}
			tp.addBM(bmsAhead.size());
			tp.addCompleteness(completeness);
			tp.addToBristol(sumBristol);

		}

	}

	public static void addBMScore(List<Chunk> chunks,
			Map<String, TagPoint> tagPoints, long hoursAheadForBM) {
		for (Chunk chunk : chunks) {
			addBMScore(chunk, tagPoints, hoursAheadForBM);
		}
	}


}
