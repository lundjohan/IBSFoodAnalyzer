package com.johanlund.model;

import java.io.Serializable;

/**
 * Created by Johan on 2017-05-06.
 */

public class TagType implements Serializable {
    String _tagname;
    TagType _is_a1 = null; //points to other TagType it inherits, for example, Pasta
    // points to wheat. Can be null.

    public TagType() {

    }

    public TagType(String _tagname, TagType _is_a1) {
        this._tagname = _tagname;
        this._is_a1 = _is_a1;
    }

    public TagType(String _tagname) {
        this._tagname = _tagname;
    }

    //getters and setters
    public String get_tagname() {
        return _tagname;
    }

    public void set_tagname(String _tagname) {
        this._tagname = _tagname;
    }

    public TagType get_type_of() {
        return _is_a1;
    }

    public void set_is_a1(TagType _is_a1) {
        this._is_a1 = _is_a1;
    }
}
