package com.ibsanalyzer.base_classes;

import com.ibsanalyzer.pseudo_event.DateMarkerEvent;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.io.Serializable;


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

    //compares localdatetime + DateMarkerEvent (should come last in case of same time)
    public int compareTo(Event event2) {
        int toReturn = this.time.compareTo(event2.time);
        if (toReturn == 0) {
            if (this instanceof DateMarkerEvent) {
                toReturn = 1; //positive
            } else if (event2 instanceof DateMarkerEvent) {
                toReturn = -1; //negative
            }
        }
        return toReturn;
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