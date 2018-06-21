package com.johanlund.picker_views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Johan on 2017-10-12.
 */

public class DatePickerFragment extends DialogFragment
        implements PickerForDate {
    Context context;
    DatePickerDialog.OnDateSetListener listener;

    public void setContext(Context c) {
        this.context = c;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current dateView as the default dateView in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(context, listener, year, month, day);
    }
}
