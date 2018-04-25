package com.johanlund.stat_classes;

import com.johanlund.base_classes.TagBase;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_portions.PortionTime;
import com.johanlund.statistics_portions.PtRatings;
import com.johanlund.statistics_settings_portions.PortionStatRange;
import com.johanlund.util.RatingTime;
import com.johanlund.util.TagsWrapperBase;
import com.johanlund.util.TimePeriod;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.ArrayList;
import java.util.List;

public class PortionPointMaker {

    /*
    For performance it would be preferable if chunks was replaced with object that contains only
    <PortionSize, TimeForMeal>, Ratings, Breaks (Breaks can be replaced by a list that
    contains the others) and stopTime for each Chunk.

    => use of toPtRatings
    (main reason is that database cursor don't have to carry the weight
    of all the events)
 */
    public static List<PortionPoint> doPortionsPoints(List<TagsWrapperBase> ptRatings, List<PortionStatRange>
            ranges, long startHoursAfterMeal, long
                                                              stopHoursAfterMeal, int
            minHoursBetweenMeals) {
        return toReplaceCalcPoints(ptRatings, ranges, startHoursAfterMeal, stopHoursAfterMeal,
                minHoursBetweenMeals);
    }

    static List<PortionPoint> toReplaceCalcPoints(List<TagsWrapperBase> ptr,
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

    //TagsWrapper will be dynamically used here.
    private static List<PtRatings> joinTooClosePortions(List<TagsWrapperBase> tagsWrapperList, int
            minHoursBetweenMeals) {
        List<PtRatings>toReturn = new ArrayList<>();
        for (TagsWrapperBase tagsWrapper : tagsWrapperList) {
            List<TagBase> pts = joinTooClosePortions2(tagsWrapper.getTags(), minHoursBetweenMeals);
            PtRatings ptr = new PtRatings(pts, tagsWrapper.getScoreTimes(), tagsWrapper.getChunkEnd());
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
    static List<TagBase> joinTooClosePortions2(final List<TagBase> ptsOrig, int
            minDist) {
        if (ptsOrig.size()<2){
            return ptsOrig;
        }
        return joinPt(ptsOrig.get(0), minDist, ptsOrig.subList(1, ptsOrig.size()), new ArrayList<TagBase>(),minDist);
    }

    //recursive function, WELL TESTED
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
     * @param p1 is Tag (not PortionTime)
     * @param distRema, 0 <= disRem <= distMin
     * @param ptsOrig
     * @param toList
     * @return
     */
    private static List<TagBase> joinPt(TagBase p1, int distRema, List<TagBase> ptsOrig,
                                        List<TagBase>toList, final int minDist) {
        if (ptsOrig.isEmpty()){
            toList.add(p1);
            return toList;
        }
        TagBase p2 = ptsOrig.get(0);
        if (p1.getTime().plusHours(distRema).isAfter(p2.getTime())){
            PortionTime joinedPt = new PortionTime(p1.getSize() + p2.getSize(), p2.getTime());
            joinPt(joinedPt, distRema - (int)(p2.getTime().toEpochSecond(ZoneOffset.UTC)-p1.getTime().toEpochSecond(ZoneOffset.UTC))/(60*60), ptsOrig.subList(1, ptsOrig.size()), toList, minDist);
        }
        else{
            toList.add(p1);
            joinPt(p2, minDist, ptsOrig.subList(1, ptsOrig.size()), toList, minDist);

        }
        return toList;
    }

    /**
     * @param startHour != stopHour
     */
    static PortionPoint getPPForRange(PortionStatRange range, List<PtRatings>
            afterJoin, long startHour, long stopHour) {
        //format: avg_rating*quant
        double rangeTotalScore = .0;
        //format: quant
        double rangeTotalQuant = .0;
        for (PtRatings ptAndR : afterJoin) {
            List<TimePeriod> tpsAfterExcept = makeExceptTps(range, ptAndR.getTags(),
                    startHour, stopHour);
            for (TimePeriod tpExc : tpsAfterExcept) {
                double[] scoreAndWeight = RatingTime.calcAvgAndWeight(tpExc, ptAndR.getScoreTimes(),
                        ptAndR.getChunkEnd());
                //calcAvgAndWeight do not know that tp might have been shortened due to except operation.
                //=> weightAfter_exc takes care of that.
                //looks complicated but is tested comp and algebraically
                double weightAfter_exc = (double)tpExc.getLengthSec()/ (stopHour-startHour)/60/60;
                double weightAfter_exc_lateRatings_earlyChunk = scoreAndWeight[1] * weightAfter_exc;
                rangeTotalScore += scoreAndWeight[0] * weightAfter_exc_lateRatings_earlyChunk;
                rangeTotalQuant += weightAfter_exc_lateRatings_earlyChunk; //for every portion max quant is 1.0
            }
        }
        //PortionPoint quant should be in hours
        double avgScore = rangeTotalQuant == 0. ? Double.NaN : rangeTotalScore / rangeTotalQuant;
        return new PortionPoint(range, avgScore, rangeTotalQuant);
    }

    static List<TimePeriod> makeExceptTps(PortionStatRange range, List<TagBase>
            portionTimes, long waitHoursAfterMeal, long stopHoursAfterMeal) {

        //1. variables: range, portionTimes, waitHoursAfterMeal, stopHoursAfterMeal
        List<TagBase> withinRange = getWithinRange(range, portionTimes);
        List<TagBase> aboveRange = getAboveRange(range, portionTimes);

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
    private static List<TimePeriod> getRawTps(List<TagBase> pts, long startHour,
                                              long stopHour) {
        List<TimePeriod>toReturn = new ArrayList<>();
        for (TagBase pt:pts){
            LocalDateTime start = pt.getTime().plusHours(startHour);
            LocalDateTime end = pt.getTime().plusHours(stopHour);
            toReturn.add(new TimePeriod(start,end));
        }
        return toReturn;
    }

    private static List<TagBase> getWithinRange(PortionStatRange range, List<TagBase>
            portionTimes) {
        List<TagBase> toReturn = new ArrayList<>();
        for (TagBase pt : portionTimes) {
            //startPortions (incl), endPortions (excl)
            // range.start <= pt.size < range.stop
            if (pt.getSize() >= range.getRangeStart() && pt.getSize() < range
                    .getRangeStop()) {
                toReturn.add(pt);
            }
        }
        return toReturn;
    }

    private static List<TagBase> getAboveRange(PortionStatRange range, List<TagBase>
            portionTimes) {
        List<TagBase> toReturn = new ArrayList<>();
        for (TagBase pt : portionTimes) {
            //since endPortions is excl, we want larger or same to get portions that doesn't fit
            // range => the ones we are interested in here
            if (pt.getSize() >= range.getRangeStop()) {
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
    static TimePeriod leftExceptRights(TimePeriod left, final List<TimePeriod> rightList) {
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
                   or
                   Equals. This should never happen in beginning since Meals are not allowed to be at same
                    time. However, after some loops here we might be at this state.
                   |-----| left
                   |----------| right
                   or
                        |-----| left
                   |----------| right
                   or
                   |----------| left
                   |----------| right

             */
            else if (!right.getStart().isAfter(newStart) && !right.getEnd().isBefore(newEnd)) {
                newStart = newEnd;
                break;

            }

            /*Case C.
            |--------| left
                   |----------| right
            or (this shouldn't be necessary since right >= left, but better be safe than sorry)
                   |-------------| left
                   |-----| right
            */
            else if (right.getStart().isBefore(newEnd) && !right.getStart().isBefore(newStart)) {
                newEnd = right.getStart();
                break;
            }

            /*Case D.
                   |------| left
            |----------| right

            or (this shouldn't be necessary since right >= left, but better be safe than sorry)
            |-------------| left
            |-----| right

             */
            else if (right.getEnd().isAfter(newStart) && !right.getStart().isAfter(newStart)) {
                newStart = right.getEnd();
                continue;
            }
        }
        return new TimePeriod(newStart, newEnd);
    }
}
