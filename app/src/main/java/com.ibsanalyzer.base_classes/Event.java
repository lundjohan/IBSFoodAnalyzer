package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//baseclass only exists to avoid duplication of code.
//implemts serializable so it can be passed in putExtra to Fragments
public abstract class Event implements Comparable<Event>, Serializable {
    protected LocalDateTime time;
    protected boolean isBreak;

    public Event(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    //compares localdatetime of events
    public int compareTo(Event event2) {
        return this.time.compareTo(event2.time);
    }

    @Override
    public boolean equals(Object b) {
        Event event2 = (Event) b;
        if (this.getClass() == b.getClass() && this.getTime().isEqual(event2.getTime())) {
            return true;
        }
        return false;
    }

    public boolean hasBreak() {
        return isBreak;
    }

    public void setBreak(boolean b) {
        isBreak = b;
    }

    /**
     * This method only changes the date of the event, not the time
     * @param ld
     */
    public void setDate(LocalDate ld){
       time =  LocalDateTime.of(ld, time.toLocalTime());
    }

    /**
     * If difference in time between consecutive events are larger (equal not enough) than hoursAheadBreak => add a Break
     * @param events
     * @param hoursAheadBreak
     * @return
     */

    public static List<Break> makeBreaks(List<Event> events, int hoursAheadBreak) {
        List<Break>breaks = new ArrayList<>();
        for (int i = 0; i<events.size()-1; i++){
            if (events.get(i).getTime().plusHours(hoursAheadBreak).isBefore(events.get(i+1).getTime())){
                breaks.add(new Break(events.get(i).getTime()));
            }
        }
        return breaks;

    }

    //classes for parceable, most of them are implemented higher up in hierarchy.
    /*public Event(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }
	public void readFromParcel(Parcel in) {
        time = LocalDateTime.ofEpochSecond(in.readLong(), 0, ZoneOffset.UTC);
        in.readTypedList(tags, Tag.CREATOR);
	}
	public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time.toEpochSecond(ZoneOffset.UTC));
        dest.writeTypedList(tags);
	}*/

}