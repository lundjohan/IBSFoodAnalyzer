package com.johanlund.base_classes;

import com.johanlund.constants.Constants;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class Meal extends InputEvent {
    private double portions;

    public Meal(LocalDateTime time, List<TagWithoutTime> tags, double portions) {
        super(time, "", tags);
        this.portions = portions;
    }

    public Meal(LocalDateTime time, String comment, List<TagWithoutTime> tags, double portions) {
        super(time, comment, tags);
        this.portions = portions;
    }

    public Meal(LocalDateTime time, String comment, boolean hasBreak, List<TagWithoutTime> tags,
                double portions) {
        super(time, comment, hasBreak, tags);
        this.portions = portions;
    }

    public double getPortions() {
        return portions;
    }

    @Override
    public int getType() {
        return Constants.MEAL;
    }

    //parceable methods
    /*@Override
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
	};*/
}