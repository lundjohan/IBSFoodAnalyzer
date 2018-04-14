package com.johanlund.stat_classes;

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
    public static List<TimePoint> doTimePoint(List<Rating> ratings, LocalDateTime chunkEnd, int
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
                }
            }
            //!isBetweenScores
            else {
                if (periodHasStarted) {
                    if (isLastRating(r, ratings)) {
                        timePoints.add(new TimePoint(startTime, chunkEnd));
                        //loop will quit here
                    }
                    //!isLastRating.
                    else {
                        timePoints.add(new TimePoint(startTime, r.getTime()));
                    }
                    periodHasStarted = false;
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
}
