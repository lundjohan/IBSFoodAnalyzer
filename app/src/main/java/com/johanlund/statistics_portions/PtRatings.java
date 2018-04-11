package com.johanlund.statistics_portions;

import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Rating;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-04-05.
 */

public class PtRatings {
    List<PortionTime>portionTimes;
    List<Rating>ratings;
    //this is equivalent to last event in chunk.
    LocalDateTime lastTimeInChunk;

    public PtRatings(List<PortionTime> portionTimes, List<Rating> ratings,
                     LocalDateTime lastTimeInChunk) {
        this.portionTimes = portionTimes;
        this.ratings = ratings;
        this.lastTimeInChunk = lastTimeInChunk;
    }

    public List<PortionTime> getPortionTimes() {
        return portionTimes;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public LocalDateTime getLastTimeInChunk() {
        return lastTimeInChunk;
    }

    public void setPortionTimes(List<PortionTime> portionTimes) {
        this.portionTimes = portionTimes;
    }

    public static List<PtRatings> toPtRatings(List<Chunk> chunks) {
        List<PtRatings> toReturn = new ArrayList<>();
        for (Chunk c : chunks) {
            PtRatings ptsAndRs = new PtRatings(c.getPortionTimes(), c
                    .getRatings(), c.getLastTime());
            toReturn.add(ptsAndRs);
        }
        return toReturn;
    }
}
