package com.ibsanalyzer.base_classes;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;;
import java.util.List;

public class Meal extends Event {
	private double portions;

	public Meal(LocalDateTime time, List<Tag> tags, double portions) {
		super(time, tags);
		this.portions = portions;
	}

	public double getPortions() {
		return portions;
	}


	//parceable methods
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeDouble(portions);

	}
	@Override
	public void readFromParcel(Parcel in) {
		portions = in.readDouble();
	}
	private Meal(Parcel in) {
		super(in);
		readFromParcel(in);
	}
	public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
		@Override
		public Meal createFromParcel(Parcel in) {
			return new Meal(in);
		}

		@Override
		public Meal[] newArray(int size) {
			return new Meal[size];
		}
	};
}