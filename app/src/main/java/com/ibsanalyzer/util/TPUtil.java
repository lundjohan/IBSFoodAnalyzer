package com.ibsanalyzer.util;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Rating;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.List;

public class TPUtil {
    /*
	 * public static void addTP(Map<String, TagPoint>tagPoints, Tag t){ TagPoint
	 * tp = tagPoints.get(t.getName()); if (tp != null) { tp.addJump(foundJump);
	 * } else{ TagPoint newTp = new TagPoint(t.getName(), t.getSize());
	 * newTp.addJump(foundJump); tagPoints.put(t.getName(), newTp); } }
	 */

    /**
     * Get accumulated score from . Based on time passed after each div in time
     * frame.
     * <p>
     * Calculated in hours
     * <p>
     * Given: these times should be within time of this chunk.
     */
    public static double calcAccumulatedScoreFromToTime(Chunk chunk,
                                                        LocalDateTime from, long minutesAhead) {
        ZoneId zoneId = ZoneId.systemDefault();
        List<Rating> divs = TPUtil.getDivsBetweenAndSometimesOneBefore(chunk,
                from, minutesAhead); // ok!
        if (divs.size() == 1) {
            return divs.get(0).getAfter() * minutesAhead;
        }
        // time of div before <from> (the first div to take into account) is not
        // interesting (it can have happened many days before), only its score.
        long startLong = from.atZone(zoneId).toEpochSecond();
        double scoreMultWithTime = 0;
        for (int i = 1; i < divs.size(); i++) {
            LocalDateTime t = divs.get(i).getTime();
            double timeDifInSec = t.atZone(zoneId).toEpochSecond() - startLong;
            scoreMultWithTime += divs.get(i - 1).getAfter() * timeDifInSec;
            startLong = divs.get(i).getTime().atZone(zoneId).toEpochSecond();
        }
        // the last one
        long toLong = from.plusMinutes(minutesAhead).atZone(zoneId).toEpochSecond();
        double lastTimeDif = toLong - startLong;
        scoreMultWithTime += divs.get(divs.size() - 1).getAfter() * lastTimeDif;

        return scoreMultWithTime / 60;
    }

    /**
     * @return If there is no div on same time as from, then an earlier div is
     * returned as well.
     */
    public static List<Rating> getDivsBetweenAndSometimesOneBefore(Chunk chunk,
                                                                   LocalDateTime from, long
                                                                           minutesAhead) {
        return getDivsBetweenAndSometimesOneBefore(chunk, from,
                from.plusMinutes(minutesAhead));
    }

    private static List<Rating> getDivsBetweenAndSometimesOneBefore(
            Chunk chunk, LocalDateTime from, LocalDateTime to) {
        // get firstInd
        int firstInd = 0;
        List<Rating> divs = chunk.getRatings();
        for (int i = 0; i < divs.size(); i++) {
            LocalDateTime divTime = divs.get(i).getTime();
            if (divTime.isBefore(from) || divTime.isEqual(from)) {
                firstInd = i;
            } else {
                break;
            }
        }

        // get LastInd
        int lastInd = divs.size();
        for (int i = divs.size() - 1; i > 0; i--) {
            LocalDateTime divTime = divs.get(i).getTime();
            if (divTime.isAfter(to) || divTime.isEqual(to)) {
                lastInd = i;
            } else {
                break;
            }
        }
        return divs.subList(firstInd, lastInd);
    }

    /**
     * returns list of meal where leftmost meals time is the same or after
     * parameter leftmosttime
     *
     * @param meals
     * @param leftmostTime
     * @return
     */
    public static List<Meal> sliceLeft(List<Meal> meals,
                                       LocalDateTime leftmostTime) {
        if (meals.isEmpty()) {
            return meals;
        }
        int leftMostInd = meals.size() - 1;
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getTime().isAfter(leftmostTime) || meals.get(i).getTime().isEqual
                    (leftmostTime)) {
                leftMostInd = i;
                break;
            }
        }
        return meals.subList(leftMostInd, meals.size());
    }

    /**
     * Prerequisite: A Rating is first, or shared first, amongst events.
     *
     * @param chunk
     * @return null if chunk contains no days, perhaps should be throwing
     * exception instead
     * @throws Exception
     */
    public static LocalDateTime getStartTime(Chunk chunk) throws Exception {
        List<Event> events = chunk.getEvents();
        if (events.size() == 0) {
            throw new Exception("Cannot get startTime from chunk with no events");
        }
        return events.get(0).getTime();
    }

    /**
     * Returns last time of last event/ tag
     *
     * @param chunk
     * @return
     * @throws Exception
     */
    public static LocalDateTime getEndTime(Chunk chunk) throws Exception {
        List<Event> events = chunk.getEvents();
        if (events.isEmpty()) {
            throw new Exception("Chunk must have more than 0 events to have an End Time");
        }
        return chunk.getEvents().get(events.size() - 1).getTime();
    }
}