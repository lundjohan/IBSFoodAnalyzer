package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

public class MealActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
    }
    /* Called when the user is finished with customizing new meal */
    public void newMeal() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

}
