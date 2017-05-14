package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Meal;

import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_JSON;

public class MealActivity extends TagEventActivity {
    private TextView portionView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_meal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        portionView = (TextView) findViewById(R.id.portions);
    }
    @Override
    public void finish() {
        //create meal
        double portions = Double.parseDouble((String) portionView.getText());
        Meal meal = new Meal(datetime, tagsList, portions);
        Gson gson = new Gson();
        String mealAsJSON = gson.toJson(meal);
        Intent data = new Intent();
        data.putExtra(RETURN_MEAL_JSON, mealAsJSON);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
