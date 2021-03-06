package com.johanlund.stat_backend.stat_util;

import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Meal;
import com.johanlund.base_classes.Rating;
import com.johanlund.base_classes.Tag;
import com.johanlund.util.Util;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

//class solely in use for legacy reasons with TagPoints.

public class Chunk {
    List<Event> events;

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
            //remove break < event, see ChunkTests when this can occur.
            for (int j = indBreaks; j < breaks.size(); j++) {
                if (breaks.get(indBreaks).getTime().isBefore(events.get(i).getTime())) {
                    indBreaks++;
                }
                break;
            }

            //same as:  last break || last event
            if (breaks.size() <= indBreaks || i == events.size() - 1) {
                toReturn.add(new Chunk(events.subList(indStartNewChunk, events.size())));
                break;
            }

            LocalDateTime bTime = breaks.get(indBreaks).getTime();
            LocalDateTime eTime = events.get(i).getTime();
            LocalDateTime nextETime = events.get(i + 1).getTime(); //this is ok due to former if
            //same as: e <= b < nextE
            if (!bTime.isBefore(eTime) && bTime.isBefore(nextETime)) {
                toReturn.add(new Chunk(events.subList(indStartNewChunk, i + 1)));
                indBreaks++;
                indStartNewChunk = i + 1;
            }
        }
        return toReturn;
    }

    //incl, incl
    public static List<ScoreTime> getBMsBetweenTimes(List<ScoreTime> bms, LocalDateTime
            searchForBMStartTime, LocalDateTime
            searchForBMStopTime) {

        List<ScoreTime> filteredBms = new ArrayList<>();
        for (ScoreTime bm : bms) {
            if (!bm.getTime().isBefore(searchForBMStartTime) && !bm.getTime().isAfter
                    (searchForBMStopTime)) {
                filteredBms.add(bm);
            }
        }
        return filteredBms;
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
}
