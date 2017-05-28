package com.ibsanalyzer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static java.util.Collections.sort;

/**
 * Created by Johan on 2017-05-14.
 */

/**
 * Class is made for repeating usages through project
 */

public class Util {
    /**
     * Common usage for EventActivities inside the finish() method (Activity has been called from
     * other Activity via startActivityForResult)
     */
    public static void jsonAndMoreFinishingData(Object obj, String putExtraString, Activity
            usingActivity) {
        Gson gson = new Gson();
        String objAsJSON = gson.toJson(obj);

        Intent data = new Intent();
        data.putExtra(putExtraString, objAsJSON);
        usingActivity.setResult(RESULT_OK, data);
    }

    public static void serializableReturn(Serializable serializable, String putExtraString,
                                          Activity usingActivity) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(putExtraString, serializable);
        Intent data = new Intent();
        data.putExtras(bundle);
        usingActivity.setResult(RESULT_OK, data);
    }


    public static void dataComingBackFromTagAdder() {

    }

    public static void eventsToJSON(Object obj, String putExtraString, Activity usingActivity) {


    }

    public static List<Event> eventsFromJSON(Object obj, String putExtraString, Activity
            usingActivity) {
        return null;
    }

    //from http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
    public static int calculateNoOfColumns(Context context, int minWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / minWidth);
        return noOfColumns;
    }

    //Strange behavoiur!
    public static int calculateWidthOfItem(Context context, int mNoOfColumns) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        //I cannot understand how the alternative to the bottom cannot be the right result, but
        // this one is
        return (int) (dpWidth);
        //return (int)(dpWidth/mNoOfColumns);
    }

    //returns position where insertion took place
    //could perhaps be more effective in case the right index to insert was found first.
    public static InsertPositions insertEventWithDayMarker(List<Event> events, Event event) {
        //add event to list
        events.add(event);
        LocalDate dateOfEvent = event.getTime().toLocalDate();
        //1. Is there a DateMarkerEvent already with the same day as event?
        DateMarkerEvent dateMarker = null;
        if (!dateMarkerExists(events, dateOfEvent)) {
            dateMarker = new DateMarkerEvent(dateOfEvent);
            events.add(dateMarker);
        }
        //3. Sort list
        Collections.sort(events);

        //4. get index (after sort) of event
        int posEvent = events.indexOf(event);
        int posDateMarker = -1;

        if (dateMarker != null) {

            //get index (after sort) of the DateMarkerEvent.
            posDateMarker = events.indexOf(dateMarker);
        }
        //5. Create an InsertPositions and return it
        InsertPositions insertPositions = new InsertPositions(posEvent, posDateMarker);

        return insertPositions;
    }

    private static boolean dateMarkerExists(List<Event>events, LocalDate dateOfMarkerEvent){
        DateMarkerEvent dateMarker = new DateMarkerEvent(dateOfMarkerEvent);
        return events.indexOf(dateMarker)> -1;
    }

}
