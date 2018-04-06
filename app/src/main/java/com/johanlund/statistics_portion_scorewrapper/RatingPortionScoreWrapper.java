package com.johanlund.statistics_portion_scorewrapper;

import android.util.Log;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_portions.PortionTime;
import com.johanlund.statistics_portions.PortionTimesAndRatings;
import com.johanlund.statistics_settings_portions.PortionStatRange;
import com.johanlund.util.TPUtil;
import com.johanlund.util.TimePeriod;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-04-03.
 */

public class RatingPortionScoreWrapper extends PortionScoreWrapper {

    public RatingPortionScoreWrapper(List<PortionStatRange> ranges, int waitHoursAfterMeal, int
            validHours, int minHoursBetweenMeals) {
        super(ranges, waitHoursAfterMeal, validHours, minHoursBetweenMeals);
    }

    /*
        For performance it would be preferable if chunks was replaced with object that contains only
        <PortionSize, TimeForMeal>, Ratings, Breaks (Breaks can be replaced by a list that
        contains the others) and stopTime for each Chunk.

        (main reason is that database cursor don't have to carry the weight
        of all the events)
     */
    @Override
    public List<PortionPoint> calcPoints(List<Chunk> chunks) {
        List<PortionTimesAndRatings> portionTimesAndRatings = chunksToPortionTimesAndRatings
                (chunks);
        return toReplaceCalcPoints(portionTimesAndRatings);
    }

    /**
     * simple
     *
     * @param chunks
     * @return
     */
    private List<PortionTimesAndRatings> chunksToPortionTimesAndRatings(List<Chunk> chunks) {
        List<PortionTimesAndRatings> toReturn = new ArrayList<>();
        for (Chunk c : chunks) {
            PortionTimesAndRatings ptsAndRs = new PortionTimesAndRatings(c.getPortionTimes(), c
                    .getRatings(), c.getLastTime());
            toReturn.add(ptsAndRs);
        }
        return toReturn;
    }


    private List<PortionPoint> toReplaceCalcPoints(List<PortionTimesAndRatings> ptr) {
        joinToNearPortions(ptr, minHoursBetweenMeals);
        List<PortionPoint> toReturn = new ArrayList<>();
        for (PortionStatRange range : ranges) {
            PortionPoint pp = getPPForRange(range, ptr);
            toReturn.add(pp);
        }
        return toReturn;
    }

    private PortionPoint getPPForRange(PortionStatRange range, List<PortionTimesAndRatings>
            afterJoin) {
        //format: avg_rating*min
        double rangeTotalScore = .0;
        //format: min
        double rangeTotalDuration = .0;
        for (PortionTimesAndRatings ptAndR : afterJoin) {
            List<TimePeriod> timePeriods = extractTimePeriods(range, ptAndR.getPortionTimes(),
                    ptAndR.getLastTimeInChunk());
            double[] scoreAndDuration = TPUtil.extractScoreAndDuration(timePeriods, ptAndR
                    .getRatings());
            rangeTotalScore += scoreAndDuration[0];
            rangeTotalDuration += scoreAndDuration[1];
        }
        //cannot divide by zero
        if (rangeTotalDuration == 0){
            return new PortionPoint(range, 0.0,0.0);
        }
        return new PortionPoint(range, rangeTotalScore / rangeTotalDuration,
                rangeTotalDuration);
    }

    /**
     * This is the hard one
     *
     * @param range
     * @param portionTimes
     * @param chunkStopTime
     * @return
     */
    private List<TimePeriod> extractTimePeriods(PortionStatRange range, List<PortionTime>
            portionTimes, LocalDateTime chunkStopTime) {

        //1. variables: range, portionTimes, waitHoursAfterMeal, stopHoursAfterMeal, chunkStopTime
        List<PortionTime> withinRange = getWithinRange(range, portionTimes);
        List<PortionTime> aboveRange = getAboveRange(range, portionTimes);

        //2. withinRange, aboveRange, waitHoursAfterMeal, stopHoursAfterMeal, chunkStopTime
        List<TimePeriod> tpsWithinRange = getTpsForPortions(withinRange, chunkStopTime);
        List<TimePeriod> tpsAboveRange = getTpsForPortions(aboveRange, chunkStopTime);

        //3. tpsWithinRange, tpsAboveRange
        leftsExceptRights(tpsWithinRange, tpsAboveRange);
        return tpsWithinRange;
    }


    private List<PortionTime> getWithinRange(PortionStatRange range, List<PortionTime>
            portionTimes) {
        List<PortionTime> toReturn = new ArrayList<>();
        for (PortionTime pt : portionTimes) {
            //startPortions (incl), endPortions (excl)
            if (pt.getPortionSize() >= range.getRangeStart() && pt.getPortionSize() < range
                    .getRangeStop()) {
                toReturn.add(pt);
            }
        }
        return toReturn;
    }

    private List<PortionTime> getAboveRange(PortionStatRange range, List<PortionTime>
            portionTimes) {
        List<PortionTime> toReturn = new ArrayList<>();
        for (PortionTime pt : portionTimes) {
            //since endPortions is excl, we want larger or same to get portions that doesn't fit
            // range => the ones we are interested in here
            if (pt.getPortionSize() >= range.getRangeStop()) {
                toReturn.add(pt);
            }
        }
        return toReturn;
    }

    /**
     * @param withinRange   must be in ASC order.
     * @param chunkStopTime
     * @return
     */
    private List<TimePeriod> getTpsForPortions(List<PortionTime> withinRange, LocalDateTime
            chunkStopTime) {
        List<TimePeriod> toReturn = new ArrayList<>();
        for (PortionTime pt : withinRange) {
            LocalDateTime start = pt.getDateTime().plusHours(waitHoursAfterMeal);
            LocalDateTime stop = pt.getDateTime().plusHours(stopHoursAfterMeal);

            //we have to deal with the fact that the TimePeriod we are building might overflow
            // chunkStopTime
            if (stop.isAfter(chunkStopTime)) {
                if (!start.isBefore(chunkStopTime)) {
                    //no point in continuing
                    break;
                }
                stop = chunkStopTime;
            }
            toReturn.add(new TimePeriod(start, stop));
        }
        return toReturn;
    }

    /**
     * Except == EXCEPT in databaseterms (compare Join, Intersect)
     * <p>
     * End result can be that some TimePeriods are left with same start as end value. This must
     * be accounted for later in program.
     *  @param lefts
     * @param rights
     */
    private void leftsExceptRights(List<TimePeriod> lefts, List<TimePeriod> rights) {
        for (int i = 0; i < lefts.size(); i++) {
            lefts.set(i, leftExceptRights(lefts.get(i), rights));
        }
    }

    /**
     * About drawings in function.
     * After one or more loops left timepoint can be shorter in length than right.
     * Left is however never longer than right.
     *
     *
     * @param left
     * @param rightList must be in ASC order, all of same time length as left.
     * @return
     */
    private TimePeriod leftExceptRights(TimePeriod left, List<TimePeriod> rightList) {
        LocalDateTime newStart = left.getStart();
        LocalDateTime newEnd = left.getEnd();
        for (TimePeriod right : rightList) {
            /*Case A.
                            |-----| left
            |----------| right
             */
            if (right.getEnd().isBefore(newStart)) {
                continue;
            }
             /*Case B.
            |-------| left
                           |----------| right
             */

            else if (right.getStart().isAfter(newEnd)) {
                break;
            }
             /* This more or less happens everytime, and early so good to have high up.
                      |-----| left
                   |----------| right

             */
            else if (right.getStart().isBefore(newStart) && right.getEnd().isAfter(newEnd)){
                newStart = newEnd;
                break;

            }
            /*Equals. This should never happen since Meals are not allowed to be at same
            time. However, there might have been some thing I have foreseen higher in code, or user
            might have mixed with imports and I
            therefor let this stand.
                   |-----| left
                   |----------| right
                   or
                        |-----| left
                   |----------| right

             */
            else if (right.getStart().equals(newEnd) || right.getEnd().equals(newEnd)){
                newStart = newEnd;
                break;

            }
            /*Case C.
            |--------| left
                   |----------| right
            */
            else if (right.getStart().isBefore(newEnd) && right.getStart().isAfter(newEnd)) {
                newEnd = right.getStart();
                break;
            }

            /*Case D.
                   |------| left
            |----------| right
             */
            else if (right.getEnd().isAfter(newStart) && right.getStart().isBefore(newStart)) {
                newStart = right.getEnd(); //FEL! right.end kan ju vara till höger om left.end
                continue;
            }
        }
        Log.d("debug", "---------------------------------------------------------------------------");
        Log.d("debug", "AFTER LOOP");
        Log.d("debug", "newStart: "+newStart);
        Log.d("debug", "newEnd: "+newEnd);
        if (newEnd.isBefore(newStart)){
            throw new RuntimeException("newStart should never be after newEnd!");
        }
        return new TimePeriod(newStart, newEnd);
    }


    //not finished
    private static void joinToNearPortions(List<PortionTimesAndRatings> portionTimesAndRatingsList, int minHoursBetweenMeals) {
        for (PortionTimesAndRatings ptAndR : portionTimesAndRatingsList) {
            joinToNearPortions2(ptAndR.getPortionTimes(), minHoursBetweenMeals);
        }
    }

    /**
     * This function could work in different ways. I have decided to use it in way as below:
     *
     * minHoursBetweenMeals == --
     * A1 (before join)
     * --p1---p2-p3---p4-p5-p6-----p7-p8p9
     *
     * A2 (after join)
     * --p1---p2p3----p4p5--p6-----p7p8p9-
     *
     * p6 will not have joined with p4p5.
     *
     *
     *
     * @param portionTimes in ASC order of time
     * @param minHoursBetweenMeals
     * @return
     */
    private static void joinToNearPortions2(List<PortionTime> portionTimes, int
            minHoursBetweenMeals) {
        for (int i = 0;i<portionTimes.size()-1; i++){
            LocalDateTime thisP = portionTimes.get(i).getDateTime();
            LocalDateTime nextP = portionTimes.get(i+1).getDateTime();
            if (thisP.plusHours(minHoursBetweenMeals).isAfter(nextP)){
                PortionTime changedP = new PortionTime(portionTimes.get(i+1).getPortionSize(),thisP);
                portionTimes.set(i+1, changedP);
            }
        }
    }
}

