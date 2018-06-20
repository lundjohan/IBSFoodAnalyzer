package com.johanlund.screens.statistics.common;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.johanlund.stat_backend.point_classes.PointBase;
import com.johanlund.stat_backend.stat_util.TagsWrapperBase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-04-04.
 */

public class StatAsyncTask <E extends PointBase> extends AsyncTask<Object, Void, List<E>> {
    // read https://medium.com/google-developer-experts/finally-understanding-how-references-work-in-android-and-java-26a0d9c92f83
    private WeakReference<Activity> activity;
    private WeakReference<StatAdapter> adapter;
    private WeakReference<RecyclerView> recyclerView;

    public StatAsyncTask(Activity activity, StatAdapter adapter, RecyclerView recyclerView) {
        this.activity = new WeakReference<>(activity);
        this.adapter = new WeakReference(adapter);
        this.recyclerView = new WeakReference(recyclerView);
    }
    @Override
    protected List<E> doInBackground(Object... params) {
        List<E>toReturn = new ArrayList<>();
        if (!isCancelled() && activity.get() != null) {
            WeakReference<ScoreWrapperBase> wrapper = new WeakReference(params[0]);
            WeakReference<List<TagsWrapperBase>> chunks = new WeakReference(params[1]);

            List<E> points = wrapper.get().calcPoints(chunks.get());

            //sort points here
            List<E> sortedList = wrapper.get().toSortedList(points);

            //remove points with too low amount of duration
            toReturn = wrapper.get().removePointsWithTooLowQuant(sortedList);
        }
        return toReturn;
    }



    @Override
    protected void onPostExecute(List<E> sortedList) {
        if (adapter.get() != null){
            adapter.get().setPointsList(sortedList);
            adapter.get().notifyDataSetChanged();
        }
        //scrolling to top is needed, otherwise it starts at the bottom.
        if (recyclerView.get() != null){
            recyclerView.get().scrollToPosition(sortedList.size()-1);
        }
    }
}

