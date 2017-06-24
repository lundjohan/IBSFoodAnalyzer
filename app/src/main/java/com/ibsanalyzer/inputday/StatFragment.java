package com.ibsanalyzer.inputday;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.constants.Constants;

import java.util.List;

import com.ibsanalyzer.tagpoint_classes.TagPoint;

import stat_classes.TagPointHandler;

import static com.ibsanalyzer.constants.Constants.AVG_SCORE;
import static com.ibsanalyzer.constants.Constants.BLUE_ZONE_SCORE;
import static com.ibsanalyzer.constants.Constants.BRISTOL_SCORE;
import static com.ibsanalyzer.constants.Constants.COMPLETENESS_SCORE;
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
                if (typeOfScore == UPDATE){
                    update(adapter.getTypeOfScore());
                }
                else if (!isAlreadyRightView(typeOfScore)) {
                    changeSetup(typeOfScore);
                }
                return true;
            }
        });
        popup.show();
    }

    private void update(int typeOfScore) {
        //in case other solution for changeSetup happens this have to change
        changeSetup(typeOfScore);
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
     * @param typeOfScore
     */
    private void changeSetup(int typeOfScore){
        tagPoints.clear();
        List<Event> events = callback.retrieveEvents();
        List<Chunk>chunks = Chunk.makeChunksFromEvents(events);
        //here create new tagPoints
        if (typeOfScore == AVG_SCORE) {
            tagPoints = TagPointHandler.retrieveAvgScoreTP(chunks, Constants.HOURS);
        } else if (typeOfScore == BLUE_ZONE_SCORE) {
            tagPoints = TagPointHandler.retrieveBlueZoneScoreTP(chunks, Constants.SCORE_ABOVE_ARE_BLUEZONES, Constants.BUFFERT_HOURS_BLUEZONES);
        } else if (typeOfScore == COMPLETENESS_SCORE) {
            tagPoints = TagPointHandler.retrieveCompleteScoreTP(chunks, HOURS_AHEAD_FOR_BM);
        } else if (typeOfScore == BRISTOL_SCORE) {
            tagPoints = TagPointHandler.retrieveBristolScoreTP(chunks, HOURS_AHEAD_FOR_BRISTOL);
        }
        adapter.notifyDataSetChanged();
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
