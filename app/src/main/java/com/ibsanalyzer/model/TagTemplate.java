package com.ibsanalyzer.model;

import java.io.Serializable;

/**
 * Created by Johan on 2017-05-06.
 */

public class TagTemplate implements Serializable{
    int _id; //using pk with id instead of text since name should be able to be UNICODE.
    String _tagname;
    TagTemplate _is_a1 = null; //points to other TagTemplate it inherits, for example, Pasta points to wheat. Can be null.
    //int quantity; vänta med denna.

    public TagTemplate() {

    }

    public TagTemplate(int _id, String _tagname) {
        this._id = _id;
        this._tagname = _tagname;

    }

    public TagTemplate(String _tagname) {
        this._tagname = _tagname;
    }

    public TagTemplate(String _tagname, TagTemplate is_a) {
        this._tagname = _tagname;
        this._is_a1 = is_a;
    }

    //getters and setters
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

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

}
