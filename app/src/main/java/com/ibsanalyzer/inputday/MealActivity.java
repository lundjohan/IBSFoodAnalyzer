package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Meal;
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
        data.putExtra("returnMealJSON", mealAsJSON);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
