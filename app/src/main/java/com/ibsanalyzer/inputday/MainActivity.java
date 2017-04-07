package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Meal;

public class MainActivity extends AppCompatActivity {
    static private final int NEW_MEAL = 1000;

    private LinearLayout eventsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventsLayout = (LinearLayout) findViewById(R.id.eventsLayout);

    }

    /**
     * Called when the user is finished with customizing new meal
     */
    public void newMeal(View view) {
        //LinearLayout mealBox = (LinearLayout) findViewById(R.id.meal_layout);
        View mealBox = getLayoutInflater().inflate(R.layout.meal_box, eventsLayout);
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
                    View mealBox = getLayoutInflater().inflate(R.layout.meal_box, eventsLayout);
                    TextView textView = new TextView(this);
                    String text = meal.getTime().getHour()+':'+meal.getTime().getMinute()+ " Portions: "+meal.getPortions() + "Tags: "+meal.getTags().get(0).getName()+" x"+meal.getTags().get(0).getSize();
                    textView.setText(text);
                    eventsLayout.addView(textView);

                }
            }

        }
    }

}

