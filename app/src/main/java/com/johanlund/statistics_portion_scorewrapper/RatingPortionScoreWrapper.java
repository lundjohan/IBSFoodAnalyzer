package com.johanlund.statistics_portion_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Rating;
import com.johanlund.statistics_point_classes.PortionPoint;
import com.johanlund.statistics_portions.PortionTime;
import com.johanlund.statistics_portions.PortionTimesAndRatings;
import com.johanlund.statistics_settings_portions.PortionStatRange;
import com.johanlund.util.TimePeriod;

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
        contains the others).

        (main reason is that database cursor dont have to carry the weight
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
    }


    private List<PortionPoint> toReplaceCalcPoints(List<PortionTimesAndRatings> ptr) {
        List<PortionTimesAndRatings> afterJoin = joinToNearPortions(ptr,
                minHoursBetweenMeals);

        List<PortionPoint> toReturn = new ArrayList<>();
        for (PortionStatRange range : ranges) {
            double rangeTotalScore = .0;
            double rangeTotalDuration = .0;
            for (PortionTimesAndRatings ptAndR : afterJoin) {
                List<TimePeriod> timePeriods = extractTimePeriods(range, ptAndR.getPortionTimes());
                double[] scoreAndDuration = extractScoreAndDuration(timePeriods, ptAndR
                        .getRatings());
                rangeTotalScore += scoreAndDuration[0];
                rangeTotalDuration += scoreAndDuration[1];
            }
            PortionPoint pp = new PortionPoint(range, rangeTotalScore / rangeTotalDuration,
                    rangeTotalDuration);
            toReturn.add(pp);
        }
        return toReturn;
    }

    /**
     * This is the hard one
     *
     * @param range
     * @param portionTimes
     * @return
     */
    private List<TimePeriod> extractTimePeriods(PortionStatRange range, List<PortionTime>
            portionTimes) {
    }

    /**
     * This should be simple
     *
     * @param timePeriods
     * @param ratings
     * @return
     */
    private double[] extractScoreAndDuration(List<TimePeriod> timePeriods, List<Rating> ratings) {
    }

    //not finished
    private static List<PortionTimesAndRatings> joinToNearPortions(List<PortionTimesAndRatings>
                                                                           portionTimesAndRatingsList, int minHoursBetweenMeals) {
        for (PortionTimesAndRatings ptAndR : portionTimesAndRatingsList) {
            List<PortionTime> portionTimesAfterJoin = joinToNearPortions2(ptAndR.getPortionTimes()
                    , minHoursBetweenMeals);
            ptAndR.setPortionTimes(portionTimesAfterJoin);
        }
        return portionTimesAndRatingsList;
    }

    private static List<PortionTime> joinToNearPortions2(List<PortionTime> portionTimes, int
            minHoursBetweenMeals) {

    }

}

