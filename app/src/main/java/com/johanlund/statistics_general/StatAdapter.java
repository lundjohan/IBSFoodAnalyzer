package com.johanlund.statistics_general;

import android.support.v7.widget.RecyclerView;

import com.johanlund.statistics_point_classes.PointBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-04-04.
 */

public abstract class StatAdapter  <E extends PointBase> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    protected List<E> pointList = new ArrayList<>();
    public void setPointsList(List<E> pointList){
        this.pointList = pointList;
    }
}
