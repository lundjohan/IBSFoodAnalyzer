package com.ibsanalyzer.inputday;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.BM;
import com.ibsanalyzer.base_classes.Score;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.meal.MealActivity;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment implements View.OnClickListener, EventAdapter.OnItemClickListener, EventAdapter.OnItemLongClickListener {
    public static final int NEW_MEAL = 1000;
    public static final int NEW_BM = 1003;
    public static final int NEW_SCORE = 1004;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    List<Event> eventList = new ArrayList<>();

    //for pinning/ marking events, this must be cleaned when user quits application or app crashes etc
    List<Integer> eventsMarked = new ArrayList<>();
    static final int BACKGROUND_COLOR = Color.BLUE;
    MainActivity parentActivity;
    ViewSwitcher tabsLayoutSwitcher;

    public DiaryFragment() {
        // Required empty public constructor
    }

    //p. 121 Android Essentials
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //    outState.putParcelableArrayList("eventList", new ArrayList<Event>(eventList));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        parentActivity = (MainActivity) getActivity();


        super.onCreate(savedInstanceState);


        if (savedInstanceState == null || !savedInstanceState.containsKey("eventList")) {
            //populate array, this will be added to when button is pressed
            //===================================================================
            LocalDateTime ldt = LocalDateTime.of(2016, Month.APRIL, 3, 16, 10);
            Tag t1 = new Tag(ldt, "milk", 2);
            Tag t2 = new Tag(ldt, "yoghurt", 1);
            List<Tag> tagList = new ArrayList<>();
            tagList.add(t1);
            tagList.add(t2);
            Meal meal1 = new Meal(ldt, tagList, 2.);
            Meal meal2 = new Meal(ldt, tagList, 1.);

            eventList.add(meal1);
            eventList.add(meal2);
            //=====================================================
        } else { //behövs denna eller räcker det med onRestoreInstanceState?
            //   eventList = savedInstanceState.getParcelableArrayList("eventList");
        }

        //starts as invisible appBarLayout but when user marks something this pops up
        tabsLayoutSwitcher = (ViewSwitcher) parentActivity.findViewById(R.id.tabLayoutSwitcher);

        //EventModel Buttons, do onClick here so handlers doesnt have to be in parent Activity
        ImageButton mealBtn = (ImageButton) view.findViewById(R.id.mealBtn);
        mealBtn.setOnClickListener(this);
        ImageButton bmBtn = (ImageButton) view.findViewById(R.id.bmBtn);
        bmBtn.setOnClickListener(this);
        ImageButton scoreBtn = (ImageButton) view.findViewById(R.id.scoreBtn);
        scoreBtn.setOnClickListener(this);

        //do other buttons here

        //RecyclerView initiation
        //==========================================================================================
        recyclerView = (RecyclerView) view.findViewById(R.id.events_layout);
        layoutManager = new LinearLayoutManager(parentActivity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(adapter);
        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        //==========================================================================================
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //common for all
        Gson gson = new Gson();
        Event event = null;

        switch (requestCode) {

            case NEW_MEAL:
                if (data.hasExtra("returnMealJSON")) {
                    String mealJSONData = data.getExtras().getString("returnMealJSON");
                    event = gson.fromJson(mealJSONData, Meal.class);

                }
                break;
            case NEW_BM:
                Log.d("Debug", "inuti DiaryFragment. OnactivityResult for NEW_BM");
                if (data.hasExtra("returnBmJSON")) {
                    String bmJSONData = data.getExtras().getString("returnBmJSON");
                    event = gson.fromJson(bmJSONData, BM.class);
                }
                break;
            case NEW_SCORE:
                if (data.hasExtra("returnScoreJSON")) {
                    String scoreJSONData = data.getExtras().getString("returnScoreJSON");
                    event = gson.fromJson(scoreJSONData, Score.class);
                }
                break;
        }
        eventList.add(event);

        //se https://guides.codepath.com/android/Using-the-RecyclerView#itemanimator för 4 alternativ
        //här för förtydligande varför notifyDataSetChanged är mer mer ineffektiv: inte https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyDataSetChanged()
        //item inserted in last position of eventList
        adapter.notifyItemInserted(eventList.size() - 1); //OBS! Simplistic!
    }

    /*This is needed since onClick otherwise goes to parent Activity*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mealBtn:
                newMealActivity(v);
                break;
            case R.id.bmBtn:
                newBmActivity(v);
                break;
            case R.id.scoreBtn:
                newScoreItem(v);
                break;
        }
        //do other buttons here
    }



    public void newMealActivity(View view) {
        Intent intent = new Intent(parentActivity, MealActivity.class);
        startActivityForResult(intent, NEW_MEAL);
    }
    private void newBmActivity(View v) {
        Intent intent = new Intent(parentActivity, BmActivity.class);
        startActivityForResult(intent, NEW_BM);
    }

    public void newScoreItem(View view) {
        Intent intent = new Intent(parentActivity, ScoreActivity.class);
        startActivityForResult(intent, NEW_SCORE);
    }

    /*
    see http://stackoverflow.com/questions/27945078/onlongitemclick-in-recyclerview
    These are for clicks on items in RecyclerView
     */

    /*
    Obs krasch om man klickar för snabbt, i alla fall vid adapter.notifyItemRemoved!
     */
    @Override
    public void onItemClicked(View v, int position) {
        Log.d("Debug", "inside fragment, item was clicked");
        if (!markingModeIsOn()) {
            editEvent(position);
        } else {
            clickHelper(v, position);
        }
    }


    @Override
    public boolean onItemLongClicked(View v, int position) {
        if (!markingModeIsOn()) {
            eventsMarked.add(position);
            v.setBackgroundColor(BACKGROUND_COLOR);
            changeToMarkedMenu();
        } else {
            clickHelper(v, position);
        }
        return false;
    }

    //same actions for short and long clicks
    // given: markingModeIsOn
    private void clickHelper(View v, int position) {
        if (eventIsMarked(position)) {
            eventsMarked.remove(Integer.valueOf(position)); //remove special case integer
            v.setBackgroundColor(Color.WHITE);

            //if last item now is unmarked, then change back menu.
            if (!markingModeIsOn()) {
                changeToTabbedMenu();
            }
        } else {   //markingModeIsOn but eventIsNotMarked
            eventsMarked.add(position);
            v.setBackgroundColor(BACKGROUND_COLOR);
        }
    }

    /*
        When user markes list items for template or copying
     */
    private void changeToMarkedMenu() {
        tabsLayoutSwitcher.showNext();
        //parentActivity.viewPager do something here to stop swipe
    }

    /*
        When user unmarkes list items
     */
    private void changeToTabbedMenu() {
        tabsLayoutSwitcher.showNext();

    }

    private boolean markingModeIsOn() {
        return eventsMarked.size() > 0;
    }

    private void editEvent(int position) {
        //TODO
    }

    private boolean eventIsMarked(int position) {
        return eventsMarked.contains(position);
    }
}
