package com.ibsanalyzer.base_classes;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;;
import java.util.Collection;
import java.util.List;

//baseclass only exists to avoid duplication of code.
public abstract class Event implements Parcelable {
	protected LocalDateTime time;
	protected List<Tag> tags;

	public LocalDateTime getTime() {
		return time;
	}

	public List<Tag> getTags() {
		return tags;
	}
	/**
	 * Compares string with name of tag.
	 * @return Tag with same name as string, or null if it doesn't exist.
	 */
	public Tag getTag(String string) {
		Tag t = null;
		for (Tag tag: tags){
			if (tag.getName().equals(string)){
				t = tag;
				break;
			}
		}
		return t;
	}


	public Event(LocalDateTime time, List<Tag> tags) {
		this.time = time;
		this.tags = tags;
	}

	/**Some Event classes will override this one (read BM), therefore it exist.
	 *
	 * @return
	 */
	public Collection<? extends Tag> getInputTags() {
		return getTags();
	}
	@Override
	public String toString(){
		return time.toString();

	}

    public Event(Parcel in){
        readFromParcel(in);
    }
	//classes for parceable, most of them are implemented higher up in hierarchy.
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
	}

}