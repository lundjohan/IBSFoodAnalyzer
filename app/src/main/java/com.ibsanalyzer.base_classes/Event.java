package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;

;

//baseclass only exists to avoid duplication of code.
//implemts serializable so it can be passed in putExtra to Fragments
public abstract class Event implements Comparable<Event>, Serializable {
	protected LocalDateTime time;


	public LocalDateTime getTime() {
		return time;
	}




	public Event(LocalDateTime time) {
		this.time = time;
	}

	public int compareTo(Event event2) {
		return this.time.compareTo(event2.time);
	}

	@Override
	public boolean equals(Object b){
		Event event2 = (Event)b;
		if (this.getClass() == b.getClass() && this.getTime().isEqual(event2.getTime())){
			return true;
		}
		return false;
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