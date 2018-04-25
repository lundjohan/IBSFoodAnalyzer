package com.johanlund.statistics_avg_scorewrapper;

import com.johanlund.statistics_general.ScoreWrapperBase;
import com.johanlund.statistics_point_classes.TagPoint;
import com.johanlund.statistics_avg.TagsWrapper;
import com.johanlund.util.TagsWrapperBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2017-06-25.
 * This class is the Strategy in Strategy Pattern
 */

public abstract class AvgScoreWrapper extends ScoreWrapperBase<TagPoint> {
    int quantLimit;
    int startHoursAfterEvent;
    int stopHoursAfterEvent;

    Map<String, TagPoint> tagPoints = new HashMap<>();

    /**
     *
     * @param startHoursAfterEvent
     * @param stopHoursAfterEvent
     * @param quantLimit
     */
    public AvgScoreWrapper(int startHoursAfterEvent, int stopHoursAfterEvent, int quantLimit) {
        this.startHoursAfterEvent = startHoursAfterEvent;
        this.stopHoursAfterEvent = stopHoursAfterEvent;
        this.quantLimit = quantLimit;
    }

    public abstract double getScore(TagPoint tp);

    public abstract List<TagPoint> calcScore(List<TagsWrapperBase> chunks, Map<String, TagPoint>
            tagPoint);

    /**
     * Given: breaks have already been accounted for.
     * @param chunks
     * @return
     */
    @Override
    public List<TagPoint> calcPoints(List<TagsWrapperBase> chunks) {

        /*TODO TYPE CONVERSION TO ARRAYLIST => PROBABLY SLOW AND INEFFECTIVE, TRY INSTEAD to use Collection instead of List lower in hierarchy.

         */
        return calcScore(chunks, tagPoints);
        //return new ArrayList<>(calcScore(chunks, tagPoints).values());
    }
    @Override
    public List<TagPoint> toSortedList(List<TagPoint> toBeSorted) {
        //without this filter, later sort can crasch
        List<TagPoint> validTPList = removeNaNFromList(toBeSorted);

        //Sort actually requires List as parameter!
        Collections.sort(validTPList, new Comparator<TagPoint>()
                {
                    @Override
                    public int compare(TagPoint t1, TagPoint t2)
                    {
                        return (int)((getScore(t1)- getScore(t2))*100);
                    }
                }
        );
        return validTPList;
    }

    /**
     * Removes TagPoint that shows a  getScore of "NaN" (can be becouse of 0.0/0.0 for example).
     * @param tagPoints
     * @return
     */
    private List<TagPoint> removeNaNFromList(Collection<TagPoint> tagPoints) {
        List<TagPoint>tagPointsToReturn = new ArrayList<>();
        for (TagPoint tp: tagPoints){
            if (!Double.isNaN(getScore(tp))){
                tagPointsToReturn.add(tp);
            }
        }
        return tagPointsToReturn;
    }

    /**
     * Sometimes you want to avoid to list stat for tags that only occurred once or three times i diary.
     * @param tpList
     * @param limit inclusive
     * @return
     */
    public List<TagPoint> removePointsWithTooLowQuant(List<TagPoint> tpList, int limit){
        List<TagPoint>trimmedTpList = new ArrayList<>();
        for (TagPoint tp:tpList){
            if(getQuantityOfTagPoint(tp)>=limit){
                trimmedTpList.add(tp);
            }
        }
        return trimmedTpList;
    }

    /**
     * Statistics counting on Average and BM uses different quanitity parameters (duration and nrOfBms)
     * @return
     */
    protected abstract double getQuantityOfTagPoint(TagPoint tp);

    //special case here, since duration for bm and rating doesnt really mean the same thing.
    public abstract int getQuantityLimit();

    @Override
    protected boolean quantIsOverLimit(TagPoint point) {
        return getQuantityOfTagPoint(point)>=getQuantityLimit();
    }
}

