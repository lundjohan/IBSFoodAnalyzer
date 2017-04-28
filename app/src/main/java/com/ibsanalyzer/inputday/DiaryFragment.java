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

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Divider;
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
    public static int NEW_MEAL = 1000;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    List<Event> eventList = new ArrayList<>();

    //for pinning/ marking events, this must be cleaned when user quits application or app crashes etc
    List<Integer> eventsMarked = new ArrayList<>();
    static final int BACKGROUND_COLOR = Color.BLUE;
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

        //EventModel Buttons, do onClick here so handlers doesnt have to be in parent Activity
        ImageButton mealBtn = (ImageButton) view.findViewById(R.id.mealBtn);
        mealBtn.setOnClickListener(this);
        ImageButton scoreBtn = (ImageButton) view.findViewById(R.id.scoreBtn);
        scoreBtn.setOnClickListener(this);

        //do other buttons here

        //RecyclerView initiation
        //==========================================================================================
        recyclerView = (RecyclerView) view.findViewById(R.id.events_layout);
        layoutManager = new LinearLayoutManager(getActivity());
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == NEW_MEAL) {
                if (data.hasExtra("returnMealJSON")) {
                    String mealJSONData = data.getExtras().getString("returnMealJSON");
                    Gson gson = new Gson();
                    Meal meal = gson.fromJson(mealJSONData, Meal.class);

                    //String text = meal.getTime().getHour() + ":" + meal.getTime().getMinute() + " Portions: " + meal.getPortions() + "Tags: " + meal.getTags().get(0).getName() + " x" + meal.getTags().get(0).getSize();
                    eventList.add(meal);

                    //se https://guides.codepath.com/android/Using-the-RecyclerView#itemanimator för 4 alternativ
                    //här för förtydligande varför notifyDataSetChanged är mer mer ineffektiv: inte https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyDataSetChanged()
                    //item inserted in last position of eventList
                    adapter.notifyItemInserted(eventList.size()-1);
                }
            }

        }
    }

    /*This is needed since onClick otherwise goes to parent Activity*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mealBtn:
                newMealActivity(v);
                break;
            case R.id.scoreBtn:
                newScoreItem(v);
                break;
        }
        //do other buttons here
    }

    public void newMealActivity(View view) {
        Intent intent = new Intent(getActivity(), MealActivity.class);
        startActivityForResult(intent, NEW_MEAL);
    }

    public void newScoreItem(View view) {
        Divider div = new Divider(LocalDateTime.now(), 9.0);
        eventList.add(div);
        adapter.notifyItemInserted(eventList.size()-1);
        Log.d("Debugging", "inuti newScoreItem");
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
        Log.d("Debug","inside fragment, item was clicked");
        if (!markingModeIsOn()){
            editEvent(position);
        }
        else if (eventIsMarked(position)){
            eventsMarked.remove(Integer.valueOf(position)); //remove special case integer
            v.setBackgroundColor(Color.WHITE);
        }
        else {   //markingModeIsOn but eventIsNotMarked
            eventsMarked.add(position);
            v.setBackgroundColor(BACKGROUND_COLOR);
        }
    }



    @Override
    public boolean onItemLongClicked(View v, int position) {
        Log.d("Debug","inside fragment, item was LONG clicked");
        if (!markingModeIsOn()){
            eventsMarked.add(position);
            v.setBackgroundColor(BACKGROUND_COLOR);
        }
        return false;
    }
    private boolean markingModeIsOn(){
        return eventsMarked.size()>0;
    }
    private void editEvent(int position) {
        //TODO
    }
    private boolean eventIsMarked(int position) {
        return eventsMarked.contains(position);
    }
}
