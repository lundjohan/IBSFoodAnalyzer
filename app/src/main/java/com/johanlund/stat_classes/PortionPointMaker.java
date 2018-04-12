package com.johanlund.stat_classes;

import android.util.Log;

import com.google.android.gms.plus.model.people.Person;
import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_portions.PortionTime;
import com.johanlund.statistics_portions.PtRatings;
import com.johanlund.statistics_settings_portions.PortionStatRange;
import com.johanlund.util.RatingTime;
import com.johanlund.util.TimePeriod;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
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
                                                              stopHoursAfterMeal, int
            minHoursBetweenMeals) {
        List<PtRatings> ptRatings = toPtRatings(chunks);
        return toReplaceCalcPoints(ptRatings, ranges, startHoursAfterMeal, stopHoursAfterMeal,
                minHoursBetweenMeals);
    }

    static List<PortionPoint> toReplaceCalcPoints(List<PtRatings> ptr,
                                                          List<PortionStatRange>
            ranges, long startHoursAfterMeal, long stopHoursAfterMeal, int minHoursBetweenMeals) {
        List<PtRatings> ptrJoined = joinTooClosePortions(ptr, minHoursBetweenMeals);
        List<PortionPoint> toReturn = new ArrayList<>();
        for (PortionStatRange range : ranges) {
            PortionPoint pp = getPPForRange(range, ptrJoined, startHoursAfterMeal, stopHoursAfterMeal);
            toReturn.add(pp);
        }
        return toReturn;
    }

    private static List<PtRatings> joinTooClosePortions(List<PtRatings> PtRatingsList, int
            minHoursBetweenMeals) {
        List<PtRatings>toReturn = new ArrayList<>();
        for (PtRatings ptAndR : PtRatingsList) {
            List<PortionTime> pts = joinTooClosePortions2(ptAndR.getPortionTimes(), minHoursBetweenMeals);
            PtRatings ptr = new PtRatings(pts, ptAndR.getRatings(), ptAndR.getLastTimeInChunk());
            toReturn.add(ptr);
        }
        return toReturn;
    }

    /**
     * If two portions are too close to each other, they should be gathered together into one.
     *
     * This function could work in different ways. I have decided to use it in way as below:
     * <p>
     * minDist == --
     * A1 (before join)
     * --p1---p2-p3---p4-p5-p6-----p7-p8p9
     * <p>
     * A2 (after join)
     * --p1---p2p3----p4p5--p6-----p7p8p9-
     * <p>
     * p6 will not have joined with p4p5.
     *
     * @param ptsOrig         in ASC order of time, will not be changed in method.
     * @param minDist == minHourDistanceBetweenMeals
     * @return
     */
    static List<PortionTime> joinTooClosePortions2(final List<PortionTime> ptsOrig, int
            minDist) {
        if (ptsOrig.size()<2){
            return ptsOrig;
        }
        return joinPt(ptsOrig.get(0), minDist, ptsOrig.subList(1, ptsOrig.size()), new ArrayList<PortionTime>(),minDist);
    }

    //recursive function
    /*
    - > minDist
    ----p1-p2-p3-----
    =>
    -------p2-p3    end result
           p1
     it will not end up as this:
     ----------p3    then it could join forever
               p2
               p1
     nor like this:
     -----p1---p3     also plausible (equally good)solution, but the furthest above was simpler
               p2
     */

    /**
     *
     * @param p1
     * @param distRema, if larger distances between portions => join
     * @param ptsOrig
     * @param toList
     * @return
     */
    private static List<PortionTime> joinPt(PortionTime p1, int distRema, List<PortionTime> ptsOrig,
                               List<PortionTime>toList, int originalMinDist) {
        if (ptsOrig.isEmpty()){
            toList.add(p1);
            return toList;
        }
        PortionTime p2 = ptsOrig.get(0);
        if (p1.getTime().plusHours(distRema).isAfter(p2.getTime())){
            PortionTime joinedPt = new PortionTime(p1.getPSize() + p2.getPSize(), p2.getTime());
            joinPt(joinedPt, distRema - (int)(p2.getTime().toEpochSecond(ZoneOffset.UTC)-p1.getTime().toEpochSecond(ZoneOffset.UTC))/(60*60), ptsOrig.subList(1, ptsOrig.size()), toList, originalMinDist);
        }
        else{
            toList.add(p1);
            joinPt(p2, originalMinDist, ptsOrig.subList(1, ptsOrig.size()), toList, originalMinDist);

        }
        return toList;
    }


    static PortionPoint getPPForRange(PortionStatRange range, List<PtRatings>
            afterJoin, long waitHoursAfterMeal, long stopHoursAfterMeal) {
        //format: avg_rating*quant
        double rangeTotalScore = .0;
        //format: quant
        double rangeTotalQuant = .0;
        for (PtRatings ptAndR : afterJoin) {
            List<TimePeriod> rawTps = simpleExtractTimePeriods(range, ptAndR.getPortionTimes(),
                    waitHoursAfterMeal, stopHoursAfterMeal);
            for (TimePeriod rawTp : rawTps) {
                double[] scoreAndWeight = RatingTime.calcAvgAndWeight(rawTp, ptAndR.getRatings(),
                        ptAndR.getLastTimeInChunk());
                rangeTotalScore += scoreAndWeight[0] * scoreAndWeight[1];
                rangeTotalQuant += scoreAndWeight[1]; //for every portion max quant is 1.0
            }
        }
        //PortionPoint quant should be in hours
        double avgScore = rangeTotalQuant == 0. ? Double.NaN : rangeTotalScore / rangeTotalQuant;
        return new PortionPoint(range, avgScore, rangeTotalQuant);
    }

    static List<TimePeriod> simpleExtractTimePeriods(PortionStatRange range, List<PortionTime>
            portionTimes, long waitHoursAfterMeal, long stopHoursAfterMeal) {

        //1. variables: range, portionTimes, waitHoursAfterMeal, stopHoursAfterMeal
        List<PortionTime> withinRange = getWithinRange(range, portionTimes);
        List<PortionTime> aboveRange = getAboveRange(range, portionTimes);

        //2. withinRange, aboveRange, waitHoursAfterMeal, stopHoursAfterMeal
        List<TimePeriod> tpsWithinRange = getRawTps(withinRange,
                waitHoursAfterMeal, stopHoursAfterMeal);
        List<TimePeriod> tpsAboveRange = getRawTps(aboveRange,
                waitHoursAfterMeal, stopHoursAfterMeal);

        //3. tpsWithinRange, tpsAboveRange
        leftsExceptRights(tpsWithinRange, tpsAboveRange);
        return tpsWithinRange;
    }

    /**
     * @param pts
     * @param startHour
     * @param stopHour
     * @return TimePeriods that has not been subjected (yet) to early chunkEnd or late start of ratings
     */
    private static List<TimePeriod> getRawTps(List<PortionTime> pts, long startHour,
                                              long stopHour) {
        List<TimePeriod>toReturn = new ArrayList<>();
        for (PortionTime pt:pts){
            LocalDateTime start = pt.getTime().plusHours(startHour);
            LocalDateTime end = pt.getTime().plusHours(stopHour);
            toReturn.add(new TimePeriod(start,end));
        }
        return toReturn;
    }

    private static List<PortionTime> getWithinRange(PortionStatRange range, List<PortionTime>
            portionTimes) {
        List<PortionTime> toReturn = new ArrayList<>();
        for (PortionTime pt : portionTimes) {
            //startPortions (incl), endPortions (excl)
            // range.start <= pt.size < range.stop
            if (pt.getPSize() >= range.getRangeStart() && pt.getPSize() < range
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
            if (pt.getPSize() >= range.getRangeStop()) {
                toReturn.add(pt);
            }
        }
        return toReturn;
    }

    /**
     * Except == EXCEPT in databaseterms
     * <p>
     * End result can be that some TimePeriods are left with same start as end value. This must
     * be accounted for later in program.
     *
     * @param lefts
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
            else if (right.getStart().isBefore(newStart) && right.getEnd().isAfter(newEnd)) {
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
            else if (right.getStart().equals(newEnd) || right.getEnd().equals(newEnd)) {
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
