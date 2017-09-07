package com.ibsanalyzer.model;

import org.threeten.bp.LocalDateTime;

//write for example spinach@green_leaves => 2 tags: spinach &
//green_leaves
public class TagModel {
    protected LocalDateTime time;
    private int _id;
    private TagTemplate tagTemplate; //fk Hur gör jag?
    private double size;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public TagTemplate getTagTemplate() {
        return tagTemplate;
    }

    public void setTagTemplate(TagTemplate tagTemplate) {
        this.tagTemplate = tagTemplate;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}