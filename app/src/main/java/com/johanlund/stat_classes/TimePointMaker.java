package com.johanlund.stat_classes;

import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Rating;
import com.johanlund.statistics_point_classes.TimePoint;

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
    public static List<TimePoint> doRatingTimePoints(List<Rating> ratings, LocalDateTime chunkEnd, int
            scoreStart, int scoreEnd) {
        List<TimePoint> timePoints = new ArrayList<>();
        boolean periodHasStarted = false;
        LocalDateTime startTime = null;
        for (Rating r : ratings) {
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
    private static boolean isLastRating(Rating r, List<Rating>ratings){
        return r.getTime().equals(ratings.get(ratings.size()-1).getTime());
    }


    private static boolean isBetweenScores(Rating r, int scoreStart, int scoreEnd) {
        return r.getAfter() >= scoreStart && r.getAfter() <= scoreEnd;
    }
    /**
     * This is even simpler than above,
     * the algorithm simply keeps on going for a TimePoint if the next bm is scorestart<=bm <=scoreend.
     * The time period will be between the first bm in interval and the last bm in interval (which means that timeperiod length 0 will be common...)
     *
     * Given:
     * bms in ASC order
     *
     *
     */
    public static List<TimePoint> doBMTimePoints(List<Bm> bms, int scoreStart, int scoreEnd) {
        List<TimePoint> timePoints = new ArrayList<>();
        boolean periodHasStarted = false;
        LocalDateTime periodStart = null;
        LocalDateTime lastBmForPeriod = null;

        for (Bm b: bms){
            //last bm
            if (isBetweenScores(b, scoreStart, scoreEnd)){
                if (periodHasStarted){
                    if (isLastBm(b, bms)){
                        TimePoint tp = new TimePoint(periodStart, b.getTime());
                        timePoints.add(tp);
                    }
                    //!lastBm
                    else{
                        lastBmForPeriod = b.getTime();
                    }
                }
                //!periodHasStarted
                else{
                    periodHasStarted = true;
                    periodStart = b.getTime();
                    lastBmForPeriod = periodStart;
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

    private static boolean isBetweenScores(Bm b, int scoreStart, int scoreEnd) {
        return b.getComplete() >= scoreStart && b.getComplete() <= scoreEnd;
    }

    private static boolean isLastBm(Bm b, List<Bm>bms){
        return b.getTime().equals(bms.get(bms.size()-1).getTime());
    }
}
