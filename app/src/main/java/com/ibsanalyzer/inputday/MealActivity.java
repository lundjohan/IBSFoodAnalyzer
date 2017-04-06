package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

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
    @Override
    public void finish(){
        Intent data = new Intent();
        LocalDateTime time = LocalDateTime.now();
        List<Tag > tags = new ArrayList<>();
        tags.add(new Tag(time, "green_leaves", 1.7));
        Meal meal = new Meal(time, tags, 2.3);

        //JSON
        Gson gson = new Gson();
        String mealAsJSON = gson.toJson(meal);
        data.putExtra("returnMealJSON",mealAsJSON);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
