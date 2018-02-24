package com.johanlund.date_time;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Johan on 2017-10-12.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    //  public LocalDate localDate;
    Context context;
    DatePickerDialog.OnDateSetListener listener;
    public void setContext(Context c){
        this.context = c;
    }
    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current dateView as the default dateView in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(context, listener, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //see http://stackoverflow
        // .com/questions/11527051/get-date-from-datepicker-using-dialogfragment accepted
        // answer.
        ((DatePickerDialog.OnDateSetListener) context).onDateSet(view, year, month, day);
        //  localDate = LocalDate.of(year,month,day);
        // dateView.setText(Integer.valueOf(ld.getYear())+" "+ld.getMonth().toString()+"
        // "+Integer.valueOf(ld.getDayOfMonth()));
    }
       /* public LocalDate getLocalDate (){
            return localDate;
        }*/


}
