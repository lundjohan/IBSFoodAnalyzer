package com.ibsanalyzer.inputday;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.ibsanalyzer.adapters.EventAdapter;
import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibsanalyzer.calc_score_classes.AvgScoreWrapper;
import com.ibsanalyzer.calc_score_classes.ScoreWrapper;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import stat_classes.TagPointMaker;

import static com.ibsanalyzer.constants.Constants.AVG_SCORE;
import static com.ibsanalyzer.constants.Constants.BLUE_ZONE_SCORE;
import static com.ibsanalyzer.constants.Constants.BRISTOL_SCORE;
import static com.ibsanalyzer.constants.Constants.COMPLETENESS_SCORE;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_AVG;
import static com.ibsanalyzer.constants.Constants.UPDATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    StatAdapter adapter;
    StatFragmentListener callback;

    //Scores
    Map<String, TagPoint> tagPoints = new HashMap<String, TagPoint>();

    public StatFragment() {
        // Required empty public constructor
    }

    // Container Activity must implement this interface
    public interface StatFragmentListener {
        List<Event> retrieveEvents();

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
        TextView statType = (TextView) view.findViewById(R.id.stattype);
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
                } else if (!isAlreadyRightView(typeOfScore)) {
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
        }
        return type;
    }

    /**
     * This method is called when user has demanded a different stat type to be shown.
     * OR if update is demanded.
     * <p>
     * Notice that new TagPoints should only be created when scorestat is used first time (empty
     * list) or update is pressed
     *
     * @param typeOfScore
     */
    private void changeAndRefreshSetup(int typeOfScore) {
        tagPoints.clear();
        List<Event> events = callback.retrieveEvents();
        List<Chunk> chunks = Chunk.makeChunksFromEvents(events);
        ScoreWrapper scoreWrapper = makeScoreWrapper(typeOfScore);
        tagPoints = scoreWrapper.calcScore(chunks, tagPoints);
        adapter.setScoreWrapper(scoreWrapper);
        adapter.notifyDataSetChanged();
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

    private ScoreWrapper makeScoreWrapper(int typeOfScore) {
        ScoreWrapper scoreWrapper = null;
        switch (typeOfScore) {
            case AVG_SCORE:
                scoreWrapper = new AvgScoreWrapper(HOURS_AHEAD_FOR_AVG);
                break;
            case BLUE_ZONE_SCORE:
                //  scoreWrapper = new BlueScore(HOURS_AHEAD_FOR_BLUEZONES);
                break;
            case COMPLETENESS_SCORE:
                //  scoreWrapper = new CompleteScore(HOURS_AHEAD_FOR_COMPLETE);
                break;
            case BRISTOL_SCORE:
                // scoreWrapper = new BristolScore(HOURS_AHEAD_FOR_BRISTOL);
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


}
