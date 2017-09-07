package com.ibsanalyzer.model;

import java.io.Serializable;

/**
 * Created by Johan on 2017-05-06.
 */

public class TagTemplate implements Serializable {
    String _tagname;
    TagTemplate _is_a1 = null; //points to other TagTemplate it inherits, for example, Pasta
    // points to wheat. Can be null.
    TagTemplate _is_a2 = null;
    TagTemplate _is_a3 = null;

    public TagTemplate() {

    }

    public TagTemplate(String _tagname, TagTemplate _is_a1, TagTemplate _is_a2, TagTemplate
            _is_a3) {
        this._tagname = _tagname;
        this._is_a1 = _is_a1;
        this._is_a2 = _is_a2;
        this._is_a3 = _is_a3;
    }

    public TagTemplate(String _tagname) {
        this._tagname = _tagname;
    }

    //getters and setters
    public String get_tagname() {
        return _tagname;
    }

    public void set_tagname(String _tagname) {
        this._tagname = _tagname;
    }

    public TagTemplate get_is_a1() {
        return _is_a1;
    }

    public void set_is_a1(TagTemplate _is_a1) {
        this._is_a1 = _is_a1;
    }

    public TagTemplate get_is_a2() {
        return _is_a2;
    }

    public void set_is_a2(TagTemplate _is_a2) {
        this._is_a2 = _is_a2;
    }

    public TagTemplate get_is_a3() {
        return _is_a3;
    }

    public void set_is_a3(TagTemplate _is_a3) {
        this._is_a3 = _is_a3;
    }
}
