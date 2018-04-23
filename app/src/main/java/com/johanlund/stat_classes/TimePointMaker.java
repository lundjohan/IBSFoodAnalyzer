package com.johanlund.stat_classes;

import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.RatingTimes;
import com.johanlund.util.ScoreTime;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class TimePointMaker {
    /**
     * Given:
     * 1. lastRating <= chunkEnd
     * 2. ratings in ASC order
     * 3. scoreStart <= scoreEnd
     *
     */
    public static List<TimePoint>doRatingTimePoints(RatingTimes rt, int
            scoreStart, int scoreEnd){
        return doRatingTimePoints(rt.getScoreTimes(), rt.getChunkEnd(), scoreStart, scoreEnd);
    }
    public static List<TimePoint> doRatingTimePoints(List<ScoreTime> ratings, LocalDateTime chunkEnd, int
            scoreStart, int scoreEnd) {
        List<TimePoint> timePoints = new ArrayList<>();
        boolean periodHasStarted = false;
        LocalDateTime startTime = null;
        for (ScoreTime r : ratings) {
            if (isBetweenScores(r, scoreStart, scoreEnd)) {
                if (periodHasStarted) {
                    if (isLastRating(r, ratings)) {
                        timePoints.add(new TimePoint(startTime, chunkEnd));
                        //loop will quit here
                    }
                    //!isLastRating.
                    else {
                        continue;
                    }
                }
                //!periodHasStarted
                else {
                    periodHasStarted = true;
                    startTime = r.getTime();
                    if (isLastRating(r, ratings)) {
                        timePoints.add(new TimePoint(startTime, chunkEnd));
                        //loop will quit here
                    }
                }
            }
            //!isBetweenScores
            else {
                if (periodHasStarted) {
                    periodHasStarted = false;
                    timePoints.add(new TimePoint(startTime, r.getTime()));
                }
                //!periodHasStarted
                else {
                    continue;
                }
            }
        }
        return timePoints;
    }
    private static boolean isLastRating(ScoreTime r, List<ScoreTime> ratings){
        return r.getTime().equals(ratings.get(ratings.size()-1).getTime());
    }


    private static boolean isBetweenScores(ScoreTime r, int scoreStart, int scoreEnd) {
        return r.getScore() >= scoreStart && r.getScore() <= scoreEnd;
    }
    /**
     * This is even simpler than above,
     * the algorithm simply keeps on going for a TimePoint if the next bm is scorestart<=bm <=scoreend.
     * The time period will be between the first bm in interval and the last bm in interval (which means that timeperiod length 0 will be common...)
     *
     * Given:
     * 1. bms in ASC order
     * 2. two bm cannot have same time
     *
     *
     */
    public static List<TimePoint> doBMTimePoints(List<ScoreTime> bms, int scoreStart, int scoreEnd) {
        List<TimePoint> timePoints = new ArrayList<>();
        boolean periodHasStarted = false;
        LocalDateTime periodStart = null;
        LocalDateTime lastBmForPeriod = null;

        for (ScoreTime b: bms){
            //last bm
            if (isBetweenScores(b, scoreStart, scoreEnd)){
                if (periodHasStarted){
                    lastBmForPeriod = b.getTime();

                }
                //!periodHasStarted
                else{
                    periodHasStarted = true;
                    periodStart = b.getTime();
                    lastBmForPeriod = periodStart;
                }
                if (isLastBm(b, bms)){
                    TimePoint tp = new TimePoint(periodStart, b.getTime());
                    timePoints.add(tp);
                }
            }
            //!isBetweenScores
            else{
                if (periodHasStarted){
                    TimePoint tp = new TimePoint(periodStart, lastBmForPeriod);
                    timePoints.add(tp);
                    periodHasStarted = false;
                }
                //!periodHasStarted
                else{
                    continue;
                }
            }
        }
        return timePoints;
    }

    private static boolean isLastBm(ScoreTime b, List<ScoreTime>bms){
        return b.getTime().equals(bms.get(bms.size()-1).getTime());
    }
}
