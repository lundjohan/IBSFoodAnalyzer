package com.ibsanalyzer.inputday;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibsanalyzer.adapters.StatAdapter;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;

import static com.ibsanalyzer.inputday.DiaryFragment.BACKGROUND_COLOR;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment implements View.OnClickListener{


    public StatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat, container, false);
        TextView statType = (TextView)view.findViewById(R.id.stattype);
        statType.setOnClickListener(this);
        return view;
    }

    public void doScorePopupMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.stat_choose_score_menu, popup.getMenu());
        Menu menu = popup.getMenu();
        MenuItem avgItem = menu.findItem(R.id.avgScoreMenuItem);
        MenuItem blueZoneItem = menu.findItem(R.id.blueZoneMenuItem);
        MenuItem completeBmItem = menu.findItem(R.id.completeMenuItem);
        MenuItem bristolItem = menu.findItem(R.id.bristolMenuItem);
        MenuItem updateItem = menu.findItem(R.id.updateScoreMenuItem);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int clickedItemId = item.getItemId();
                if (isAlreadyRightView(clickedItemId)){
                    return true;
                }

                if (clickedItemId == R.id.avgScoreMenuItem) {
                    changeToAvgScoreSetUp();
                }
                else if (clickedItemId == R.id.blueZoneMenuItem) {
                    changeToAvgBlueZoneSetUp();
                }
                else if (clickedItemId == R.id.completeMenuItem) {
                    changeToCompleteSetUp();
                }
                else if (clickedItemId == R.id.bristolMenuItem) {
                    changeToBristolSetUp();
                }
                else if (clickedItemId == R.id.updateScoreMenuItem) {
                    updateScoreForStat();
                }
                return true;
            }
        });
        popup.show();
    }

    private void changeToAvgScoreSetUp() {
    }
    private void changeToAvgBlueZoneSetUp() {
    }
    private void changeToCompleteSetUp() {
    }
    private void changeToBristolSetUp() {
    }
    private void updateScoreForStat() {
    }
    private boolean isAlreadyRightView(int clickedItemId) {
        return true;
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
