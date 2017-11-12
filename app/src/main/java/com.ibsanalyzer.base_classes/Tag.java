package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;

//write for example spinach@green_leaves => 2 tags: spinach &
//green_leaves
public class Tag implements Serializable {
    protected LocalDateTime time;
    private String name;
    private double size;


    public Tag(String name) {
        this.name = name;
        this.size = 1;    //default 1

    }

    public Tag(LocalDateTime time, String name, double size) {
        this.name = name;
        this.size = size;
        this.time = time;
    }
/*
    protected Tag(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
      @Override
    public int describeContents() {
        return 0;
    }
    public void readFromParcel(Parcel in) {
        time = LocalDateTime.ofEpochSecond(in.readLong(), 0, ZoneOffset.UTC);
        name = in.readString();
        size = in.readDouble();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time.toEpochSecond(ZoneOffset.UTC));
        dest.writeString(name);
        dest.writeDouble(size);

    }
    */

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;

    }
    public void setSize(double d) {
        size = d;

    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getSize() {
        return size;
    }


}