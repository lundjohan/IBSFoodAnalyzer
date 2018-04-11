package com.johanlund.stat_classes;

import android.util.Log;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_portions.PortionTime;
import com.johanlund.statistics_portions.PtRatings;
import com.johanlund.statistics_settings_portions.PortionStatRange;
import com.johanlund.util.RatingTime;
import com.johanlund.util.TPUtil;
import com.johanlund.util.TimePeriod;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.johanlund.statistics_portions.PtRatings.toPtRatings;

public class PortionPointMaker {

    /*
    For performance it would be preferable if chunks was replaced with object that contains only
    <PortionSize, TimeForMeal>, Ratings, Breaks (Breaks can be replaced by a list that
    contains the others) and stopTime for each Chunk.

    => use of toPtRatings
    (main reason is that database cursor don't have to carry the weight
    of all the events)
 */
    public static List<PortionPoint> doPortionsPoints(List<Chunk> chunks, List<PortionStatRange>
            ranges, long startHoursAfterMeal, long
            stopHoursAfterMeal, int minHoursBetweenMeals) {
        List<PtRatings> ptRatings = toPtRatings(chunks);
        return toReplaceCalcPoints(ptRatings, ranges, startHoursAfterMeal, stopHoursAfterMeal, minHoursBetweenMeals);
    }
    private static List<PortionPoint> toReplaceCalcPoints(List<PtRatings> ptr, List<PortionStatRange>
            ranges, long startHoursAfterMeal, long stopHoursAfterMeal, int minHoursBetweenMeals) {
        joinTooClosePortions(ptr, minHoursBetweenMeals);
        List<PortionPoint> toReturn = new ArrayList<>();
        for (PortionStatRange range : ranges) {
            PortionPoint pp = getPPForRange(range, ptr, startHoursAfterMeal, stopHoursAfterMeal);
            toReturn.add(pp);
        }
        return toReturn;
    }
    //not finished
    private static void joinTooClosePortions(List<PtRatings> PtRatingsList, int minHoursBetweenMeals) {
        for (PtRatings ptAndR : PtRatingsList) {
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



    static PortionPoint getPPForRange(PortionStatRange range, List<PtRatings>
            afterJoin, long waitHoursAfterMeal, long stopHoursAfterMeal) {
        //format: avg_rating*min
        double rangeTotalScore = .0;
        //format: min
        double rangeTotalQuant = .0;
        for (PtRatings ptAndR : afterJoin) {
            List<TimePeriod> tps = extractTimePeriods(range, ptAndR.getPortionTimes(), waitHoursAfterMeal, stopHoursAfterMeal, ptAndR.getLastTimeInChunk());
            for (TimePeriod tp: tps){
                double[] scoreAndWeight = RatingTime.calcAvgAndWeight(tp, ptAndR.getRatings(), ptAndR.getLastTimeInChunk());
                rangeTotalScore += scoreAndWeight[0];
                rangeTotalQuant += scoreAndWeight[1]; //for every portion max quant is 1.0
            }
        }
        //PortionPoint quant should be in hours
        double avgScore = rangeTotalQuant == 0 ? Double.NaN : rangeTotalScore / rangeTotalQuant;
        return new PortionPoint(range, avgScore, rangeTotalQuant);
    }
    /**
     * This is the hard one
     *
     * @param range
     * @param portionTimes
     * @param chunkStopTime
     * @return
     */
    private static List<TimePeriod> extractTimePeriods(PortionStatRange range, List<PortionTime>
            portionTimes, long waitHoursAfterMeal, long stopHoursAfterMeal, LocalDateTime chunkStopTime) {

        //1. variables: range, portionTimes, waitHoursAfterMeal, stopHoursAfterMeal, chunkStopTime
        List<PortionTime> withinRange = getWithinRange(range, portionTimes);
        List<PortionTime> aboveRange = getAboveRange(range, portionTimes);

        //2. withinRange, aboveRange, waitHoursAfterMeal, stopHoursAfterMeal, chunkStopTime
        List<TimePeriod> tpsWithinRange = getTpsForPortions(withinRange, chunkStopTime, waitHoursAfterMeal, stopHoursAfterMeal);
        List<TimePeriod> tpsAboveRange = getTpsForPortions(aboveRange, chunkStopTime, waitHoursAfterMeal, stopHoursAfterMeal);

        //3. tpsWithinRange, tpsAboveRange
        leftsExceptRights(tpsWithinRange, tpsAboveRange);
        return tpsWithinRange;
    }
    private static List<PortionTime> getWithinRange(PortionStatRange range, List<PortionTime>
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

    private static List<PortionTime> getAboveRange(PortionStatRange range, List<PortionTime>
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
    private static List<TimePeriod> getTpsForPortions(List<PortionTime> withinRange, LocalDateTime
            chunkStopTime, long waitHoursAfterMeal, long stopHoursAfterMeal) {
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
     * Except == EXCEPT in databaseterms
     * <p>
     * End result can be that some TimePeriods are left with same start as end value. This must
     * be accounted for later in program.
     *  @param lefts
     * @param rights
     */
    private static void leftsExceptRights(List<TimePeriod> lefts, List<TimePeriod> rights) {
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
    private static TimePeriod leftExceptRights(TimePeriod left, List<TimePeriod> rightList) {
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
        return new TimePeriod(newStart, newEnd);
    }
}
