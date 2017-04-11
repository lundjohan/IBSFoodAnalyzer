package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int NEW_MEAL = 1000;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    List<Event> eventList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        recyclerView = (RecyclerView)findViewById(R.id.events_layout);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);


    }

    public void newMealActivity(View view) {
        Intent intent = new Intent(this, MealActivity.class);
        startActivityForResult(intent, NEW_MEAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == NEW_MEAL) {
                if (data.hasExtra("returnMealJSON")) {
                    String mealJSONData = data.getExtras().getString("returnMealJSON");
                    Gson gson = new Gson();
                    Meal meal = gson.fromJson(mealJSONData, Meal.class);

                    String text = meal.getTime().getHour() + ":" + meal.getTime().getMinute() + " Portions: " + meal.getPortions() + "Tags: " + meal.getTags().get(0).getName() + " x" + meal.getTags().get(0).getSize();
                    eventList.add(meal);
                    adapter.notifyDataSetChanged();
                }
            }

        }
    }

}

