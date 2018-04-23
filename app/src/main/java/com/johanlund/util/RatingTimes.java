package com.johanlund.util;

import com.johanlund.base_classes.Rating;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class RatingTimes extends ScoreTimesBase{

    //the end of chunk might very well be after last rating
    private LocalDateTime chunkEnd;

    public RatingTimes(List<CompleteTime> ratings, LocalDateTime chunkEnd) {
        super(ratings);
        this.chunkEnd = chunkEnd;
    }

    public LocalDateTime getChunkEnd() {
        return chunkEnd;
    }
}
