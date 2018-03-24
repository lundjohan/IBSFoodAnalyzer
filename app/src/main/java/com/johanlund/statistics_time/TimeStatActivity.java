package com.johanlund.statistics_time;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.johanlund.base_classes.Chunk;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_adapters.TimeStatAdapter;
import com.johanlund.statistics_general.StatBaseActivity;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.statistics_time_scorewrapper.TimeScoreWrapper;

import org.threeten.bp.LocalDate;

import java.util.List;

import static com.johanlund.constants.Constants.DATE_TO_START_DIARY;

/**
 * Created by Johan on 2018-03-17.
 */

public abstract class TimeStatActivity extends StatBaseActivity implements TimeStatAdapter.TimeStatAdapterUser{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_stat);
        recyclerView = (RecyclerView) findViewById(R.id.time_stat_table);
        continueOnCreate();
    }

    @Override
    public TimeStatAdapter getStatAdapter(){
        return new TimeStatAdapter(getScoreWrapper(), this);
    }
    public abstract TimeScoreWrapper getScoreWrapper();



    @Override
    protected void startAsyncTask(List<Chunk> chunks) {
        TimeStatAsyncTask asyncThread = new TimeStatAsyncTask();
        asyncThread.execute(getScoreWrapper(), chunks);
    }

    @Override
    public void restartDiaryAtDate(LocalDate ld){
        Intent intent = getIntent();
        intent.putExtra(DATE_TO_START_DIARY, ld);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    private class TimeStatAsyncTask extends AsyncTask<Object, Void, List<TimePoint>> {
        public TimeStatAsyncTask() {
        }
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
