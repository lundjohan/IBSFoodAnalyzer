package com.johanlund.util;

import com.johanlund.base_classes.TagBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public interface TagsWrapperBase {

    List<TagBase> getTags();
    
    List<ScoreTime>getScoreTimes();

    LocalDateTime getChunkEnd();
}
