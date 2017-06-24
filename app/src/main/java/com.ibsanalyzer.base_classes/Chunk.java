package com.ibsanalyzer.base_classes;

import com.ibsanalyzer.util.Util;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;
//class solely in use for legacy reasons with TagPoints.

public class Chunk {
	List<Event> events = new ArrayList<>();

	public Chunk(List<Event> events) {
        this.events = events;
	}

	public List<Rating> getDivs() {
		List<Rating> ratings = new ArrayList<>();
		for (Event e : events) {
			if (e instanceof Rating) {
                ratings.add((Rating)e);
            }
		}
		return ratings;
	}

	public List<Bm> getBMs() {
		List<Bm> bms = new ArrayList<>();
        for (Event e : events) {
            if (e instanceof Bm) {
                bms.add((Bm) e);
            }
        }
		return bms;
	}
	//tags exist in Meal, Other and Exercise events.
	public List<Tag> getTags() {
		return Util.getTags(events);
	}

	/**
	 * @return tags from start time of chunk to (endtime of chunk - hours)
     * Preequisite: events are sorted by time (latest last)
	 */
	public List<Tag> getTags(int hours) {
		List<Tag> tags = new ArrayList<>();
        if (events.isEmpty()){
            return tags;
        }
        LocalDateTime lastTime = events.get(events.size()-1).getTime();
        LocalDateTime lastValidTime = lastTime.minusHours(hours);

        //loop backwards
        List<Event>remainingEvents = new ArrayList<>();
        for (int i=events.size()-1;i>=0;i--){
            //if event is before or equal to time limit
            if (!events.get(i).getTime().isAfter(lastValidTime)){
                remainingEvents = events.subList(0, i+1); //+1 since 2nd parameter in subList is excl
                break;
            }
        }
		return Util.getTags(remainingEvents);
	}

	/**
	 * Get average score from . Based on time
	 * passed after each div in time frame.
	 *
	 * Given: these times should be within time of this chunk.
	 */
	public double calcAvgScoreFromToTime(LocalDateTime from, long hoursAhead) {
		ZoneId zoneId = ZoneId.systemDefault();
		List<Rating> divs = getDivsBetweenAndSometimesOneBefore(from, hoursAhead); //ok!
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
	public List<Rating> getDivsBetweenAndSometimesOneBefore(LocalDateTime from, long hoursAhead) {
		return getDivsBetweenAndSometimesOneBefore(from, from.plusHours(hoursAhead));
	}
	public List<Rating> getDivsBetweenAndSometimesOneBefore(LocalDateTime from, LocalDateTime to) {
		//get firstInd
		int firstInd= 0;
		List<Rating> divs = getDivs();
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

	public List<Bm> getBMsAfterTime(Chunk chunk, LocalDateTime time,
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

	public static List<Chunk> makeChunksFromEvents(List<Event>events){
		List<Chunk>chunks = new ArrayList<>();
		int lastStartIndex = 0;
		for (int i = 0;i<events.size(); i++){
			if (events.get(i).hasBreak()){
				Chunk ch = new Chunk(events.subList(lastStartIndex,i ));
				chunks.add(ch);
			}
		}
		return chunks;
	}

}
