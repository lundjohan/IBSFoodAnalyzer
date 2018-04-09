package com.johanlund.base_classes;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Johan on 2017-10-05.
 */

public class Break implements Comparable<Break>{
    LocalDateTime time;

    public Break(LocalDateTime t){
        time = t;
    }

    public LocalDateTime getTime(){
        return time;
    }

    /**
     * Do both manual and automatic breaks
     * @param events
     * @param hoursInFrontOfAutoBreak
     * @return sorted list in asc datetime order
     */
    public static List<Break> makeAllBreaks(List<Event> events, int hoursInFrontOfAutoBreak) {
        List<Break> toReturn = makeAutoBreaks(events, hoursInFrontOfAutoBreak);
        toReturn.addAll(getManualBreaks(events));
        Collections.sort(toReturn);

        return toReturn;
    }
    /**
     * If difference in time between consecutive events are larger (equal not enough) than hoursAheadBreak => add a Break
     * @param events
     * @param hoursAheadBreak
     * @return
     */

    static List<Break> makeAutoBreaks(List<Event> events, int hoursAheadBreak) {
        List<Break>breaks = new ArrayList<>();
        for (int i = 0; i<events.size()-1; i++){
            if (events.get(i).getTime().plusHours(hoursAheadBreak).isBefore(events.get(i+1).getTime())){
                breaks.add(new Break(events.get(i).getTime()));
            }
        }
        return breaks;

    }
    static List<Break> getManualBreaks(List<Event> events) {
        List<Break>toReturn = new ArrayList<>();
        for (Event e: events){
            if (e.hasBreak()){
                toReturn.add(new Break(e.getTime()));
            }
        }
        return toReturn;
    }

    @Override
    public int compareTo(@NonNull Break break2) {
        return this.time.compareTo(break2.time);
    }
}
