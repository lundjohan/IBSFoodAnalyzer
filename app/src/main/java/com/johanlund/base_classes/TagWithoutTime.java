package com.johanlund.base_classes;

import java.io.Serializable;

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
    public void setSize(double d){
        size = d;
    }
}
