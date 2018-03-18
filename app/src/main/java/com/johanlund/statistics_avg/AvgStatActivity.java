package com.johanlund.statistics_avg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.johanlund.adapters.AvgStatAdapter;
import com.johanlund.base_classes.Chunk;
import com.johanlund.calc_score_classes.ScoreWrapper;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics.StatBaseActivity;
import com.johanlund.statistics_point_classes.TagPoint;

import java.util.ArrayList;
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
        setContentView(R.layout.activity_stat);
        recyclerView = (RecyclerView) findViewById(R.id.stat_table);
        continueOnCreate();
    }
    @Override
    protected void startAsyncTask(List<Chunk>chunks){
        StatAsyncTask asyncThread = new StatAsyncTask();
        asyncThread.execute(getScoreWrapper(), chunks);
    }

    public abstract ScoreWrapper getScoreWrapper();



    /**
     * This inner class is responsible for putting calculations of stats in new thread
     * <p>
     * A bit of Spaghetti code (onPostExecute accepts scoreWrapper which seems a little bit odd
     * for example), but it works.
     */
    private class StatAsyncTask extends AsyncTask<Object, Void, List<TagPoint>> {
        final String TAG = this.getClass().getName();

        public StatAsyncTask() {
        }

        /**
         * @param params should be in order. (implementation of) ScoreWrapper, List<Chunk>,
         *               Map<String, TagPoint>
         * @return
         */
        @Override
        protected List<TagPoint> doInBackground(Object... params) {
            Log.d(TAG, "Inside doInBackground");
            ScoreWrapper wrapper = (ScoreWrapper) params[0];
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