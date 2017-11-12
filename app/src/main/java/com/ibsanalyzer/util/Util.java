package com.ibsanalyzer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.InputEvent;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.diary.EventActivity;
import com.ibsanalyzer.diary.R;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ibsanalyzer.constants.Constants.BM;
import static com.ibsanalyzer.constants.Constants.CHANGED_EVENT;
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
     *
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
     *
     * @param serializable
     * @param eventId
     */
    public static void returnChangedEvent(Serializable serializable, String putExtraString,
                                          EventActivity usingActivity, long eventId, int
                                                  posInList) {
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
     * @param event
     * @param events
     * @return -1 on failure
     */
    //TODO Change to if (events.size()<10) and Implement else
    //TODO ugly method, works, but should be total remake over.
    //ineffective method, but doesnt matter since it is inly used when ONE event is added.
    //(other methods are used to add much )
    public static int addEventAtRightPlace(Event event, List<Event> events) {
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
     * @param e not allowed to be null or other classes than listed in conditionals here.
     * @return
     */
    public static int getTypeOfEvent(Event e) {
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
        } else if (e == null) {
            throw new NullPointerException("Event should not be null here");
        }
        return -1;
    }

    public static void useNumberPickerDialog(Activity activity, final TextView textWithNrToChange) {
        View v = activity.getLayoutInflater().inflate(R.layout.decimal_number_picker, null);

        //for conversion to numbers on both sides of decimal point note that double is inexakt =>
        // 4.5 can become 4.4999999999, so it's not good idea to simply truncate with (int)
        Double originalNr = Double.parseDouble((String) textWithNrToChange.getText());
        int intPart = originalNr.intValue();
        int decPart = (int) Math.round((originalNr.doubleValue() - (double) intPart) * 10.);
        final NumberPicker np1 = (NumberPicker) v.findViewById(R.id.numberPicker1);
        np1.setMinValue(0);
        np1.setMaxValue(9);
        np1.setValue(intPart);
        final NumberPicker np2 = (NumberPicker) v.findViewById(R.id.numberPicker2);
        np2.setMinValue(0);
        np2.setMaxValue(9);
        np2.setValue(decPart);
        new AlertDialog.Builder(activity)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        double value;
                        value = np1.getValue() + ((double) np2.getValue()) / 10;
                        textWithNrToChange.setText(Double.toString(value));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }


}
