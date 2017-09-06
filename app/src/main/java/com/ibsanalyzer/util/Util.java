package com.ibsanalyzer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.InputEvent;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.inputday.EventActivity;
import com.ibsanalyzer.pseudo_event.DateMarkerEvent;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ibsanalyzer.constants.Constants.BM;
import static com.ibsanalyzer.constants.Constants.CHANGED_EVENT;
import static com.ibsanalyzer.constants.Constants.DATE_MARKER;
import static com.ibsanalyzer.constants.Constants.EXERCISE;
import static com.ibsanalyzer.constants.Constants.ID_OF_EVENT_RETURNED;
import static com.ibsanalyzer.constants.Constants.MEAL;
import static com.ibsanalyzer.constants.Constants.OTHER;
import static com.ibsanalyzer.constants.Constants.POS_OF_EVENT_RETURNED;
import static com.ibsanalyzer.constants.Constants.RATING;

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

    /**
     * Method used by MealActivity, TagAdderActivity etc as part of creating a new Event.
     * @param serializable
     * @param putExtraString
     * @param usingActivity
     */
    public static void returnSerializable(Serializable serializable, String putExtraString,
                                          Activity usingActivity) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(putExtraString, serializable);
        Intent data = new Intent();
        data.putExtras(bundle);
        usingActivity.setResult(RESULT_OK, data);
    }

    /**
     * Method used my MealActivity etc as part of CHANGING an Event.
     * @param serializable
     * @param eventId
     */
    public static void returnChangedEvent(Serializable serializable, String putExtraString, EventActivity usingActivity, long eventId, int posInList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(putExtraString, serializable);
        bundle.putLong(ID_OF_EVENT_RETURNED, eventId);
        bundle.putInt(POS_OF_EVENT_RETURNED, posInList);
        bundle.putBoolean(CHANGED_EVENT, true);
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
    //==============================================================================================
    //Removal of events from list.
    // (possible removal of datemarkers must be in consideration)
    //==============================================================================================

    /**
     * @param events
     * @param position
     * @return true if a DateMarker was last for day and therefore removed.
     */
    public static boolean removeEventAndAlsoDateMarkerIfLast(List<Event> events, int position) {
        LocalDate dateOfEvent = events.get(position).getTime().toLocalDate();

        events.remove(position);
        //possibly remove datemarker
        //position has moved to be on step later in list (notice t)
        //N.B. another event could have been before removed event so it is not so banal.
        if (dateMarkerIsSoleEventOneDay(events, dateOfEvent, position)) {
            //position is now upheld by DateMarker (since list has been contracted by events
            // .remove above)
            events.remove(position);
            return true;
        }
        return false;
    }

    /**
     * Is is understated that DateMarkerEvent occupies last position in list for that date.
     *
     * Uses position to sort faster.
     *
     *
     * <p>
     * Requires sorted list.
     * <p>
     *
     *
     * @param events
     * @param dateOfEvent
     * @param position => is either a normal event that day, or a datemarker that day. Used for optimization
     * @return
     */
    private static boolean dateMarkerIsSoleEventOneDay(List<Event> events, LocalDate dateOfEvent, int position) {
        //check at position
        if (events.get(position).getClass() != DateMarkerEvent.class){
            return false;
        }
        //check before position if there are other events that day
        if (position>0 && events.get(position-1).getTime().toLocalDate().isEqual(dateOfEvent)){
            return false;
        }
        //Check after position is not needed since datemarker should always be placed last for day.
        return true;
    }
    //==============================================================================================
    //Adding of events from list.
    // (possible adding of datemarkers must be in consideration)
    //==============================================================================================
    //instead of sorting this should use addDateEventAtRightPlace for insertion of event
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


    public static DateMarkerEvent addDateMarkerIfNotExists(LocalDate dateOfEvent, List<Event>
            events) {
        DateMarkerEvent dateMarker = null;
        if (!dateMarkerExists(events, dateOfEvent)) {
            dateMarker = new DateMarkerEvent(dateOfEvent);
            addEventAtRightPlace(dateMarker, events);
        }
        return dateMarker;
    }

    /**
     * @param event
     * @param events
     * @return -1 on failure
     */
    //TODO Change to if (events.size()<10) and Implement else
    //TODO ugly method, works, but should be total remake over.
    //ineffective method, but doesnt matter since it is inly used when ONE event is added.
    //(other methods are used to add much )
    private static int addEventAtRightPlace(Event event, List<Event> events) {
        int indexOfInsertion = -1;
        if (events.isEmpty()) {
            events.add(event);
            indexOfInsertion = 0;
        } else if (events.size() > 0) {
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getTime().isBefore(event.getTime()))
                    continue;
                else {
                    events.add(i, event);
                    indexOfInsertion = i;
                    break;
                }

            }
        }
        //do some more effective algorithm
        else {

        }
        //no event has been added => means that it should be added last (see in loop above why
        // so). This is ugly but works.
        if (indexOfInsertion == -1)
            events.add(event);
        return indexOfInsertion;
    }

    private static boolean dateMarkerExists(List<Event> events, LocalDate dateOfMarkerEvent) {
        DateMarkerEvent dateMarker = new DateMarkerEvent(dateOfMarkerEvent);
        return events.indexOf(dateMarker) > -1;
    }

    /**
     * tags exist in Meal, Other and Exercise events.
     *
     * @param events
     * @return
     */
    public static List<Tag> getTags(List<Event> events) {
        List<Tag> tags = new ArrayList<>();
        for (Event e : events) {
            if (e instanceof InputEvent) {
                tags.addAll(((InputEvent) e).getInputTags());
            } else if (e instanceof Exercise) {
                tags.add(((Exercise) e).getTypeOfExercise());
            }
        }
        return tags;
    }

    /**
     * Methods used by DiaryFragment at start of app after list has been filled with events
     *
     * @param eventList
     */
    public static void addDateEventsToList(List<Event> eventList) {
        LocalDate date;
        for (int i = 0; i < eventList.size(); i++) {
            date = eventList.get(i).getTime().toLocalDate();
            i = Util.stepForwardUntilNewDateOrEndOfList(eventList, date, i);
            Util.addDateEventToList(date, eventList, i);
        }
    }

    /**
     * Prerequisite: position is the place to add DateEvent.
     * OR if position == size of list, it is DateEvent is added last in list
     *
     * @param dateOfEvent
     * @param eventList
     * @param position
     */
    public static void addDateEventToList(LocalDate dateOfEvent, List<Event> eventList, int
            position) {
        DateMarkerEvent dateMarker = new DateMarkerEvent(dateOfEvent);

        if (position == eventList.size()) {
            eventList.add(dateMarker);
        } else {
            eventList.add(position, dateMarker);
        }
    }

    public static int stepForwardUntilNewDateOrEndOfList(List<Event> eventList, LocalDate ld,
                                                         int startPos) {
        int i = ++startPos;
        if (startPos >= eventList.size() || !eventList.get(i).getTime().toLocalDate().equals(ld))
            return i;
        else {
            return stepForwardUntilNewDateOrEndOfList(eventList, ld, i);
        }
    }

    public static int getTypeOfEvent (Event e){
        if (e instanceof Meal) {
            return MEAL;
        } else if (e instanceof Other) {
            return OTHER;
        } else if (e instanceof Exercise) {
            return EXERCISE;
        } else if (e instanceof Bm) {
            return BM;
        } else if (e instanceof Rating) {
            return RATING;
        } else if (e instanceof DateMarkerEvent) {
            return DATE_MARKER;
        }

        throw new RuntimeException("unknown class");

    }



}
