package com.johanlund.statistics_general;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.johanlund.statistics_adapters.TimeStatAdapter;
import com.johanlund.statistics_time_scorewrapper.CompleteTimeScoreWrapper;
import com.johanlund.util.ScoreTime;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CompleteStatAsyncTask<TimePoint> extends AsyncTask<Object, Void, List<TimePoint>> {
    private WeakReference<TimeStatAdapter> adapter;
    private WeakReference<RecyclerView> recyclerView;
public CompleteStatAsyncTask(StatAdapter adapter, RecyclerView recyclerView) {
        this.adapter = new WeakReference(adapter);
        this.recyclerView = new WeakReference(recyclerView);
    }
    @Override
    protected List<TimePoint> doInBackground(Object... params) {
        List<TimePoint>toReturn = new ArrayList<>();
        if (!isCancelled()) {
            CompleteTimeScoreWrapper wrapper = (CompleteTimeScoreWrapper) params[0];
            List<List<ScoreTime>> dividedCts = (List<List<ScoreTime>> ) params[1];

            List<TimePoint> points = wrapper.calcTimePoints(dividedCts);

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
            sa.setTimePointsList((List<com.johanlund.statistics_point_classes.TimePoint>) sortedList);
            sa.notifyDataSetChanged();
        }
        //scrolling to top is needed, otherwise it starts at the bottom.
        RecyclerView rw = recyclerView.get();
        if (rw != null){
            rw.scrollToPosition(sortedList.size()-1);
        }
    }
}
