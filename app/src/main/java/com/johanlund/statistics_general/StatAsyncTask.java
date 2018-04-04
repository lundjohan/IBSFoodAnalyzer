package com.johanlund.statistics_general;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_point_classes.PointBase;

import java.util.List;

import static com.johanlund.ibsfoodanalyzer.R.id.recyclerView;

/**
 * Created by Johan on 2018-04-04.
 */

public class StatAsyncTask <E extends PointBase> extends AsyncTask<Object, Void, List<E>> {
    private StatAdapter adapter;
    private RecyclerView recyclerView;
    public StatAsyncTask(StatAdapter adapter, RecyclerView recyclerView) {
        this.adapter = adapter;
        this.recyclerView = recyclerView;
    }
    @Override
    protected List<E> doInBackground(Object... params) {
        ScoreWrapperBase<E> wrapper = (ScoreWrapperBase) params[0];
        List<Chunk> chunks = (List<Chunk>) params[1];
        List<E> points= wrapper.calcPoints(chunks);

        //sort points here
        List<E> sortedList = wrapper.toSortedList(points);

        //remove points with too low amount of quantity
        return wrapper.removePointsWithTooLowQuant(sortedList);
    }



    @Override
    protected void onPostExecute(List<E> sortedList) {
        adapter.setPointsList(sortedList);
        adapter.notifyDataSetChanged();
        //scrolling to top is needed, otherwise it starts at the bottom.
        recyclerView.scrollToPosition(sortedList.size()-1);
    }
}

