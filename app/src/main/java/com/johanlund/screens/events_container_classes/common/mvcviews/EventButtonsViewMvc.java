package com.johanlund.screens.events_container_classes.common.mvcviews;

import android.content.Intent;
import android.view.View;

public interface EventButtonsViewMvc  {
    interface Listener {
        void newEventActivity(int eventType);

        void executeNewEvent(int requestCode, Intent data);
    }
}
