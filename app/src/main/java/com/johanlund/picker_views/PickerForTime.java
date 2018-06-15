package com.johanlund.picker_views;

import android.app.TimePickerDialog;

public interface PickerForTime extends Picker {
    void setListener(TimePickerDialog.OnTimeSetListener listener);
}

