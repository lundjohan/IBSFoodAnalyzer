package com.johanlund.statistics_time_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Rating;
import com.johanlund.statistics_point_classes.TimePoint;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-03-20.
 */

public class RatingTimeScoreWrapper extends TimeScoreWrapper {
    public RatingTimeScoreWrapper(int ratingStart, int ratingEnd, int durationLimit) {
        super(ratingStart, ratingEnd, durationLimit);
    }

    protected List<TimePoint> calcTimePeriods(Chunk c) {
        List<Rating> ratings = c.getRatings();
        return calcTimePeriods(ratings, c.getLastTime());
    }

    private List<TimePoint> calcTimePeriods(List<Rating> ratings, LocalDateTime lastTimeInChunk) {
        List<TimePoint> timePoints = new ArrayList<>();
        boolean periodHasStarted = false;
        LocalDateTime startTime = null;
        for (Rating r : ratings) {
            if (isBetweenScores(r)) {
                if (periodHasStarted) {
                    if (isLastRating(r, ratings)) {
                        timePoints.add(new TimePoint(startTime, lastTimeInChunk));
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
                        timePoints.add(new TimePoint(startTime, lastTimeInChunk));
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
    private boolean isLastRating(Rating r, List<Rating>ratings){
        return r.getTime().equals(ratings.get(ratings.size()-1).getTime());
    }


    private boolean isBetweenScores(Rating r) {
        return r.getAfter() >= scoreStart && r.getAfter() <= scoreEnd;
    }
}
