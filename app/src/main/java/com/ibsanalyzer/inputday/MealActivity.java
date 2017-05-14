package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_JSON;
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
        Util.jsonAndMoreFinishingData(meal,RETURN_MEAL_JSON, this);
    }
}
