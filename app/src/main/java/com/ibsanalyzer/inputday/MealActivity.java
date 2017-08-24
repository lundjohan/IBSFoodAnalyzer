package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.EVENT_TO_CHANGE;
import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;
import static com.ibsanalyzer.inputday.R.drawable.meal;
import static com.ibsanalyzer.inputday.R.id.intensity;
import static com.ibsanalyzer.inputday.R.id.intensityBar;
import static com.ibsanalyzer.inputday.R.id.intensityName;

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
        Intent intent = getIntent();
        if (intent.hasExtra(EVENT_TO_CHANGE)){
            Meal meal = (Meal)intent.getSerializableExtra(EVENT_TO_CHANGE);
            portionView.setText(Double.toString(meal.getPortions()));
        }
    }
    @Override
    public void finish() {
        //create meal
        double portions = Double.parseDouble((String) portionView.getText());
        Meal event = new Meal(getLocalDateTime(), tagsList, portions);
        returnEvent(event, RETURN_MEAL_SERIALIZABLE);
    }
}
