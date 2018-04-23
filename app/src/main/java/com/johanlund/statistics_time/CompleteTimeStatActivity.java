package com.johanlund.statistics_time;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;

import com.johanlund.base_classes.Break;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_adapters.TimeStatAdapter;
import com.johanlund.statistics_general.StatAdapter;
import com.johanlund.statistics_time_scorewrapper.CompleteTimeScoreWrapper;
import com.johanlund.statistics_time_scorewrapper.TimeScoreWrapper;
import com.johanlund.util.CompleteTime;

import org.threeten.bp.LocalDateTime;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.johanlund.constants.Constants.HOURS_AHEAD_FOR_BREAK_BACKUP;

/**
 * Created by Johan on 2018-03-13.
 */

public class CompleteTimeStatActivity extends TimeStatActivity  {
    @Override
    protected String getInfoStr() {
        return "Complete Time Stat helps you go to the bowel movement periods of a certain score interval. ";
    }

    @Override
    public String getStringForTitle() {
        return "Completeness Time Stat";
    }


    @Override
    public TimeScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int ratingStart = preferences.getInt(getResources().getString(R.string.time_complete_start),4);
        int ratingEnd = preferences.getInt(getResources().getString(R.string.time_complete_end), 5);
        return new CompleteTimeScoreWrapper(ratingStart,ratingEnd);
    }
    @Override
    protected void calculateStats() {
        //get events from database
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        List<LocalDateTime> breaks = dbHandler.getManualBreaks();
        List<CompleteTime> cts = dbHandler.getCompleteTimes();

        //insert or remove automatic breaks on events.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext()
                );
        int hoursInFrontOfAutoBreak = preferences.getInt("hours_break",
                HOURS_AHEAD_FOR_BREAK_BACKUP);

        //List<Break> breaks = Break.makeAllBreaks(events, hoursInFrontOfAutoBreak);
        List<LocalDateTime> allBreaks = Break.makeAllBreaks(cts, breaks, hoursInFrontOfAutoBreak);
        List<List<CompleteTime>> dividedCts = Break.divideTimes(cts, allBreaks);
        CompleteStatAsyncTask asyncThread = new CompleteStatAsyncTask(adapter, recyclerView);
        asyncThread.execute(getScoreWrapper(), dividedCts);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    private static class CompleteStatAsyncTask <TimePoint> extends AsyncTask<Object, Void, List<TimePoint>> {
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
                List<List<CompleteTime>> dividedCts = (List<List<CompleteTime>> ) params[1];

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

}
