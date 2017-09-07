package com.ibsanalyzer.util;


import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Rating;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class IBSUtil {

    /**
     * Precondition 1: there is at least one divider in chunk. Precondition 2:
     * last divider in chunk is valid.
     *
     * @param ch
     * @return
     */
    public static double retrieveLastScore(Chunk ch) {
        List<Rating> divs = ch.getDivs();
        Rating lastDiv = divs.get(divs.size() - 1);
        return lastDiv.getAfter();
    }

    /**
     * Not unefficient method but ugly, clean up if time is!
     *
     * @param ch
     * @param divs must not be empty
     */
    public static void addLastDiv(Chunk ch, List<Rating> divs) {
        if (divs.isEmpty()) {
            throw new IllegalArgumentException("divs must not be empty");
        }
        if (ch.getEvents().size() == 0) {
            return;
        }


        LocalDateTime timeLastEvent = ch.getLastTime();

        Rating earlierLastDiv = divs.get(divs.size() - 1);
        if (earlierLastDiv.getTime().isAfter(timeLastEvent))
            return;
        //get last score/after
        int lastAfter = divs.get(divs.size() - 1).getAfter();
        divs.add(new Rating(timeLastEvent, lastAfter));
    }

    /**
     * @param events must be in time order!
     * @param tp
     * @return
     */
    public static List<Event> trimEventsToPeriod(List<Event> events, TimePeriod tp) {
        List<Event> toReturn = new ArrayList<>();
        for (Event e : events) {
            if (e.getTime().isBefore(tp.getStart())) {
                continue;
            } else if (e.getTime().isAfter(tp.getEnd())) {
                break;
            }
            toReturn.add(e);
        }
        return toReturn;
    }
}
