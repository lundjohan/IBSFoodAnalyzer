package com.johanlund.stat_backend.stat_util;


import com.johanlund.base_classes.Tag;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.List;

/*
    start <= end
 */
public class TimePeriod {
    LocalDateTime start;
    LocalDateTime end;

    public TimePeriod(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * @param timePeriods
     * @param hoursToRemove
     * @return List of shortened timeperiods from the right. If end -
     * hoursToRemoveFromEnd <= start List becomes shorter
     */
    public static List<TimePeriod> removeHoursAhead(List<TimePeriod> timePeriods, long
            hoursToRemove) {
        List<TimePeriod> toReturn = new ArrayList<>();
        for (TimePeriod tp : timePeriods) {
            if (tp.isOkToTrim(hoursToRemove)) {
                toReturn.add(tp.sliceFromRight(hoursToRemove));
            }
        }
        return toReturn;
    }

    /**
     * Prerequisute: no overlapping chunks (speaking about time).
     *
     * @param chunks
     * @param tp
     * @return
     */
    public static List<Tag> retrieveTagsForPeriod(List<Chunk> chunks, TimePeriod tp) {
        Chunk chunk = getChunkForPeriod(chunks, tp);
        return chunk.getTagsForPeriod(tp);
    }

    private static Chunk getChunkForPeriod(List<Chunk> chunks, TimePeriod tp) {
        Chunk toReturn = null;
        for (Chunk ch : chunks) {
            if ((tp.getStart().isAfter(ch.getStartTime()) || tp.getStart().isEqual(ch
                    .getStartTime()))
                    && (tp.getEnd().isBefore(ch.getLastTime()) || tp.getEnd().isEqual(ch
                    .getLastTime()))) {
                toReturn = ch;
                break;
            }
        }
        return toReturn;
    }

    /**
     * Given: under should not be zero
     * <p>
     * returns the quote of the division of absolute length
     */
    public static double getQuote(TimePeriod above, TimePeriod below) {
        long aboveSec = above.getLengthSec();
        long belowSec = below.getLengthSec();
        if (belowSec == 0) {
            return Double.NaN;
        }
        //must convert to double first, otherwise qoute will round down
        //btw => (double)aboveSec == ((double)aboveSec), but we want to avoid confusion
        return ((double) aboveSec) / ((double) belowSec);
    }

    public long getLengthSec() {
        return end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC);
    }

    private TimePeriod sliceFromRight(long hoursToRemove) {
        if (isOkToTrim(hoursToRemove)) {

        } else {
            throw new IllegalArgumentException("a TimePeriod cannot be sliced so end is before " +
                    "start");
        }
        end = end.minusHours(hoursToRemove);
        return this;
    }

    private boolean isOkToTrim(long hours) {
        LocalDateTime ldt = end.minusHours(hours);
        return ldt.isAfter(start) || ldt.isEqual(start);
    }
    // =================================================================================================================

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
    // =================================================================================================================
}
