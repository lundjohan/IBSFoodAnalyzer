package com.johanlund.event_activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.johanlund.base_classes.Meal;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.util.Util;

import static com.johanlund.constants.Constants.EVENT_TO_CHANGE;
import static com.johanlund.constants.Constants.MEAL;

public class MealActivity extends TagEventActivity {
    private TextView portionView;

    @Override
    protected int getInfoLayout() {
        return R.layout.info_meal;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_meal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        portionView = (TextView) findViewById(R.id.portionSize);
        final Activity thisActivity = this;
        portionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.useNumberPickerDialog(thisActivity, portionView);
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra(EVENT_TO_CHANGE)) {
            Meal meal = (Meal) intent.getSerializableExtra(EVENT_TO_CHANGE);
            portionView.setText(Double.toString(meal.getPortions()));
        }
    }

    @Override
    protected String getTextForAddTagsBtn() {
        return "Add Meal Components";
    }

    @Override
    protected int getEventType() {
        return MEAL;
    }

    @Override
    protected void buildEvent() {
        //create meal
        Log.d("Debug", "finish inside MealActivity");
        double portions = Double.parseDouble((String) portionView.getText());
        Meal event = new Meal(getLocalDateTime(), getComment(), tagsList, portions);
        returnEvent(event);
    }

    @Override
    protected String getTitleStr() {
        return "New Meal";
    }

}
