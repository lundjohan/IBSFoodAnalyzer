package com.johanlund.util;

import com.johanlund.base_classes.Tag;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public interface TagsWrapperBase {

    List<Tag> getTags();
    
    List<ScoreTime>getScoreTimes();

    LocalDateTime getChunkEnd();
}
