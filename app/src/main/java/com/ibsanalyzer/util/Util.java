package com.ibsanalyzer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ibsanalyzer.inputday.R.id.dateMarker;
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

    //instead of sorting this should use addEventAtRightPlace for insertion of event
    public static InsertPositions insertEventWithDayMarker(List<Event> events, Event event) {
        //add event to list
        //this feels bad. better to add it at the right index straight away...
        events.add(event);

        LocalDate dateOfEvent = event.getTime().toLocalDate();
        //1. Is there a DateMarkerEvent already with the same day as event?
        DateMarkerEvent dateMarker = addDateMarkerIfNotExists(dateOfEvent, events);

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
    public static DateMarkerEvent addDateMarkerIfNotExists(LocalDate dateOfEvent, List<Event> events) {
        DateMarkerEvent dateMarker = null;
        if (!dateMarkerExists(events, dateOfEvent)) {
            dateMarker = new DateMarkerEvent(dateOfEvent);
            addEventAtRightPlace(dateMarker, events);
        }
        return dateMarker;
    }

    /**
     *
     * @param event
     * @param events
     * @return -1 on failure
     */
    //TODO Change to if (events.size()<10) and Implement else
    //TODO ugly method, works, but should be total remake over.
    private static int addEventAtRightPlace(Event event, List<Event> events) {
        int indexOfInsertion = -1;
        if (events.isEmpty()){
            events.add(event);
            indexOfInsertion = 0;
        }
        else if (events.size()>0){
            for (int i = 0; i< events.size();i++){
                if (events.get(i).getTime().isBefore(event.getTime()))
                    continue;
                else{
                    events.add(i, event);
                    indexOfInsertion = i;
                    break;
                }

            }
        }
        //do some more effective algorithm
        else{

        }
        //no event has been added => means that it should be added last (see in loop above why so). This is ugly but works.
        if (indexOfInsertion == -1)
            events.add(event);
        return indexOfInsertion;
    }

    private static boolean dateMarkerExists(List<Event>events, LocalDate dateOfMarkerEvent){
        DateMarkerEvent dateMarker = new DateMarkerEvent(dateOfMarkerEvent);
        return events.indexOf(dateMarker)> -1;
    }


}
