package com.johanlund.base_classes;

import com.johanlund.statistics_portions.PortionTime;
import com.johanlund.util.IBSUtil;
import com.johanlund.util.TimePeriod;
import com.johanlund.util.Util;

import org.threeten.bp.LocalDateTime;

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
     * @return chunks in chronological order
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

    public List<PortionTime> getPortionTimes() {
        List<PortionTime> pts = new ArrayList<>();
        for (Event e : events) {
            if (e instanceof Meal) {
                Meal m = (Meal) e;
                PortionTime pt = new PortionTime(m.getPortions(), m.getTime());
                pts.add(pt);
            }
        }
        return pts;
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
}
