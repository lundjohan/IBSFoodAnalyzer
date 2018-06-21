package com.johanlund.stat_backend.stat_util;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class RatingTimes extends ScoreTimesBase {

    //the end of chunk might very well be after last rating
    private LocalDateTime chunkEnd;

    public RatingTimes(List<ScoreTime> ratings, LocalDateTime chunkEnd) {
        super(ratings);
        this.chunkEnd = chunkEnd;
    }

    public LocalDateTime getChunkEnd() {
        return chunkEnd;
    }
}
