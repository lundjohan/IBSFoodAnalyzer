package com.johanlund.statistics_general;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.PointBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Johan on 2018-04-03.
 * Generic abstract class
 */

public abstract class ScoreWrapperBase <E extends PointBase>{

    /**
     * Given: breaks have already been accounted for.
     * @param chunks
     * @return
     */
    public abstract List<E> calcPoints(List<Chunk> chunks);

    public abstract List<E> toSortedList(List<E> points);

    public List<E> removePointsWithTooLowQuant(List<E> sortedList){
        List<E>trimmedTimePointList = new ArrayList<>();
        for (E p:sortedList){
            if(quantIsOverLimit(p)){
                trimmedTimePointList.add(p);
            }
        }
        return trimmedTimePointList;
    }

    /**
     * Return false, in case it is not implemented.
     * @param point
     * @return
     */
    protected abstract boolean quantIsOverLimit(E point);

}
