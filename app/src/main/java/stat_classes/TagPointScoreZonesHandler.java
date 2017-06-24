package stat_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.util.IBSUtil;
import com.ibsanalyzer.util.TimePeriod;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagPointScoreZonesHandler {
	/*
	 * public static void addBlueZonesScore(List<Chunk> chunks, Map<String,
	 * TagPoint> tagPoints, int percentsThatAreBluezones) { if
	 * (percentsThatAreBluezones <= 0 || percentsThatAreBluezones > 100) { throw
	 * new IllegalArgumentException(); } double scoreLimit =
	 * getAbsoluteLimit(chunks, percentsThatAreBluezones); for (Chunk chunk :
	 * chunks) { addBlueZonesScore(chunk, tagPoints, scoreLimit); }
	 * 
	 * }
	 */

	/**
	 * 
	 * @param chunk
	 * @param tagPoints
	 * @param percentsThatAreBluezones
	 *            can only be positive right now
	 */
	/*
	 * private static void addBlueZonesScore(Chunk chunk, Map<String, TagPoint>
	 * tagPoints, double scoreLimit) { addBlueZonesTagsToTagPoints(tagPoints,
	 * chunk, scoreLimit); }
	 */

	/**
	 * 
	 * @param chunks
	 * @param scoreFrom
	 * @param scoreTo
	 * @param hoursNeededAhead
	 *            => describes the amount of hour in end of period (a period
	 *            with score between scoreFrom and scoreTo) that is needed as
	 *            buffert.
	 * @return List of TimePeriods where the periods is <total_time_within_score
	 *         - hoursNeededAhead>
	 */
	public static List<TimePeriod> retrieveScorePeriods(List<Chunk> chunks, double scoreFrom, double scoreTo,
														long hoursNeededAhead) {
		List<TimePeriod> timePeriods = new ArrayList<>();
		for (Chunk ch : chunks) {
			//without this if program will crash in retrieveScorePeriods
			if (ch.getEvents().isEmpty()) {
				continue;
			}
			List<TimePeriod> tps = retrieveScorePeriods(ch, scoreFrom, scoreTo, hoursNeededAhead);
			timePeriods.addAll(tps);
		}
		return timePeriods;
	}

	/**
	 * See retrieveScorePeriods(List<Chunk> ... for more description.
	 * 
	 * Searches from back to front (it is easier since there is no divider at
	 * end but at front. I can start with calculating end score, ending with
	 * that would be more complicated.)
	 */
	private static List<TimePeriod> retrieveScorePeriods(Chunk ch, double scoreFrom, double scoreTo,
			long hoursNeededAhead) {
		List<TimePeriod> timePeriods = retrieveScorePeriods(ch, scoreFrom, scoreTo);
		return TimePeriod.removeHoursAhead(timePeriods, hoursNeededAhead);
	}

	/**
	 * get the FULL time periods between certain scores.
	 * 
	 * @param ch
	 * @param scoreFrom
	 * @param scoreTo
	 * @return
	 */
	private static List<TimePeriod> retrieveScorePeriods(Chunk ch, double scoreFrom, double scoreTo) {
		List<Rating> divs = ch.getDivs();
		IBSUtil.addLastDiv(ch, divs);
		return retrieveScorePeriods(divs, scoreFrom, scoreTo);
	}

	/**
	 * 
	 * Condition: last div in divs is at end of chunk.
	 * 
	 * @return the full timePeriods that occurs if in between scoreFrom and
	 *         scoreTo
	 */
	private static List<TimePeriod> retrieveScorePeriods(List<Rating> divs, double scoreFrom, double scoreTo) {
		// 0. create empty list of TimePeriods
		List<TimePeriod> timePeriods = new ArrayList<>();
		// 1. loop through divs, creating TimePeriod for period with score
		// between scoreFrom and scoreTo
		// describes if timePeriod is being built
		boolean buildingTimePeriod = false;
		LocalDateTime start = null;
		for (int i = 0; i < divs.size(); i++) {
			Rating div = divs.get(i);
			if (!buildingTimePeriod) {
				if (div.getAfter() >= scoreFrom && div.getAfter() <= scoreTo) {
					start = div.getTime();
					buildingTimePeriod = true;
				}
			} else {
				// time to quit period, when score is out of range OR chunk is
				// finished (last div)
				if (div.getAfter() < scoreFrom || div.getAfter() > scoreTo || i >= divs.size() - 1) {
					TimePeriod tp = new TimePeriod(start, div.getTime());
					timePeriods.add(tp);
					buildingTimePeriod = false;
				}
			}
		}
		return timePeriods;
	}

	/**
	 * This is not optimal. Better would be to have a seperate tagpoint class
	 * for blueZones, badZones etc. But it should be easy to change this
	 * function to one with that behaviour.
	 * 
	 * What currently is changed in TagPoint is blueZonesFreq (not a very
	 * flexible name to begin with, but says a lot why this approach is not
	 * good). However, for now thats the score zone stat (bluezone) I am most
	 * interested in
	 * 
	 * @param tagPoints
	 * @param chunks
	 * @param timePeriods
	 */
	public static void addTagsInScoreZones(Map<String, TagPoint> tagPoints, List<Chunk> chunks,
										   List<TimePeriod> timePeriods) {
		Map<String, Double> scoreZonesFreq = new HashMap<>();
		for (TimePeriod tp : timePeriods) {
			List<Tag> tags = TimePeriod.retrieveTagsForPeriod(chunks, tp);
			addTagsQuant(scoreZonesFreq, tags);
		}
		// This one will change when i make seperate class for timePeriods (no
		// more TagPoint used for BlueZones and other score intervals).
		mergeBlueZonesToTP(tagPoints, scoreZonesFreq);
	}

	private static void addTagsQuant(Map<String, Double> scoreZonesFreq, List<Tag> tags) {
		// 1. if name for tag doesnt exist in scoreZonesFreq, add it
		for (Tag t : tags) {
			if (!scoreZonesFreq.containsKey(t.getName())) {
				scoreZonesFreq.put(t.getName(), t.getSize());
			} else {
				scoreZonesFreq.merge(t.getName(), t.getSize(), Double::sum);// same
																			// as
																			// (d1,
																			// d2)->
																			// d1+d2));
			}
		}
	}

	/**
	 * 
	 * This is temporarely until we have own TagPoint system for ScoreZones. It
	 * will probably be removed.
	 * 
	 * @param tagPoints
	 *            => must have same tagNames (key) as do exist in scoreZonesFreq
	 * @param scoreZonesFreq
	 */
	private static void mergeBlueZonesToTP(Map<String, TagPoint> tagPoints, Map<String, Double> scoreZonesFreq) {
		for (Map.Entry<String, Double> entry : scoreZonesFreq.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			if (!tagPoints.containsKey(key)) {
				tagPoints.put(key, new TagPoint(key,value));
				
			}
			tagPoints.computeIfPresent(key, (keyName, v) -> v.addBlueZonesQuant(value));
		}
	}
	/**
	 * @param chunks
	 * @param tagPoints
	 * @param scoreAboveAreBluezones
	 * @param buffertHoursBluezones
	 */
	public static void addBlueZonesScore(List<Chunk> chunks, Map<String, TagPoint> tagPoints,
			double scoreAboveAreBluezones, int buffertHoursBluezones) {
		List<TimePeriod> scorePeriods = TagPointScoreZonesHandler.retrieveScorePeriods(chunks, scoreAboveAreBluezones,5.0, buffertHoursBluezones);
		TagPointScoreZonesHandler.addTagsInScoreZones(tagPoints, chunks, scorePeriods);
		
	}
}
