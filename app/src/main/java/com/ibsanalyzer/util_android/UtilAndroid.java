package com.ibsanalyzer.util_android;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.util.InsertPositions;
import com.ibsanalyzer.util.Util;

import java.util.List;

/**
 * Created by Johan on 2017-05-28.
 */

public class UtilAndroid {
    public static void doneClicked(Activity activity) {
        activity.finish();
    }

    public void addEventToList(List<Event> eventList, Event event, RecyclerView.Adapter adapter) {
        InsertPositions insertPositions = Util.insertEventWithDayMarker(eventList, event);
        adapter.notifyItemInserted(insertPositions.getPosInserted());
        if (insertPositions.isDateMarkerAdded()) {
            adapter.notifyItemInserted(insertPositions.getPosDateMarker());
        }
    }


}
