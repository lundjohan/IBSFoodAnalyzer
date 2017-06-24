package stat_classes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import base_classes.Chunk;
import base_classes.Meal;
import constants.Constants;
import util.TPUtil;

public class TagPointPortionsHandler {

	/**
	 * ------------------------------
	 * Terminology:
	 *
	 * lookBehind: the period defined by parameter <<hours>> where all meals
	 * portionSizes must fulfill <= <<portionSize>>
	 *
	 * scorePeriod: the period after lookBehind, where score is calculated.
	 * thisperiod last until either chunk has reached end or meal with
	 * portion size >= <<portionSize>>
	 * --------------------------------
	 *
	 * Granted: First div in chunk is the before or the same place as the first
	 * tag in chunk.
	 *
	 * @param hours,
	 *            refers to the coherent time that must have been fulfilled
	 *            before
	 * @throws Exception
	 */
	static void addPortionScore(Chunk chunk, Map<String, TagPoint> tagPoints,
			long hours, long portionSize) throws Exception {
		List<Meal> meals = chunk.getMeals();
		if (meals.size()<1){
			return;
		}
		LocalDateTime startOfLookBehind = TPUtil.getStartTime(chunk);
		LocalDateTime startOfScorePeriod = startOfLookBehind.plusHours(hours);
		//quantity is for "portionTagPoints" hours of scoreTime
		double quantity = 0;
		//here: score = sum of deltaScore*deltaHours (deltaHours == time that deltaScore last)
		double score = 0;
		while (true){
			 meals = TPUtil.sliceLeft (meals,startOfLookBehind);
			 if (meals.isEmpty()){
				 break;
			 }

			//returns null if reaches end of chunk before it has come to startOfScorePeriod.
			LocalDateTime endOfScorePeriod = getEndOfScorePeriod(meals, startOfLookBehind, startOfScorePeriod, TPUtil.getEndTime(chunk), portionSize);
			if (endOfScorePeriod == null){
				break;
			}
			double durationInMinutesOfScorePeriod = ((endOfScorePeriod.atZone(Constants.ZONE_ID).toEpochSecond() -
					startOfScorePeriod.atZone(Constants.ZONE_ID).toEpochSecond())/60.);
			if (durationInMinutesOfScorePeriod > 0){
				quantity += durationInMinutesOfScorePeriod;
				score += TPUtil.calcAccumulatedScoreFromToTime(chunk, startOfScorePeriod, (long)durationInMinutesOfScorePeriod);
			}
			//for next loop
			startOfLookBehind = endOfScorePeriod.plusMinutes(1);
			startOfScorePeriod = startOfLookBehind.plusHours(hours);
		}

		String name = "%"+Long.toString(portionSize)+"PortionsAndLess"+"%";

		TagPoint tp = tagPoints.get(name);
		TagPoint portionTP = null;
		if (tp == null){
			portionTP = new TagPoint(name, quantity, score);

		}
		else{
			double q = tp.getQuantity();
			double s = tp.getOrig_tot_points();
			portionTP = new TagPoint(name, quantity+q, score+s);

		}
		tagPoints.put(name, portionTP);
	}
	//problem not solved: ska vara möjligt att returnera end time of chunk
	private static LocalDateTime getEndOfScorePeriod(List<Meal> meals,
			LocalDateTime startOfLookBehind, LocalDateTime startOfScorePeriod, LocalDateTime endOfChunk, long portionSize) {
		if (meals.isEmpty()){
			return null;
		}
		if (startOfScorePeriod.isAfter(endOfChunk)){
			return null;
		}
		LocalDateTime timeLastOkMeal = endOfChunk;
		for (Meal m: meals){
			if (m.getPortions()>portionSize){
				timeLastOkMeal = m.getTime();
				break;
			}
		}
		return timeLastOkMeal;
	}

	public static void addPortionScore(List<Chunk> chunks,
			Map<String, TagPoint> tagPoints, long hours, long size) throws Exception {
		for (Chunk chunk : chunks) {
			addPortionScore(chunk, tagPoints, hours, size);
		}

	}

}
