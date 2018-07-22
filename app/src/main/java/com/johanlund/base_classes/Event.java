package com.johanlund.base_classes;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.io.Serializable;

import static com.johanlund.constants.Constants.BM;
import static com.johanlund.constants.Constants.EXERCISE;
import static com.johanlund.constants.Constants.MEAL;
import static com.johanlund.constants.Constants.OTHER;
import static com.johanlund.constants.Constants.RATING;


//baseclass only exists to avoid duplication of code.
//implemts serializable so it can be passed in putExtra to Fragments
public abstract class Event implements Comparable<Event>, Serializable {
    protected LocalDateTime time;
    protected boolean hasBreak = false;
    protected String comment;

    public Event(LocalDateTime time) {
        this.time = time;
        comment = "";
    }

    public Event(LocalDateTime time, String comment) {
        this.time = time;
        this.comment = comment;
    }

    public Event(LocalDateTime time, String comment, boolean hasBreak) {
        this.time = time;
        this.comment = comment;
        this.hasBreak = hasBreak;
    }

    /**
     * @param eventType
     * @return null if eventype is not a valid one
     */
    public static String getEventTypeStr(int eventType) {
        String str = null;
        switch (eventType) {
            case MEAL: {
                str = "Meal";
                break;
            }
            case OTHER: {
                str = "Other";
                break;
            }
            case EXERCISE: {
                str = "Exercise";
                break;
            }
            case BM: {
                str = "Bm";
                break;
            }
            case RATING: {
                str = "Rating";
                break;
            }
        }
        return str;
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
        return hasBreak;
    }

    public void setHasBreak(boolean b) {
        hasBreak = b;
    }

    /**
     * This method only changes the date of the event, not the time
     *
     * @param ld
     */
    public void changeDateKeepTime(LocalDate ld) {
        time = LocalDateTime.of(ld, time.toLocalTime());
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public abstract int getType();

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