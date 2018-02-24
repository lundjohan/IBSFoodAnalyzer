package com.johanlund.base_classes;

import com.johanlund.constants.Constants;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class Other extends InputEvent {

    public Other(LocalDateTime time, List<Tag> tags) {

        super(time, tags);
    }
    public Other(LocalDateTime time, String comment, List<Tag> tags) {

        super(time, comment, tags);
    }

    public Other(LocalDateTime ldt, String comment, boolean hasBreak, List<Tag> tags) {
        super(ldt, comment, hasBreak,tags);
    }

    @Override
    public int getType() {
        return Constants.OTHER;
    }
}