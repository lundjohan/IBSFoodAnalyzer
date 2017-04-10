package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Meal;

import java.util.ArrayList;

import static com.ibsanalyzer.inputday.R.attr.colorAccent;

public class MainActivity extends AppCompatActivity {
    static private final int NEW_MEAL = 1000;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private ListView eventsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventsLayout = (ListView) findViewById(R.id.eventsLayout);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        eventsLayout.setAdapter(adapter);


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
                    listItems.add(text);
                    adapter.notifyDataSetChanged();

                }
            }

        }
    }

}

