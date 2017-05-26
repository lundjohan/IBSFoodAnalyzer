package com.ibsanalyzer.inputday;

import android.os.Bundle;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;

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
        Meal meal = new Meal(getLocalDateTime(), tagsList, portions);
        Util.serializableReturn(meal,RETURN_MEAL_SERIALIZABLE, this);
    }
}
