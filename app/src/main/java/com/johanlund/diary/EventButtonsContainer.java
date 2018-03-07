package com.johanlund.diary;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.johanlund.ibsfoodanalyzer.R;

import static android.app.Activity.RESULT_OK;
import static com.johanlund.constants.Constants.CHANGED_EVENT;
import static com.johanlund.constants.Constants.NEW_EVENT;
import static com.johanlund.constants.Constants.TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED;

/**
 * Created by Johan on 2018-03-07.
 */
public class EventButtonsContainer {

    public interface EventButtonContainerUser extends View.OnClickListener{
        void newMealActivity(View view);

        void newOtherActivity(View v);

        void newExerciseActivity(View v);

        void newBmActivity(View v);

        void newScoreItem(View view);

        void executeNewEvent(int requestCode, Intent data);
    }

    private EventButtonContainerUser user;

    public EventButtonsContainer(EventButtonContainerUser user) {
        this.user = user;
    }

    public void setUpEventButtons(View view) {
        //EventModel Buttons, do onClick here so handlers don't have to be in parent Activity
        ImageButton mealBtn = (ImageButton) view.findViewById(R.id.mealBtn);
        mealBtn.setOnClickListener(user);

        ImageButton otherBtn = (ImageButton) view.findViewById(R.id.otherBtn);
        otherBtn.setOnClickListener(user);

        ImageButton exerciseBtn = (ImageButton) view.findViewById(R.id.exerciseBtn);
        exerciseBtn.setOnClickListener(user);

        ImageButton bmBtn = (ImageButton) view.findViewById(R.id.bmBtn);
        bmBtn.setOnClickListener(user);

        ImageButton scoreBtn = (ImageButton) view.findViewById(R.id.ratingBtn);
        scoreBtn.setOnClickListener(user);
    }

    public void doOnClick(View v) {
        switch (v.getId()) {
            case R.id.mealBtn:
                user.newMealActivity(v);
                break;
            case R.id.otherBtn:
                user.newOtherActivity(v);
                break;
            case R.id.exerciseBtn:
                user.newExerciseActivity(v);
                break;
            case R.id.bmBtn:
                user.newBmActivity(v);
                break;
            case R.id.ratingBtn:
                user.newScoreItem(v);
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data.hasExtra(NEW_EVENT)){
            user.executeNewEvent(requestCode, data);
        }
    }
}
