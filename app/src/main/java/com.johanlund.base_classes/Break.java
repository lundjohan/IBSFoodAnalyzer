package com.johanlund.base_classes;

import android.support.annotation.NonNull;

import com.johanlund.util.CompleteTime;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
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

    //make generic (CompleteTime and others should implement some interface)
    public static List<LocalDateTime> makeAllBreaks(List<CompleteTime> cts, List<LocalDateTime> breaks, int
            hoursInFrontOfAutoBreak) {
        breaks.addAll(makeCompleteAutoBreaks(cts, hoursInFrontOfAutoBreak));
        Collections.sort(breaks);
        return breaks;
    }

    //should be generic and be same method as the one below (but better to use this as model,
    // because other statwrappers will use similar - lighter - constructs instead of events)
    private static List<LocalDateTime> makeCompleteAutoBreaks(List<CompleteTime> cts, int hoursAheadBreak) {
        List<LocalDateTime>breaks = new ArrayList<>();
        for (int i = 0; i<cts.size()-1; i++){
            if (cts.get(i).getTime().plusHours(hoursAheadBreak).isBefore(cts.get(i+1).getTime())){
                breaks.add(cts.get(i).getTime());
            }
        }
        return breaks;
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
    /**
     * @param events should be in chronological order
     * @param breaks should be in chronological order
     * @return chunks in chronological order
     */
    //make generic
    //sorry for the names, copied from Chunk.makeChunksFromEvents
    public static List<List<CompleteTime>> divideTimes(List<CompleteTime> events, List<LocalDateTime> breaks) {
        List<List<CompleteTime>> toReturn = new ArrayList<>();
            int indBreaks = 0;
            int indStartNewChunk = 0; //incl
            for (int i = 0; i < events.size(); i++) {
                //remove break < event, see ChunkTests when this can occur.
                for (int j = indBreaks; j<breaks.size();j++) {
                    if (breaks.get(indBreaks).isBefore(events.get(i).getTime())) {
                        indBreaks++;
                    }
                    break;
                }

                //same as:  last break || last event
                if (breaks.size() <= indBreaks || i == events.size() - 1) {
                    toReturn.add(new ArrayList(events.subList(indStartNewChunk, events.size())));
                    break;
                }

                LocalDateTime bTime = breaks.get(indBreaks);
                LocalDateTime eTime = events.get(i).getTime();
                LocalDateTime nextETime = events.get(i + 1).getTime(); //this is ok due to former if
                //same as: e <= b < nextE
                if (!bTime.isBefore(eTime) && bTime.isBefore(nextETime)) {
                    toReturn.add(new ArrayList(events.subList(indStartNewChunk, i + 1)));
                    indBreaks++;
                    indStartNewChunk = i + 1;
                }
            }
            return toReturn;
        }

    @Override
    public int compareTo(@NonNull Break break2) {
        return this.time.compareTo(break2.time);
    }


}
