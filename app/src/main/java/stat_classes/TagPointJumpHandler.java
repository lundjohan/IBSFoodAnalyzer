package stat_classes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import base_classes.Chunk;
import base_classes.Divider;
import base_classes.Tag;

public class TagPointJumpHandler {

	public static void addJumpScore(Chunk chunk,
			Map<String, TagPoint> tagPoints, int hoursAheadForJump,
			double minScoreDif) {
		List<Tag> allTags = chunk.getTags();
		List<Divider> allDivs = chunk.getDivs();

		for (Tag t : allTags) {
			int foundJump = lookForJump(chunk, t.getTime(), hoursAheadForJump,
					minScoreDif);
			if (foundJump == 0) {
				continue;
			} else { // jump == 1 || -1
				TagPoint tp = tagPoints.get(t.getName());
				if (tp != null)	{
					tp.addJump(foundJump);
				}
				else{
					TagPoint newTp = new TagPoint(t.getName(), t.getSize());
					newTp.addJump(foundJump);
					tagPoints.put(t.getName(), newTp);
				}
			}
		}
	}
	/**
	 *
	 * NB. It is assumed that jump happens not instantly but rather a short time
	 * after tag.
	 *
	 * @return 0 (no jump), 1 (jump up), -1 ("jump" down)
	 */
	private static int lookForJump(Chunk chunk, LocalDateTime timeForTag,
			int hoursAheadForJump, double minScoreDif) {
		List<Divider> divs = chunk.getDivsBetweenAndSometimesOneBefore(
				timeForTag, hoursAheadForJump);

		// no point in continue if these are true.
		// they might happen in start or end of chunk.
		if (divs.size() == 0 || divs.size() == 1) {
			return 0;
		}
		// get the div before tag, it must be the first one in div list.
		Divider divBefore = divs.get(0);

		// check the divs after tagTime occurred.
		// it doesn't matter if there are divs with lower delta-diff in between.
		int toReturn = 0;
		for (int i = 1; i < divs.size(); i++) {
			double diff = divs.get(i).getAfter() - divBefore.getAfter();
			if (Math.abs(diff) >= minScoreDif) {
				toReturn = diff > 0 ? 1 : -1;
			}

		}
		return toReturn;
	}
	public static void addJumpScore(List<Chunk> chunks,
			Map<String, TagPoint> tagPoints, int hoursAheadForJump,
			double minScoreDif) {
		for (Chunk chunk : chunks) {
			addJumpScore(chunk, tagPoints, hoursAheadForJump, minScoreDif);
		}
	}
}
