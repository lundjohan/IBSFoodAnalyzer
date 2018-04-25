package com.johanlund.statistics_general;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.PointBase;
import com.johanlund.util.TagsWrapperBase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-04-04.
 */

public class StatAsyncTask <E extends PointBase> extends AsyncTask<Object, Void, List<E>> {
    private WeakReference<StatAdapter> adapter;
    private WeakReference<RecyclerView> recyclerView;
    public StatAsyncTask(StatAdapter adapter, RecyclerView recyclerView) {
        this.adapter = new WeakReference(adapter);
        this.recyclerView = new WeakReference(recyclerView);
    }
    @Override
    protected List<E> doInBackground(Object... params) {
        List<E>toReturn = new ArrayList<>();
        if (!isCancelled()) {
            ScoreWrapperBase wrapper = (ScoreWrapperBase) params[0];
            List<TagsWrapperBase> chunks = (List<TagsWrapperBase>) params[1];

            List<E> points = wrapper.calcPoints(chunks);

            //sort points here
            List<E> sortedList = wrapper.toSortedList(points);

            //remove points with too low amount of duration
            toReturn = wrapper.removePointsWithTooLowQuant(sortedList);
        }
        return toReturn;
    }



    @Override
    protected void onPostExecute(List<E> sortedList) {
        StatAdapter<E> sa = adapter.get();
        if (sa != null){
            sa.setPointsList(sortedList);
            sa.notifyDataSetChanged();
        }
        //scrolling to top is needed, otherwise it starts at the bottom.
        RecyclerView rw = recyclerView.get();
        if (rw != null){
            rw.scrollToPosition(sortedList.size()-1);
        }
    }
}

