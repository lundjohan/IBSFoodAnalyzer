package com.ibsanalyzer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.Collection;
import java.util.List;



public abstract class EventModel {
	protected LocalDateTime time;
	protected List<Tag> tags;

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
}