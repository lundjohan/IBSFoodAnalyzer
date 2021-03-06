package com.johanlund.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.InputEvent;
import com.johanlund.base_classes.Tag;
import com.johanlund.ibsfoodanalyzer.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

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
                tags.add(new Tag(e.getTime(), ((Exercise) e).getTypeOfExercise()));
            }
        }
        return tags;
    }

    public static void setNrsForNumberPicker(NumberPicker np, boolean startValueIsOne) {
        np.setMinValue(0);
        np.setMaxValue(9);
        if (startValueIsOne == true) {
            np.setValue(1);
        }
    }

    public static void useNumberPickerDialog(final TextView textWithNrToChange, LayoutInflater inflater, Context context) {
        View v = inflater.inflate(R.layout.decimal_number_picker, null);
        final NumberPicker np1 = (NumberPicker) v.findViewById(R.id.numberPicker1);
        setNrsForNumberPicker(np1, true);
        final NumberPicker np2 = (NumberPicker) v.findViewById(R.id.numberPicker2);
        setNrsForNumberPicker(np2, false);

        //for conversion to numbers on both sides of decimal point note that double is inexakt =>
        // 4.5 can become 4.4999999999, so it's not good idea to simply truncate with (int)
        Double originalNr = Double.parseDouble((String) textWithNrToChange.getText());
        int intPart = originalNr.intValue();
        int decPart = (int) Math.round((originalNr.doubleValue() - (double) intPart) * 10.);
        np1.setValue(intPart);
        np2.setValue(decPart);
        new AlertDialog.Builder(context)
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

    public static long[] appendToArray(long[] arr, long toAdd) {
        long[] ret;
        if (arr == null) {
            ret = new long[1];
        } else {
            ret = new long[arr.length + 1];
        }
        for (int i = 0; i < ret.length; i++)
            ret[ret.length - 1] = toAdd;
        return ret;
    }

    // suGar => Sugar
    public static String makeFirstLetterCapitalAndRestSmall(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static void addLineSeparator(RecyclerView recyclerView, LinearLayoutManager
            layoutManager) {
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }
}
