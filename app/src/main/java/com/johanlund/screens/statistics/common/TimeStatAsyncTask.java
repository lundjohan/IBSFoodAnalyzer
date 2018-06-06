package com.johanlund.screens.statistics.common;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.johanlund.screens.statistics.time.common.TimeStatAdapter;
import com.johanlund.stat_backend.time_scorewrapper.TimeScoreWrapper;
import com.johanlund.util.ScoreTimesBase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TimeStatAsyncTask<TimePoint> extends AsyncTask<Object, Void, List<TimePoint>> {
    private WeakReference<TimeStatAdapter> adapter;
    private WeakReference<RecyclerView> recyclerView;
public TimeStatAsyncTask(StatAdapter adapter, RecyclerView recyclerView) {
        this.adapter = new WeakReference(adapter);
        this.recyclerView = new WeakReference(recyclerView);
    }
    @Override
    protected List<TimePoint> doInBackground(Object... params) {
        List<TimePoint>toReturn = new ArrayList<>();
        if (!isCancelled()) {
            TimeScoreWrapper wrapper = (TimeScoreWrapper) params[0];
            List<ScoreTimesBase> stb = (List<ScoreTimesBase>) params[1];

            List<com.johanlund.stat_backend.point_classes.TimePoint> points = wrapper.calcTimePoints(stb);

            //sort points here
            List<TimePoint> sortedList = wrapper.toSortedList((List) points);

            //remove points with too low amount of duration
            toReturn = sortedList;
           // toReturn = wrapper.removePointsWithTooLowQuant(sortedList);
        }
        return toReturn;
    }



    @Override
    protected void onPostExecute(List<TimePoint> sortedList) {
        TimeStatAdapter sa = adapter.get();
        if (sa != null){
            sa.setTimePointsList((List<com.johanlund.stat_backend.point_classes.TimePoint>) sortedList);
            sa.notifyDataSetChanged();
        }
        //scrolling to top is needed, otherwise it starts at the bottom.
        RecyclerView rw = recyclerView.get();
        if (rw != null){
            rw.scrollToPosition(sortedList.size()-1);
        }
    }
}
