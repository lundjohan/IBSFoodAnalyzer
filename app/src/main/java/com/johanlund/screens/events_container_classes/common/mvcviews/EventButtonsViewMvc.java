package com.johanlund.screens.events_container_classes.common.mvcviews;

import android.content.Intent;
import android.view.View;

public interface EventButtonsViewMvc  {
    interface Listener {
        void newMealActivity(View view);

        void newOtherActivity(View v);

        void newExerciseActivity(View v);

        void newBmActivity(View v);

        void newScoreItem(View view);

        void executeNewEvent(int requestCode, Intent data);
    }
}
