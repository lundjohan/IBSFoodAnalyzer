package com.johanlund.stat_backend.portion_scorewrapper;

import com.johanlund.stat_backend.makers.PortionPointMaker;
import com.johanlund.stat_backend.point_classes.PortionPoint;
import com.johanlund.screens.statistics.portions_settings.PortionStatRange;
import com.johanlund.util.TagsWrapperBase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Johan on 2018-04-03.
 *
 * The way Ration Portion Stat works:
 * ==================================
 * NB (it seems to work good, but this algorithm is not tested yet, this should of course be done).
 *
 * Variables coming in from settings:
 *   ranges, waitHoursAfterMeal, validHours, minHoursBetweenMeals.
 * Relevant variables (can be represented in various ways) coming in from database:
 *   portionSizes and there times, Ratings.after and their times, breaks, endTimeOfEachChunk.
 *
 * The algorithm is looped for every range that the user has added.
 *
 * Important to know is that if a bigger portion than the range max is occurring before or during
 * the timeperiod for the portions within the range, the timeperiod ==time scope) of the range will
 * be Excepted with the timeperiod of the larger portion (see method leftExceptRights).
 * This might lead to significant less quant coming out of a portion stat for a range than
 * otherwise would be expected (and that ranges with higher max seem to have longer quant).
 * But there is no way to get around it, larger portion might otherwise interfere and destroy the
 * meaning with this stat.
 *   Smaller portions in the same timeperiod as portions within the range are not,
 *   however, treated as a problem.
 *
 * waitHoursAfterMeal == hours_after_portion_to_start_counting_score_from_rating
 * validHours == hours_after_portion_to_stop_counting_score_from_rating
 *
 *
 *
 */

public class RatingPortionScoreWrapper extends PortionScoreWrapper {

    public RatingPortionScoreWrapper(List<PortionStatRange> ranges, int waitHoursAfterMeal, int
            stopHoursAfterMeal, int minHoursBetweenMeals) {
        super(ranges, waitHoursAfterMeal, stopHoursAfterMeal, minHoursBetweenMeals);
    }

    @Override
    public List<PortionPoint> calcPoints(List<TagsWrapperBase> chunks) {
        return PortionPointMaker.doPortionsPoints(chunks, ranges, waitHoursAfterMeal, stopHoursAfterMeal, minHoursBetweenMeals);
    }
    @Override
    public List<PortionPoint> toSortedList(List<PortionPoint> points) {
        Collections.sort(points, new Comparator<PortionPoint>() {
                    @Override
                    public int compare(PortionPoint p1, PortionPoint p2) {
                        return (int) ((p1.getScore() - p2.getScore())*100);
                    }
                }
        );
        return points;
    }
}