package com.johanlund.statistics_avg_scorewrapper;

import com.johanlund.statistics_point_classes.TagPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.johanlund.stat_classes.TagPointMaker;
import com.johanlund.statistics_avg.TagsWrapper;
import com.johanlund.util.TagsWrapperBase;

/**
 * Created by Johan on 2017-06-25.
 * <p>
 * Statistics algorithms needs TimeZone that needs a device to run on, that's why I have placed
 * this test under AndroidTests
 */

public class RatingAvgScoreWrapper extends AvgScoreWrapper {

    public RatingAvgScoreWrapper(int waitHoursAfterEvent, int stopHoursAfterEvent, int quantLimit) {
        super(waitHoursAfterEvent, stopHoursAfterEvent, quantLimit);
    }

    @Override
    public double getScore(TagPoint tp) {
        return tp.getOrigAvgScore();
    }

    @Override
    public List<TagPoint> calcScore(List<TagsWrapperBase> chunks, Map<String, TagPoint> tagPoints) {
        /*TODO TYPE CONVERSION TO ARRAYLIST => PROBABLY SLOW AND INEFFECTIVE, TRY INSTEAD to use Collection instead of List lower in hierarchy.
         */
        return new ArrayList<>(TagPointMaker.doAvgScore(chunks, startHoursAfterEvent, stopHoursAfterEvent,
                tagPoints).values());
    }

    @Override
    protected double getQuantityOfTagPoint(TagPoint tp) {
        return tp.getQuantity();
    }

    @Override
    public int getQuantityLimit() {
        return quantLimit;
    }


}