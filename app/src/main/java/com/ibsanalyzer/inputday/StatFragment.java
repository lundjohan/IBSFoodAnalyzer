package com.ibsanalyzer.inputday;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.base_classes.Break;
import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.calc_score_classes.AvgScoreWrapper;
import com.ibsanalyzer.calc_score_classes.BlueScoreWrapper;
import com.ibsanalyzer.calc_score_classes.CompleteScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;
import com.ibsanalyzer.settings.StatSettingsActivity;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ibsanalyzer.base_classes.Event.makeBreaks;
import static com.ibsanalyzer.constants.Constants.AVG_SCORE;
import static com.ibsanalyzer.constants.Constants.BLUE_ZONE_SCORE;
import static com.ibsanalyzer.constants.Constants.BRISTOL_SCORE;
import static com.ibsanalyzer.constants.Constants.COMPLETENESS_SCORE;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_AVG;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BLUEZONES;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BREAK_BACKUP;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BRISTOL;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_COMPLETE;
import static com.ibsanalyzer.constants.Constants.SCORE_BLUEZONES_FROM;
import static com.ibsanalyzer.constants.Constants.SETTINGS;
import static com.ibsanalyzer.constants.Constants.UPDATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment implements View.OnClickListener {
    static final String TAG = "STAT_FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    StatAdapter adapter;
    StatFragmentListener callback;
    TextView statType;

    //Scores
    Map<String, TagPoint> tagPoints = new HashMap<String, TagPoint>();

    public StatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (StatFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat, container, false);
        statType = (TextView) view.findViewById(R.id.stattype);
        statType.setOnClickListener(this);


        recyclerView = (RecyclerView) view.findViewById(R.id.stat_table);
        layoutManager = new LinearLayoutManager((Context) this.callback, LinearLayoutManager
                .VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StatAdapter(tagPoints);
        recyclerView.setAdapter(adapter);
        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        return view;
    }

    public void doScorePopupMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.stat_choose_score_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int typeOfScore = getTypeOfScore(item.getItemId());

                if (typeOfScore == UPDATE) {
                    update(adapter.getTypeOfScore());
                } else if (typeOfScore == SETTINGS) {
                    Intent i = new Intent(getActivity(), StatSettingsActivity.class);
                    ((Activity)callback).startActivity(i);
                } else if (!isAlreadyRightView(typeOfScore)) {
                    changeTypeText(typeOfScore);
                    adapter.setTypeOfScore(typeOfScore);
                    changeSetup(typeOfScore);
                }
                return true;
            }
        });
        popup.show();
    }

    private void update(int typeOfScore) {
        //in case other solution for changeSetup happens this have to change
        try {
            changeAndRefreshSetup(typeOfScore);
        } catch (Exception e) {
            Log.d("Error", "Update could not be performed");
            e.printStackTrace();
        }
    }

    private int getTypeOfScore(int itemId) {
        int type = -1;
        if (itemId == R.id.avgScoreMenuItem) {
            type = AVG_SCORE;
        } else if (itemId == R.id.blueZoneMenuItem) {
            type = BLUE_ZONE_SCORE;
        } else if (itemId == R.id.completeMenuItem) {
            type = COMPLETENESS_SCORE;
        } else if (itemId == R.id.bristolMenuItem) {
            type = BRISTOL_SCORE;
        } else if (itemId == R.id.updateScoreMenuItem) {
            type = UPDATE;
        } else if (itemId == R.id.settingsMenuItem) {
            type = SETTINGS;
        }
        return type;
    }

    private void changeTypeText(int typeOfScore) {
        switch (typeOfScore) {
            case AVG_SCORE:
                statType.setText("Avg Score");
                break;
            case BLUE_ZONE_SCORE:
                statType.setText("Blue Zone Score");
                break;
            case COMPLETENESS_SCORE:
                statType.setText("Completeness Score");
                break;
            case BRISTOL_SCORE:
                statType.setText("Bristol");
                break;
        }
    }

    /**
     * This method is called when user has demanded a different stat type to be shown.
     * OR if update is demanded.
     * <p>
     * Notice that new TagPoints should only be created when scorestat is used first time (empty
     * list) or update is pressed
     *
     * @param typeOfScore uses an AsyncTask to create separate thread where calculations are made.
     *                    all different stat calculations classes extent ScoreWrapper
     */
    private void changeAndRefreshSetup(int typeOfScore) {
        tagPoints.clear();
        List<Event> events = callback.retrieveEvents();
        //insert or remove automatic breaks on events.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int hoursInFrontOfAutoBreak = preferences.getInt("hoursInFrontOfAutoBreak", HOURS_AHEAD_FOR_BREAK_BACKUP);
        Log.d(TAG, "hoursInFrontOfAutoBreak == "+ hoursInFrontOfAutoBreak);

        List<Break> breaks = Event.makeBreaks(events, hoursInFrontOfAutoBreak);
        List<Chunk> chunks = Chunk.makeChunksFromEvents(events, breaks);
        ScoreWrapper scoreWrapper = makeScoreWrapper(typeOfScore);
        //tagPoints = scoreWrapper.calcScore(chunks, tagPoints);
        Log.d(TAG, "Inside Main Thread, before asyncTask");
        StatAsyncTask asyncThread = new StatAsyncTask();
        asyncThread.execute(scoreWrapper, chunks);
        Log.d(TAG, "Inside Main Thread, 'after' asyncTask");

    }

    /**
     * Changes tagPoints to other type.
     * Calls changeAndRefresh if tagPoints turns out to be empty (could be a sign that it never
     * has been initiated for Tagpoint type).
     *
     * @param typeOfScore
     */
    private void changeSetup(int typeOfScore) {
        tagPoints.clear();
        ScoreWrapper scoreWrapper = makeScoreWrapper(typeOfScore);
        adapter.setScoreWrapper(scoreWrapper);
        adapter.notifyDataSetChanged();
    }

    /**
     * Uses SharedPreferences (editable by users in Settings) for algorithms variables.
     * @param typeOfScore
     * @return
     */
    private ScoreWrapper makeScoreWrapper(int typeOfScore) {
        ScoreWrapper scoreWrapper = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switch (typeOfScore) {
            case AVG_SCORE:
                int hours_ahead_for_av = preferences.getInt("avg_hours_ahea",HOURS_AHEAD_FOR_AVG);
                Log.d("Debug", "ahead_for_avg_score, funkar detta så funkar seekbarpreference: "+hours_ahead_for_av);
                scoreWrapper = new AvgScoreWrapper(hours_ahead_for_av);
                break;
            case BLUE_ZONE_SCORE:
                int hours_ahead_for_bluezones = preferences.getInt("hours_ahead_bluezones",HOURS_AHEAD_FOR_BLUEZONES);
                scoreWrapper = new BlueScoreWrapper(Integer.valueOf(hours_ahead_for_bluezones),SCORE_BLUEZONES_FROM);
                break;
            case COMPLETENESS_SCORE:
                int hours_ahead_for_complete = preferences.getInt("hours_ahead_complete",HOURS_AHEAD_FOR_COMPLETE);
                scoreWrapper = new CompleteScoreWrapper(hours_ahead_for_complete);
                break;
            case BRISTOL_SCORE:
                int hours_ahead_for_bristol = preferences.getInt("hours_ahead_bristol",HOURS_AHEAD_FOR_BRISTOL);
                scoreWrapper = new CompleteScoreWrapper(hours_ahead_for_bristol);
                break;
        }
        return scoreWrapper;
    }

    private boolean isAlreadyRightView(int typeOfScoreClicked) {
        return adapter.getTypeOfScore() == typeOfScoreClicked;
    }

    /*This is needed since onClick otherwise goes to parent Activity*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stattype:
                doScorePopupMenu(v);
                break;
        }
    }


    // Container Activity must implement this interface
    public interface StatFragmentListener {
        List<Event> retrieveEvents();
    }

    /**
     * This inner class is responsible for putting calculations of stats in new thread
     * <p>
     * A bit of Spaghetti code (onPostExecute accepts scoreWrapper which seems a little bit odd
     * for example), but it works.
     */
    private class StatAsyncTask extends AsyncTask<Object, Void, ScoreWrapper> {
        final String TAG = this.getClass().getName();

        public StatAsyncTask() {
        }

        /**
         * @param params should be in order. (implementation of) ScoreWrapper, List<Chunk>,
         *               Map<String, TagPoint>
         * @return
         */
        @Override
        protected ScoreWrapper doInBackground(Object... params) {
            Log.d(TAG, "Inside doInBackground");
            ScoreWrapper wrapper = (ScoreWrapper) params[0];
            List<Chunk> chunks = (List<Chunk>) params[1];
            tagPoints = wrapper.calcScore(chunks, tagPoints);
            //sort tagPoints here

            return wrapper;
        }

        @Override
        protected void onPostExecute(ScoreWrapper scoreWrapper) {
            adapter.setScoreWrapper(scoreWrapper);
            adapter.notifyDataSetChanged();
        }
    }


}
