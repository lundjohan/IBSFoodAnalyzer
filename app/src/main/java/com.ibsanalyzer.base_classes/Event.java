package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;

;

//baseclass only exists to avoid duplication of code.
//implemts serializable so it can be passed in putExtra to Fragments
public abstract class Event implements Serializable {
	protected LocalDateTime time;


	public LocalDateTime getTime() {
		return time;
	}




	public Event(LocalDateTime time) {
		this.time = time;
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