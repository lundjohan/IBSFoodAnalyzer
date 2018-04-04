package com.johanlund.statistics_avg_scorewrapper;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.TagPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2017-06-25.
 * This class is the Strategy in Strategy Pattern
 */

public abstract class AvgScoreWrapper {
    int quantLimit;
    int startHoursAfterEvent;
    int stopHoursAfterEvent;


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

    public abstract Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint>
            tagPoint);

    /**
     * Notice that getScore is coming from inherited methods of this class.
     * @param tagPoints
     * @return
     */
    public List<TagPoint> toSortedList(Map<String, TagPoint> tagPoints) {
        List<TagPoint> toBeSorted = new ArrayList<>( tagPoints.values());

        //without this later sort can crasch
        List<TagPoint> validTPList = removeNaNFromList(toBeSorted);
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
    private List<TagPoint> removeNaNFromList(List<TagPoint> tagPoints) {
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
    public List<TagPoint> removeTagPointsWithTooLowQuant(List<TagPoint> tpList, int limit){
        List<TagPoint>trimmedTpList = new ArrayList<>();
        for (TagPoint tp:tpList){
            if(getQuantityOfTagPoint(tp)>=limit){
                trimmedTpList.add(tp);
            }
        }
        return trimmedTpList;
    }

    /**
     * Statistics counting on Average and BM uses different quanitity parameters (quantity and nrOfBms)
     * @return
     */
    protected abstract double getQuantityOfTagPoint(TagPoint tp);

    //special case here, since quantity for bm and rating doesnt really mean the same thing.
    public abstract int getQuantityLimit();
}

