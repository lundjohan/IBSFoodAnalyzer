package com.ibsanalyzer.inputday;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.BM;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ibsanalyzer.constants.Constants.LISTENER_AS_ARG;
import static com.ibsanalyzer.constants.Constants.RETURN_BM_JSON;
import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_JSON;
import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_JSON;
import static com.ibsanalyzer.constants.Constants.RETURN_OTHER_JSON;
import static com.ibsanalyzer.constants.Constants.RETURN_RATING_JSON;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryFragment extends Fragment implements View.OnClickListener, EventAdapter.OnItemClickListener, EventAdapter.OnItemLongClickListener {
    public static final int NEW_MEAL = 1000;
    public static final int NEW_OTHER = 1001;
    public static final int NEW_EXERCISE = 1002;
    public static final int NEW_BM = 1003;
    public static final int NEW_SCORE = 1004;


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    List<Event> eventList = new ArrayList<>();

    //for pinning/ marking events, this must be cleaned when user quits application or app crashes etc
    List<Integer> eventsMarked = new ArrayList<>();
    static final int BACKGROUND_COLOR = Color.YELLOW;

    //switcher tab and it's tabs
    ViewSwitcher tabsLayoutSwitcher;
    TabItem toTemplateTab;

//==================================================================================================
    //as recommended for communication between Fragment to Activity.
    //https://developer.android.com/training/basics/fragments/communicating.html
//==================================================================================================

    DiaryFragmentListener callback;
    // Container Activity must implement this interface
    public interface DiaryFragmentListener {
        ViewSwitcher getTabsLayoutSwitcher();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (DiaryFragmentListener) context;

        Bundle args = getArguments();
        setHasOptionsMenu(true);
    }
//==================================================================================================

    public DiaryFragment() {

        Bundle b = getArguments();

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
            populateList();


            //=====================================================
        } else { //behövs denna eller räcker det med onRestoreInstanceState?
            //   eventList = savedInstanceState.getParcelableArrayList("eventList");
        }

        //starts as invisible appBarLayout but when user marks something this pops up
        tabsLayoutSwitcher = (ViewSwitcher) callback.getTabsLayoutSwitcher();
        setUpEventButtons(view);
        initiateRecyclerView(view);

        setUpMenu();
        return view;
    }

    private void setUpEventButtons(View view) {
        //EventModel Buttons, do onClick here so handlers doesnt have to be in parent Activity
        ImageButton mealBtn = (ImageButton) view.findViewById(R.id.mealBtn);
        mealBtn.setOnClickListener(this);

        ImageButton otherBtn = (ImageButton) view.findViewById(R.id.otherBtn);
        otherBtn.setOnClickListener(this);

        ImageButton exerciseBtn = (ImageButton) view.findViewById(R.id.exerciseBtn);
        exerciseBtn.setOnClickListener(this);

        ImageButton bmBtn = (ImageButton) view.findViewById(R.id.bmBtn);
        bmBtn.setOnClickListener(this);

        ImageButton scoreBtn = (ImageButton) view.findViewById(R.id.scoreBtn);
        scoreBtn.setOnClickListener(this);
    }

    private void setUpMenu() {
        //cant come up with better solution for gaining access to toolbar buttons that lie on main_activity.xml
        ImageButton toTemplateBtn = (ImageButton) getActivity().findViewById(R.id.to_template_btn);
        toTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void initiateRecyclerView(View v) {
        //==========================================================================================
        recyclerView = (RecyclerView) v.findViewById(R.id.events_layout);
        layoutManager = new LinearLayoutManager((Context) this.callback);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(adapter);
        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        //==========================================================================================
    }

    private void populateList() {
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
    }
   /* public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Debug", "isnide onCreateOptionsMenu inside DiaryFragment"); //kallas aldrig.
        MenuInflater inflater = callBack.getMenuInflater();
        inflater.inflate(R.menu.cancel_done_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sendEventsForTemplate(null);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
        return true;
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        //common for all
        Gson gson = new Gson();
        Event event = null;

        switch (requestCode) {

            case NEW_MEAL:
                if (data.hasExtra(RETURN_MEAL_JSON)) {
                    String mealJSONData = data.getExtras().getString(RETURN_MEAL_JSON);
                    event = gson.fromJson(mealJSONData, Meal.class);
                }
                break;
            case NEW_OTHER:
                if (data.hasExtra(RETURN_OTHER_JSON)) {
                    String otherJSONData = data.getExtras().getString(RETURN_OTHER_JSON);
                    event = gson.fromJson(otherJSONData, Other.class);
                }
                break;
            case NEW_EXERCISE:
                if (data.hasExtra(RETURN_EXERCISE_JSON)) {
                    String exerciseJSONData = data.getExtras().getString(RETURN_EXERCISE_JSON);
                    event = gson.fromJson(exerciseJSONData, Exercise.class);
                }
                break;
            case NEW_BM:
                if (data.hasExtra(RETURN_BM_JSON)) {
                    String bmJSONData = data.getExtras().getString(RETURN_BM_JSON);
                    event = gson.fromJson(bmJSONData, BM.class);
                }
                break;
            case NEW_SCORE:
                if (data.hasExtra(RETURN_RATING_JSON)) {
                    String scoreJSONData = data.getExtras().getString(RETURN_RATING_JSON);
                    event = gson.fromJson(scoreJSONData, Rating.class);
                }
                break;
        }
        eventList.add(event);

        //se https://guides.codepath.com/android/Using-the-RecyclerView#itemanimator för 4 alternativ
        //här för förtydligande varför notifyDataSetChanged är mer mer ineffektiv: inte https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#notifyDataSetChanged()
        //item inserted in last position of eventList
        adapter.notifyItemInserted(eventList.size() - 1); //OBS! Simplistic! There should be possiblities to add another date & time than the latest.
    } //efter detta kraschar det

    /*This is needed since onClick otherwise goes to parent Activity*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mealBtn:
                newMealActivity(v);
                break;
            case R.id.otherBtn:
                newOtherActivity(v);
                break;
            case R.id.exerciseBtn:
                newExerciseActivity(v);
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


    //dessa metoder ska istället skapa fragments.
    //ska de skapas inifrån denna fragment (vad jag tror) eller från callBack
    public void newMealActivity(View view) {
        Intent intent = new Intent((Activity) this.callback, MealActivity.class);
        startActivityForResult(intent, NEW_MEAL);
    }

    private void newOtherActivity(View v) {
        Intent intent = new Intent((Activity) this.callback, OtherActivity.class);
        startActivityForResult(intent, NEW_OTHER);
    }

    private void newExerciseActivity(View v) {
        Intent intent = new Intent((Activity) this.callback, ExerciseActivity.class);
        startActivityForResult(intent, NEW_EXERCISE);
    }

    private void newBmActivity(View v) {
        Intent intent = new Intent((Activity) this.callback, BmActivity.class);
        startActivityForResult(intent, NEW_BM);
    }

    public void newScoreItem(View view) {
        Intent intent = new Intent((Activity) this.callback, RatingActivity.class);
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

    private List<Event> prepareMarkedEvents() {
        List<Event> eventsToSend = new ArrayList<>();
        for (int i : eventsMarked) {
            eventsToSend.add(eventList.get(i));
        }
        return eventsToSend;
    }

    /*
        When user markes list items for template or copying
     */
    private void changeToMarkedMenu() {
        tabsLayoutSwitcher.showNext();
        //callBack.viewPager do something here to stop swipe
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
