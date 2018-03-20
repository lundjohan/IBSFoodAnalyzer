package com.johanlund.statistics_avg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.johanlund.statistics_adapters.AvgStatAdapter;
import com.johanlund.base_classes.Chunk;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_general.StatBaseActivity;
import com.johanlund.statistics_avg_scorewrapper.AvgScoreWrapper;
import com.johanlund.statistics_point_classes.TagPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AvgStatActivity extends StatBaseActivity {
    static final String TAG = "STAT_ACTIVITY";

    //Scores
    Map<String, TagPoint> tagPoints = new HashMap<String, TagPoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_stat);
        recyclerView = (RecyclerView) findViewById(R.id.avg_stat_table);
        continueOnCreate();
    }
    @Override
    protected void startAsyncTask(List<Chunk>chunks){
        AvgStatAsyncTask asyncThread = new AvgStatAsyncTask();
        asyncThread.execute(getScoreWrapper(), chunks);
    }

    public abstract AvgScoreWrapper getScoreWrapper();



    /**
     * This inner class is responsible for putting calculations of stats in new thread
     * <p>
     * A bit of Spaghetti code (onPostExecute accepts avgScoreWrapper which seems a little bit odd
     * for example), but it works.
     */
    private class AvgStatAsyncTask extends AsyncTask<Object, Void, List<TagPoint>> {
        final String TAG = this.getClass().getName();

        public AvgStatAsyncTask() {
        }

        /**
         * @param params should be in order. (implementation of) AvgScoreWrapper, List<Chunk>,
         *               Map<String, TagPoint>
         * @return
         */
        @Override
        protected List<TagPoint> doInBackground(Object... params) {
            AvgScoreWrapper wrapper = (AvgScoreWrapper) params[0];
            List<Chunk> chunks = (List<Chunk>) params[1];
            tagPoints = wrapper.calcScore(chunks, tagPoints);
            //sort tagPoints here
            List<TagPoint> sortedList = wrapper.toSortedList(tagPoints);

            //remove tagPoints with too low amount of quantity
            return wrapper.removeTagPointsWithTooLowQuant(sortedList, wrapper.getQuantityLimit());
        }



        @Override
        protected void onPostExecute(List<TagPoint> sortedList) {
            ((AvgStatAdapter)adapter).setTagPointsList(sortedList);
            adapter.notifyDataSetChanged();
            //scrolling to top is needed, otherwise it starts at the bottom.
            recyclerView.scrollToPosition(sortedList.size()-1);
        }
    }
}