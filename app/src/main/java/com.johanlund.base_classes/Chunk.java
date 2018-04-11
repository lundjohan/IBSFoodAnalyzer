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
     * @param events should be in chronological order
     * @param breaks should be in chronological order
     * @return chunks in chronological order
     */
    public static List<Chunk> makeChunksFromEvents(List<Event> events, List<Break> breaks) {
        List<Chunk> toReturn = new ArrayList<>();
        int indBreaks = 0;
        int indStartNewChunk = 0; //incl
        for (int i = 0; i < events.size(); i++) {
            if (breaks.size() <= indBreaks || i == events.size() - 1) {
                toReturn.add(new Chunk(events.subList(indStartNewChunk, events.size())));
                break;
            }
            //same as: break <= event
            else if (!breaks.get(indBreaks).getTime().isAfter(events.get(i).getTime())) {
                toReturn.add(new Chunk(events.subList(indStartNewChunk, i)));
                indBreaks++;
                indStartNewChunk = i;
            }
        }
        return toReturn;
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
            if (!bm.getTime().isBefore(searchForBMStartTime) && !bm.getTime().isAfter
                    (searchForBMStopTime)) {
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
