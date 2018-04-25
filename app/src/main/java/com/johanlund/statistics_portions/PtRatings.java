package com.johanlund.statistics_portions;

import com.johanlund.base_classes.TagBase;
import com.johanlund.util.ScoreTime;
import com.johanlund.statistics_avg.TagsWrapper;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-04-05.
 */

public class PtRatings implements TagsWrapperBase{
    List<TagBase>portionTimes;
    List<ScoreTime>ratings;
    //this is equivalent to last event in chunk.
    LocalDateTime lastTimeInChunk;

    public PtRatings(List<TagBase> portionTimes, List<ScoreTime> ratings,
                     LocalDateTime lastTimeInChunk) {
        this.portionTimes = portionTimes;
        this.ratings = ratings;
        this.lastTimeInChunk = lastTimeInChunk;
    }

    public List<TagBase> getTags() {
        return portionTimes;
    }

    public List<ScoreTime> getScoreTimes() {
        return ratings;
    }

    public LocalDateTime getChunkEnd() {
        return lastTimeInChunk;
    }

    public void setPortionTimes(List<TagBase> portionTimes) {
        this.portionTimes = portionTimes;
    }

    public static List<PtRatings> toPtRatings(List<TagsWrapper> chunks) {
        List<PtRatings> toReturn = new ArrayList<>();
        /*for (TagsWrapper c : chunks) {
            PtRatings ptsAndRs = new PtRatings(c.getTags(), c
                    .getScoreTimes(), c.getLastTime());
            toReturn.add(ptsAndRs);
        }*/
        return toReturn;
    }
}
