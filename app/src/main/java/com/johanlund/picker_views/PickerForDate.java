package com.johanlund.picker_views;

import android.app.DatePickerDialog;

public interface PickerForDate extends Picker {
    void setListener(DatePickerDialog.OnDateSetListener listener);
}
