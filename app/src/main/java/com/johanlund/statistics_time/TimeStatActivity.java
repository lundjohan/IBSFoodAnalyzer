package com.johanlund.statistics_time;

import android.os.AsyncTask;

import com.johanlund.base_classes.Chunk;
import com.johanlund.statistics_adapters.AvgStatAdapter;
import com.johanlund.statistics_adapters.TimeStatAdapter;
import com.johanlund.statistics_general.StatBaseActivity;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.statistics_time_scorewrapper.TimeScoreWrapper;

import java.util.List;

/**
 * Created by Johan on 2018-03-17.
 */

public abstract class TimeStatActivity extends StatBaseActivity{

    @Override
    public AvgStatAdapter getStatAdapter() {
        return null;
    }


    @Override
    protected void startAsyncTask(List<Chunk> chunks) {
        TimeStatAsyncTask asyncThread = new TimeStatAsyncTask();
        asyncThread.execute(getScoreWrapper(), chunks);
    }
    public abstract TimeScoreWrapper getScoreWrapper();
    private class TimeStatAsyncTask extends AsyncTask<Object, Void, List<TimePoint>> {
        final String TAG = this.getClass().getName();

        public TimeStatAsyncTask() {
        }

        /**
         * @param params should be in order. (implementation of) AvgScoreWrapper, List<Chunk>,
         *               Map<String, TagPoint>
         * @return
         */
        @Override
        protected List<TimePoint> doInBackground(Object... params) {
            TimeScoreWrapper wrapper = (TimeScoreWrapper) params[0];
            List<Chunk> chunks = (List<Chunk>) params[1];
            List<TimePoint> timePoints= wrapper.calcTimePeriods(chunks);
            //sort timePoints here
            List<TimePoint> sortedList = wrapper.toSortedList(timePoints);

            //remove tagPoints with too low amount of quantity
            return wrapper.removeTimePointsWithTooLowQuant(sortedList);
        }



        @Override
        protected void onPostExecute(List<TimePoint> sortedList) {
            ((TimeStatAdapter)adapter).setTimePointsList(sortedList);
            adapter.notifyDataSetChanged();
            //scrolling to top is needed, otherwise it starts at the bottom.
            recyclerView.scrollToPosition(sortedList.size()-1);
        }
    }
}
