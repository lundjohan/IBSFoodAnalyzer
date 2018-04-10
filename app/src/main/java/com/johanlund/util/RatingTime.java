package com.johanlund.util;

import com.johanlund.base_classes.Rating;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;

import java.util.List;


 /*
 *
 * Used by stat classes that handles avg Rating.
 * see constructor for rules.
 *
 */

public class RatingTime {
    private TimePeriod tp;
    private List<Rating> ratings;
    private LocalDateTime chunkEnd;

    /**
     * time:            -
     * rating:          o
     * lastTimeOfChunk: ||
     *
     * Ratings must be in ASC order.
     * tp.start >= tp.end (this is the condition of TimePeriod)
     *
     *
     * A.
     * ratings   o--|---o----o---||     OK! (normal case -> timeOfFirstRating <= tpStart)
     * tp:          |-----------|
     *
     * B.
     * ratings      |---o----o--||      OK! (beginning of tp have no score)
     * tp:          |-----------|
     *
     * C.
     * ratings       |--o-------||      OK! (this does in theory never happen =>
     * tp:          |-----------|         tps startHourAfterEvent cant be before beginning of
     *                                    Chunk (it makes no sense, since startHourAfterEvent >= 0))
     *                                    However, since we don't pass startOfChunk to this class,
     *                                    its the first Rating that counts. This case is in practice
     *                                    the same as B.
     *
     * D.
     * ratings      |o-------||         OK! (tps stop hour extends after chunkEnd.
     * tp:              |------|          A trick is used to make this work => we use fraction
     *                                    (==weight) to lower the quantity proportionally to the part
     *                                    that is missing. It is not a perfect solution, but a
     *                                    compromise.
     *                                    The problem with this solution is that an effect of might
     *                                    happen at a certain time later. However it seems
     *                                    a shame to remove data completely if only a small fraction
     *                                    is missing of chunk). Perhaps have a settings option to
     *                                    turn this function on/ off?
     *
     * E.
     * ratings      |o--||              NOT OK! (startOfTp >= chunkEnd).
     * tp:                  |------|      We don't know what happens between chunkEnd and
     *                                    startofTp. There might be a missing Rating with another
     *                                    score.
     *
     * F.
     * ratings      |---o----o--|-o-||  NOT OK! (useless last rating)
     * tp:          |-----------|
     *
     * G.
     * ratings   o-o|---o----o----||    NOT OK! (useless first rating)
     * tp:          |-----------|
     *
     * H.
     * ratings      |-----------|-o-||  NOT OK! (rating appears first after)
     * tp:          |-----------|
     *
     * I.
     * ratings      |------------||     NOT OK! (empty rating list)
     * tp:          |-----------|
     *
     *
     * ratings       |--o-------||      NOT OK! (tps startHourAfterEvent cant be before beginning of
     * tp:          |-----------|         Chunk (it makes no sense, since startHourAfterEvent >= 0))
     *
     */


    public RatingTime(TimePeriod tp, List<Rating> ratings, LocalDateTime chunkEnd) {
        this.tp = tp;
        this.ratings = ratings;
        this.chunkEnd = chunkEnd;
    }

    /**
     * method is adjusting ratings to tp to fit with RatingTime conditions (see diagrams above constructor)
     *
     * @param tp
     * @param manyRatings must be in ASC order (it is not checked for in method).
     * @return null if ratings are empty, or if there is no Rating before end of tp.
     * @return [Avg score for tp, factor] where factor/ weight is >0 && <= 1.0. factor
     * later adjusts quantity
     *
     */
    public static double[] calcAvgAndWeight(TimePeriod tp, List<Rating> manyRatings, LocalDateTime endOfChunk){
        //see drawings above constructor.
        if (manyRatings.isEmpty() ||
                !manyRatings.get(0).getTime().isBefore(tp.getEnd())||
                !tp.getStart().isBefore(endOfChunk)
                ){
            return null;
        }
        List<Rating> inScope = getDivsBetweenAndSometimesOneBefore(tp, manyRatings);
        if (inScope.isEmpty()){
            return null;
        }
        RatingTime rt = new RatingTime(tp, inScope, endOfChunk);
        return rt.calcAvgPoints();
    }

    /**
     * Given: Conditions are met as seen from diagram of constructor.
     * @return [score, factor] (factor/ weight is used internally to reduce quantity if needed be)
     * @return null if point should have no points.
     *
     */
    private double[] calcAvgPoints() {
        double[] toReturn;
        if (ratings.get(0).getTime().isAfter(tp.getStart())) {
           toReturn =  calcWithLateFirstRating();
        }
        else if (chunkEnd.isBefore(tp.getEnd())){
            toReturn = calcWithEndOfChunkBeforeTpEnd();
        }
        else {
           toReturn =  calcAvgPointsNormal();
        }
        return toReturn;
    }

    /**
     * empty ratings not allowed (already checked for)
     *
     * tp:          |-----------|
     * ratings   o--|---o----o--|   OK! (normal case)
     *
     * this is also OK:
     * tp:          |-----------|
     * ratings      o---o----o--|
     *
     * (Remember that there is only one rating <= tp.start)
     *
     * All the time period have corresponding rating score, so no problem here.
     * Factor return will be 1.0.
     */
    private double[] calcAvgPointsNormal() {
        double avgScore = calcAvgScoreFromToTime(tp.getStart(), tp.getEnd(), ratings);
        return new double[]{avgScore, 1.0};
    }
    /*
     * empty ratings not allowed (already checked for)
     * first rating should come after tp.start (same time not allowed!)
     *
     *
     * tp:          |-----------|
     * ratings      |---o----o--|   OK! (beginning of tp have no score)
     *
     * We need to use factor/ weight here. The first part will have no score so we drag out score
     * from the right to fill it. Because of this the factor will be weighted
     * proportionally (lineary) lower (<1.0)
     *
     */


    private double[] calcWithLateFirstRating() {

        LocalDateTime timeFirstRating = ratings.get(0).getTime();
        double avgScore = calcAvgScoreFromToTime(timeFirstRating, tp.getEnd(), ratings);

        //do factor/ weight
        TimePeriod leftPartWithoutScore = new TimePeriod(tp.getStart(), timeFirstRating);
        double factor = TimePeriod.getQuote(leftPartWithoutScore, tp);
        return new double[] {avgScore, factor};
    }
    /* D.
     * ratings      |o-------||         OK! (tps stop hour extends after chunkEnd.
     * tp:              |------|          A trick is used to make this work => we use fraction
     *                                    (==weight) to lower the quantity proportionally to the part
     *                                    that is missing. It is not a perfect solution, but a
     *                                    compromise.
     */
    private double[] calcWithEndOfChunkBeforeTpEnd() {
        double avgScore = calcAvgScoreFromToTime(tp.getStart(), chunkEnd, ratings);
        //do factor/ weight
        TimePeriod rightPartWithoutScore = new TimePeriod(chunkEnd, tp.getEnd());
        double factor = TimePeriod.getQuote(rightPartWithoutScore, tp);
        return new double[] {avgScore, factor};
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @param ratings cannot be of size 0. First rating must be before startTime. sorted in ASC order.
     * @return
     */
    static double calcAvgScoreFromToTime(LocalDateTime startTime, LocalDateTime endTime, List<Rating>ratings){
        if (ratings.size() == 1) {
            return ratings.get(0).getAfter();
        }
        //time of div before <startTime> (the first div to take into account) is not interesting (it
        // can have happened many days before), only its score.
        long startLongSec = startTime.toEpochSecond(ZoneOffset.UTC);
        double scoreMultWithTime = 0;
        for (int i = 1; i < ratings.size(); i++) {
            LocalDateTime t = ratings.get(i).getTime();
            double timeDifInSec = t.toEpochSecond(ZoneOffset.UTC) - startLongSec;
            scoreMultWithTime += ratings.get(i - 1).getAfter() * timeDifInSec;
            startLongSec = ratings.get(i).getTime().toEpochSecond(ZoneOffset.UTC);
        }
        //the last one
        long toLong = endTime.toEpochSecond(ZoneOffset.UTC);
        double lastTimeDif = toLong - startLongSec;
        scoreMultWithTime += ratings.get(ratings.size() - 1).getAfter() * lastTimeDif;
        long durationPeriodSec = endTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC);
        double avgScore = scoreMultWithTime / ((durationPeriodSec));
        return avgScore;
    }


    /**
     * given: empty list already checked for
     * @return
     */
    private boolean ratingStartsBeforeTpEnd(){
        return ratings.get(0).getTime().isBefore(tp.getEnd());
    }

    /**
     * given: empty list has already been checked for
     * @return
     */
    private boolean ratingWithinScope(){
        LocalDateTime startRating = ratings.get(0).getTime();
        LocalDateTime endRating = ratings.get(ratings.size()-1).getTime();
        return tp.getStart().isBefore(startRating) && tp.getEnd().isAfter(endRating);
    }


    private static List<Rating> getDivsBetweenAndSometimesOneBefore(TimePeriod tp, List<Rating>divs){
        return getDivsBetweenAndSometimesOneBefore(tp.getStart(), tp.getEnd(), divs);
    }
    /**
     * Legacy code. TEST AND PERHAPS IMPROVE. (Main reason for code is to be used in this class (I believe)! )
     *
     * current problem if only one div from getRatings, it should still return one div but it returns zero.
     * If there are no divs in chunk, an empty List is returned.
     */
    static List<Rating> getDivsBetweenAndSometimesOneBefore(LocalDateTime from, LocalDateTime to,
                                                            List<Rating> divs){
        //get firstInd
        int firstInd = 0;
        if (divs.isEmpty()){
            return divs;
        }
        for (int i = 0; i < divs.size(); i++) {
            LocalDateTime divTime = divs.get(i).getTime();
            if (divTime.isBefore(from) || divTime.isEqual(from)) {
                firstInd = i;
            } else {
                break;
            }
        }

        //get LastInd
        int lastInd = firstInd;
        for (int i = firstInd+1; i < divs.size(); i++) {
            LocalDateTime divTime = divs.get(i).getTime();
            if (divTime.isAfter(to)) {
                break;
            } else {
                lastInd = i;
            }
        }
        //sublist(incl, excl (therefore +1))
        return divs.subList(firstInd, lastInd+1);
    }
}
