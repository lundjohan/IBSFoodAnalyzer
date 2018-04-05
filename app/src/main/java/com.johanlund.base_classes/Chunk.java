package com.johanlund.base_classes;

import com.johanlund.util.IBSUtil;
import com.johanlund.util.TimePeriod;
import com.johanlund.util.Util;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

//class solely in use for legacy reasons with TagPoints.

public class Chunk {
    List<Event> events = new ArrayList<>();

    private Chunk(List<Event> events) {
        this.events = events;
    }

    /**
     * NB: first and last breaks are never before or after first/last event
     *
     * sorry for the mess, this is way too complicated. But it is tested.
     *
     * @param events should be in chronological order
     * @param breaks should be in chronological order
     * @return
     */
    public static List<Chunk> makeChunksFromEvents(List<Event> events, List<Break> breaks) {
        List<Chunk> chunks = new ArrayList<>();
        if (breaks.isEmpty() && !events.isEmpty()) {
            chunks.add(new Chunk(events));
            return chunks;
        }
        LocalDateTime leftBreak = LocalDateTime.MIN;
        for (int i = 0; i < breaks.size(); i++) {
            //start or middle of breaks list
            Chunk firstOrMiddleCh = makeChunk(events, leftBreak, breaks.get(i).getTime());
            if (firstOrMiddleCh != null) {
                chunks.add(firstOrMiddleCh);
            }
            //at end break.
            if (i == breaks.size() - 1) {
                //end, notice that this can happen at same time as start
                    Chunk lastCh = makeChunk(events, breaks.get(i).getTime(), LocalDateTime.MAX);
                if (lastCh != null) {
                    chunks.add(lastCh);
                }
            }
            leftBreak = breaks.get(i).getTime();
        }
        return chunks;
    }

    /**
     * Helper method for chopping a list of events with breaks
     * the event that is at the same time as break goes to the chunk before
     * => fromExcl, toIncl
     *
     * @param events
     * @param fromExcl
     * @param toIncl must be after or at same time as fromExcl
     * @return can return null value
     */
    private static Chunk makeChunk(List<Event> events, LocalDateTime fromExcl, LocalDateTime
            toIncl) {
        int startIndEvents = 0;
        int endIndEvents = events.size()-1;

        //find startIndEvents
        for (int i= 0;i<events.size();i++) {
            LocalDateTime timeOfEvent = events.get(i).getTime();
            if (timeOfEvent.isAfter(fromExcl)) {
                startIndEvents = i;
                break;
            }
        }
        //find endIndEvents
        for (int i= events.size()-1;i>=0;i--) {
            LocalDateTime timeOfEvent = events.get(i).getTime();
            if (!timeOfEvent.isAfter(toIncl)) {
                endIndEvents = i;
                break;
            }
        }
        //remember sublist is exclusive second parameter, therefore +1
        return new Chunk (events.subList(startIndEvents, endIndEvents +1));
    }


    public List<Event> getEvents() {
        return events;
    }

    public List<Rating> getRatings() {
        List<Rating> ratings = new ArrayList<>();
        for (Event e : events) {
            if (e instanceof Rating) {
                ratings.add((Rating) e);
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
     *
     * Obsolete
     */
    public List<Tag> getTags(int hours) {
        List<Tag> tags = new ArrayList<>();
        if (events.isEmpty()) {
            return tags;
        }
        LocalDateTime lastTime = events.get(events.size() - 1).getTime();
        LocalDateTime lastValidTime = lastTime.minusHours(hours);

        //loop backwards
        List<Event> remainingEvents = new ArrayList<>();
        for (int i = events.size() - 1; i >= 0; i--) {
            //if event is before or equal to time limit
            if (!events.get(i).getTime().isAfter(lastValidTime)) {
                remainingEvents = events.subList(0, i + 1); //+1 since 2nd parameter in subList
                // is excl
                break;
            }
        }
        return Util.getTags(remainingEvents);
    }

    /**
     * Get average score from . Based on time
     * passed after each div in time frame.
     * <p>
     * Given: these times should be within time of this chunk.
     *
     * Returns -1.0 if no appropriate rating events are found in chunk
     */
    public double calcAvgScoreFromToTime(LocalDateTime tagTime, long minutesAheadStart, long minutesAheadStop) {
        LocalDateTime startTime = tagTime.plusMinutes(minutesAheadStart);
        LocalDateTime endTime = tagTime.plusMinutes(minutesAheadStop);
        List<Rating> divs = getDivsBetweenAndSometimesOneBefore(startTime, minutesAheadStop); //ok!
        if (divs.isEmpty()){
            return -1.0;
        }

        if (divs.size() == 1) {
            return divs.get(0).getAfter();
        }
        //time of div before <startTime> (the first div to take into account) is not interesting (it
        // can have happened many days before), only its score.
        long startLong = startTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        double scoreMultWithTime = 0;
        for (int i = 1; i < divs.size(); i++) {
            LocalDateTime t = divs.get(i).getTime();
            double timeDifInSec = t.atZone(ZoneId.systemDefault()).toEpochSecond() - startLong;
            scoreMultWithTime += divs.get(i - 1).getAfter() * timeDifInSec;
            startLong = divs.get(i).getTime().atZone(ZoneId.systemDefault()).toEpochSecond();
        }
        //the last one
        long toLong = endTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        double lastTimeDif = toLong - startLong;
        scoreMultWithTime += divs.get(divs.size() - 1).getAfter() * lastTimeDif;

        double avgScore = scoreMultWithTime / ((minutesAheadStop - minutesAheadStart) * 60);
        return avgScore;
    }

    /**
     *
     * @param
     * @param minutesAheadStop
     * @return [factor, Score]. Explanation of factor: if 10 hours is needed ahead to see tag score,
     * and only 5 h exists, then the factor is 0.5  (the score is dragged out to the double
     * (double as in "twice") it's time.)
     */
    public double [] calcAvgScoreForOverridingTag(LocalDateTime tagTime, long minutesAheadStart, long minutesAheadStop){
        LocalDateTime preferredLastTimeOfChunk = tagTime.plusMinutes(minutesAheadStop);
        LocalDateTime deFactoLastTimeOfChunk = getLastTime();
        long secondsDiff = preferredLastTimeOfChunk.atZone(ZoneId.systemDefault()).toEpochSecond()-deFactoLastTimeOfChunk.atZone(ZoneId.systemDefault()).toEpochSecond();
        double factor = ((double)secondsDiff) /(minutesAheadStop*60);

        //calculate score
        long minutesToEndOfChunk = secondsDiff/60;
        double score = calcAvgScoreFromToTime(tagTime, minutesAheadStart, minutesToEndOfChunk);
        if (score == -1.0){
            return null;
        }
        return new double[]{factor, score};
    }


    /**
     * @return If there is no div on same time as from, then an earlier div is returned as well.
     *
     * Given Chunk must at least have one div, and that one should be at start
     */
    public List<Rating> getDivsBetweenAndSometimesOneBefore(LocalDateTime from, long minutesAhead) {
        return getDivsBetweenAndSometimesOneBefore(from, from.plusMinutes(minutesAhead));
    }
    /**current problem if only one div from getRatings, it should still return one div but it returns zero.
     * If there are no divs in chunk, an empty List is returned.
     */
    public List<Rating> getDivsBetweenAndSometimesOneBefore(LocalDateTime from, LocalDateTime to) {
        //get firstInd
        int firstInd = 0;
        List<Rating> divs = getRatings();
        if (divs.isEmpty()){
            return divs;
        }
        for (int i = 0; i < divs.size(); i++) {
            LocalDateTime divTime = divs.get(i).getTime();
            if (divTime.isBefore(from) || divTime.isEqual(from)) {
                firstInd = i;
            } else {
                break;
            }
        }

        //get LastInd
        int lastInd = firstInd;
        for (int i = firstInd+1; i < divs.size(); i++) {
            LocalDateTime divTime = divs.get(i).getTime();
            if (divTime.isAfter(to)) {
                break;
            } else {
                lastInd = i;
            }
        }
        //sublist(incl, excl (therefore +1))
        return divs.subList(firstInd, lastInd+1);
    }
    //incl, incl
    public List<Bm> getBMsBetweenTimes(LocalDateTime searchForBMStartTime, LocalDateTime
            searchForBMStopTime) {

        List<Bm> filteredBms = new ArrayList<>();
        List<Bm> bms = getBMs();
        for (Bm bm : bms) {
            if (!bm.getTime().isBefore(searchForBMStartTime) && !bm.getTime().isAfter(searchForBMStopTime)) {
                filteredBms.add(bm);
            }
        }
        return filteredBms;
        /*return getBMs().stream().
                filter(bm-> bm.getTime().isAfter(time) && bm.getTime().isBefore(time.plusHours
				(hoursAhead))).
				collect(Collectors.toList());*/
    }

    public List<Meal> getMeals() {
        List<Meal> meals = new ArrayList<>();
        for (Event e : events) {
            if (e instanceof Meal) {
                meals.add((Meal) e);
            }
        }
        return meals;
    }

    /**
     * Prerequisite events>0
     *
     * @return
     */
    public LocalDateTime getLastTime() {
        Event e = events.get(events.size() - 1);
        return e.getTime();
    }

    /**
     * Prerequisite events>0
     *
     * @return
     */
    public LocalDateTime getStartTime() {
        return events.get(0).getTime();
    }

    public List<Tag> getTagsForPeriod(TimePeriod tp) {
        List<Event> trimmedEvents = IBSUtil.trimEventsToPeriod(getEvents(), tp);
        return Util.getTags(trimmedEvents);
    }

    public boolean timeOverridesChunkEnd(LocalDateTime preferredStopTime) {
        return preferredStopTime.isAfter(getLastTime());
    }


}
