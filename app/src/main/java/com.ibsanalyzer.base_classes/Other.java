package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class Other extends InputEvent {

    public Other(LocalDateTime time, List<Tag> tags) {

        super(time, tags);
    }
    public Other(LocalDateTime time, String comment, List<Tag> tags) {

        super(time, comment, tags);
    }
}