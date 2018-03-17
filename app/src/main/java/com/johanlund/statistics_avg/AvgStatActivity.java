package com.johanlund.statistics_avg;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.johanlund.adapters.StatAdapter;
import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Event;
import com.johanlund.calc_score_classes.ScoreWrapper;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.info.InfoActivity;
import com.johanlund.statistics.StatBaseActivity;
import com.johanlund.tagpoint_classes.TagPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.johanlund.constants.Constants.HOURS_AHEAD_FOR_BREAK_BACKUP;

public abstract class AvgStatActivity extends StatBaseActivity {
    static final String TAG = "STAT_ACTIVITY";
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    StatAdapter adapter;

    //Scores
    Map<String, TagPoint> tagPoints = new HashMap<String, TagPoint>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info, menu);
        menu.findItem(R.id.menu_info).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                infoClicked();
                return true;
            }
        });
        return true;
    }

    private void infoClicked(){
        InfoActivity.newInfoActivity(this, getInfoStr());
    }
    protected abstract String getInfoStr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        recyclerView = (RecyclerView) findViewById(R.id.stat_table);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = getStatAdapter();
        recyclerView.setAdapter(adapter);
        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        getSupportActionBar().setTitle(getStringForTitle());

        calculateStats();


    }
    private void calculateStats() {
        //get events from database
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        List<Event> events = dbHandler.getAllEventsMinusEventsTemplateSorted();
        //insert or remove automatic breaks on events.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext()
        );
        int hoursInFrontOfAutoBreak = preferences.getInt("hours_break",
                HOURS_AHEAD_FOR_BREAK_BACKUP);
        Log.d(TAG, "hoursInFrontOfAutoBreak == " + hoursInFrontOfAutoBreak);
        List<Break> breaks = Event.makeBreaks(events, hoursInFrontOfAutoBreak);
        List<Chunk> chunks = Chunk.makeChunksFromEvents(events, breaks);
        StatAsyncTask asyncThread = new StatAsyncTask();
        asyncThread.execute(getScoreWrapper(), chunks);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //in case API<21 onBackPressed is not called
    //this is blocking natural behavoiur of backbutton
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public abstract ScoreWrapper getScoreWrapper();
    public abstract StatAdapter getStatAdapter();
    public abstract String getStringForTitle();


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
            adapter.setTagPointsList(sortedList);
            adapter.notifyDataSetChanged();
            //scrolling to top is needed, otherwise it starts at the bottom.
            recyclerView.scrollToPosition(sortedList.size()-1);
        }
    }
}