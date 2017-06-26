package com.ibsanalyzer.inputday;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Event;

import java.util.List;

import com.ibsanalyzer.calc_score_classes.AvgScore;
import com.ibsanalyzer.calc_score_classes.CalcScore;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import stat_classes.TagPointHandler;

import static com.ibsanalyzer.constants.Constants.AVG_SCORE;
import static com.ibsanalyzer.constants.Constants.BLUE_ZONE_SCORE;
import static com.ibsanalyzer.constants.Constants.BRISTOL_SCORE;
import static com.ibsanalyzer.constants.Constants.COMPLETENESS_SCORE;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_AVG;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BLUEZONES;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_BRISTOL;
import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_COMPLETE;
import static com.ibsanalyzer.constants.Constants.UPDATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment implements View.OnClickListener {
    List<TagPoint> tagPoints;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    StatAdapter adapter;
    StatFragmentListener callback;

    //Scores
    List<TagPoint> avgScoreTPs;
    List<TagPoint> blueZonesScoreTPs;
    List<TagPoint> completeScoreTPs;
    List<TagPoint> bristolScoreTPs;


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
            Log.d("Error","Update could not be performed");
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
    private void changeAndRefreshSetup(int typeOfScore){
        tagPoints.clear();
        List<Event> events = callback.retrieveEvents();
        List<Chunk> chunks = Chunk.makeChunksFromEvents(events);
        CalcScore calcScore = null;

        //here create new tagPoints
        switch (typeOfScore) {
            case AVG_SCORE:
                calcScore = new AvgScore(HOURS_AHEAD_FOR_AVG);
                break;
            case BLUE_ZONE_SCORE:
                calcScore = new BlueScore(HOURS_AHEAD_FOR_BLUEZONES);
                break;
            case COMPLETENESS_SCORE:
                calcScore = new CompleteScore(HOURS_AHEAD_FOR_COMPLETE);
                break;
            case BRISTOL_SCORE:
                calcScore = new BristolScore(HOURS_AHEAD_FOR_BRISTOL);
                break;
        }
        tagPoints = TagPointHandler.retrieveTagPoints(chunks, calcScore);
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

        //here create new tagPoints
        switch (typeOfScore) {
            case AVG_SCORE:
                tagPoints = avgScoreTPs;
                break;
            case BLUE_ZONE_SCORE:
                tagPoints = blueZonesScoreTPs;
                break;
            case COMPLETENESS_SCORE:
                tagPoints = completeScoreTPs;
                break;
            case BRISTOL_SCORE:
                tagPoints = bristolScoreTPs;
                break;
        }
        if (tagPoints.isEmpty()) {
            changeAndRefreshSetup(typeOfScore);
        } else {
            adapter.notifyDataSetChanged();
        }
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
