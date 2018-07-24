package com.johanlund.base_classes;

import org.threeten.bp.LocalDateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TagWithoutTime implements Serializable {
    private String name;
    private double size;

    public TagWithoutTime(String name, double size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double d) {
        size = d;
    }

    public static Tag toTagWithTime(TagWithoutTime withoutTime, LocalDateTime ldt) {
        return new Tag(ldt, withoutTime.getName(),withoutTime.getSize());
    }

    public static List<Tag> toTagsWithTime(List<TagWithoutTime>withoutTimeList, LocalDateTime ldt) {
        List<Tag> toReturn =new ArrayList<>();
        for (TagWithoutTime without: withoutTimeList){
            toReturn.add(toTagWithTime(without,ldt));
        }
        return toReturn;
    }
}
