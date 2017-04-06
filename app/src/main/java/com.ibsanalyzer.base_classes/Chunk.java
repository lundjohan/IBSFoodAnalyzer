package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors; Fix streams for Android!

public class Chunk {
	List<Day> days = new ArrayList<>();
	public Chunk(List<Day> days) {
		this.days = days;
	}

	public List<Divider> getDivs() {
		List<Divider> divs = new ArrayList<>();
		for (Day day : days) {
			divs.addAll(day.getDividers());
		}
		return divs;
	}

	public List<BM> getBMs() {
		List<BM> bms = new ArrayList<>();
		for (Day d : days) {
			bms.addAll(d.getBMs());
		}
		return bms;
	}
	public List<Tag> getTags() {
		List<Tag> tags = new ArrayList<>();
		for (Day day:this.days) {
			tags.addAll(day.getInputTags());
		}
		return tags;
	}

	/**
	 * @return tags from start time of chunk to (endtime of chunk - hours)
	 */
	public List<Tag> getTags(int hours) {
		List<Tag> tags = new ArrayList<>();

		int hoursToCut = hours == 0?0:hours % 24;
		int daysToCut = hours == 0?0:hours / 24;
		daysToCut += hoursToCut>0?1:0;

		if (days.size() <= daysToCut) {
			return tags;
		}

		for (int i = 0; i < days.size() - daysToCut; i++) {
			tags.addAll(days.get(i).getInputTags());
		}
		if (hoursToCut > 0) {
			Day dayToGetLastTagsFrom = days.get((days.size() - 1) - daysToCut);
			tags.addAll(dayToGetLastTagsFrom.getTags(hoursToCut));
		}

		return tags;
	}

	//copy-pastat från ovan, inte så snyggt
	public List<Tag> getTagsTruncatedEnds(int hours){
		List<Tag> tags = new ArrayList<>();

		int hoursToCutAtEnd = hours == 0?0:hours % 24;
		int daysToCutAtEnd = hours == 0?0:hours / 24;
		daysToCutAtEnd += hoursToCutAtEnd>0?1:0;

		if (days.size() <= daysToCutAtEnd*2) {
			return tags;
		}

		//add hours in front
		if (hoursToCutAtEnd > 0) {
			Day dayToGetFirstTagsFrom = days.get(daysToCutAtEnd);
			tags.addAll(dayToGetFirstTagsFrom.getTags(-hoursToCutAtEnd));
		}
		//add days in middle
		for (int i = daysToCutAtEnd; i < days.size() - daysToCutAtEnd; i++) {
			tags.addAll(days.get(i).getInputTags());
		}

		//add hours in end
		if (hoursToCutAtEnd > 0) {
			Day dayToGetLastTagsFrom = days.get(days.size() -1 -daysToCutAtEnd);
			tags.addAll(dayToGetLastTagsFrom.getTags(hoursToCutAtEnd));
		}

		return tags;
	}

	/**
	 * Get average score from . Based on time
	 * passed after each div in time frame.
	 *
	 * Given: these times should be within time of this chunk.
	 */
	public double calcAvgScoreFromToTime(LocalDateTime from, long hoursAhead) {
		ZoneId zoneId = ZoneId.systemDefault();
		List<Divider> divs = getDivsBetweenAndSometimesOneBefore(from, hoursAhead); //ok!
		if (divs.size() == 1) {
			return divs.get(0).getAfter();
		}
		//time of div before <from> (the first div to take into account) is not interesting (it can have happened many days before), only its score.
		long startLong = from.atZone(zoneId).toEpochSecond();
		double scoreMultWithTime = 0;
		for (int i = 1; i < divs.size(); i++) {
			LocalDateTime t = divs.get(i).getTime();
			double timeDifInSec = t.atZone(zoneId).toEpochSecond() - startLong;
			scoreMultWithTime += divs.get(i - 1).getAfter() * timeDifInSec;
			startLong = divs.get(i).getTime().atZone(zoneId).toEpochSecond();
		}
		//the last one
		long toLong = from.plusHours(hoursAhead).atZone(zoneId).toEpochSecond();
		double lastTimeDif = toLong - startLong;
		scoreMultWithTime += divs.get(divs.size() - 1).getAfter() * lastTimeDif;

		double avgScore = scoreMultWithTime / (hoursAhead*3600);
		return avgScore;
	}
	/**
	 *
	 * @return If there is no div on same time as from, then an earlier div is returned as well.
	 */
	public List<Divider> getDivsBetweenAndSometimesOneBefore(LocalDateTime from, long hoursAhead) {
		return getDivsBetweenAndSometimesOneBefore(from, from.plusHours(hoursAhead));
	}
	public List<Divider> getDivsBetweenAndSometimesOneBefore(LocalDateTime from, LocalDateTime to) {
		//get firstInd
		int firstInd= 0;
		List<Divider> divs = getDivs();
		for (int i = 0; i < divs.size(); i++) {
			LocalDateTime divTime = divs.get(i).getTime();
			if (divTime.isBefore(from) || divTime.isEqual(from)){
				firstInd = i;
			}
			else{
				break;
			}
		}

		//get LastInd
		int lastInd = divs.size()-1;
		for (int i = divs.size()-1; i > 0; i--) {
			LocalDateTime divTime = divs.get(i).getTime();
			if (divTime.isAfter(to) || divTime.isEqual(to)){
				lastInd = i;
			}
			else{
				break;
			}
		}
		return divs.subList(firstInd, lastInd);
	}

	public List<BM> getBMsAfterTime(Chunk chunk, LocalDateTime time,
			long hoursAhead) {
		return null; //change to under, when stream is ok
        /*return getBMs().stream().
				filter(bm-> bm.getTime().isAfter(time) && bm.getTime().isBefore(time.plusHours(hoursAhead))).
				collect(Collectors.toList());*/
	}

	public List<Meal> getMeals() {
		List<Meal>meals = new ArrayList<>();
        //uncomment below, when stream is ok
	//	days.forEach(d -> meals.addAll(d.getMeals()));
		return meals;
	}

	public List<Day> getDays() {
		return days;
	}

}
