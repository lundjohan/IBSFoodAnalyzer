package com.ibsanalyzer.diary;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.date_time.DatePickerFragment;
import com.ibsanalyzer.date_time.DateTimeFormat;
import com.ibsanalyzer.model.EventsTemplate;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.EVENTSTEMPLATE_TO_LOAD;
import static com.ibsanalyzer.constants.Constants.EVENTS_TO_LOAD;

public class LoadEventsTemplateActivity extends EventsTemplateActivity implements
        DatePickerDialog.OnDateSetListener {
    TextView dateView;
    EventsTemplate et;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra(EVENTSTEMPLATE_TO_LOAD)) {
            et = (EventsTemplate) intent.getSerializableExtra
                    (EVENTSTEMPLATE_TO_LOAD);
        }
        super.onCreate(savedInstanceState);
        dateView = (TextView) findViewById(R.id.dateView);
        setDateView(LocalDate.now());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_load_events_templates;
    }

    /**
     * The name doesn't matter in this class
     *
     * @return
     */
    @Override
    protected String getStartingName() {
        return "";
    }


    /**
     * in this class, this method is only used as a preperation for finish.
     *
     * @param et
     */
    @Override
    protected void saveToDB(EventsTemplate et) {
    }


    /**
     * Send back data to TemplateFragment
     *
     * @param
     */
    @Override
    public void finish() {
        List<Event> eventsToReturn = et.getEvents();
        for (Event e : eventsToReturn) {
            String ldStr = (String) dateView.getText();
            LocalDate ld = DateTimeFormat.fromTextViewDateFormat(ldStr);
            e.setDate(ld);
        }
        Intent intent = new Intent();
        intent.putExtra(EVENTS_TO_LOAD, (Serializable) eventsToReturn);
        setResult(RESULT_OK, intent);
        super.finish();
    }
    /**
     * when user wants to change date.
     */
    public void newDate(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setContext(this);
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * This must be overloaded from EditEventsTemplateActivity
     *
     * @return
     */
    @Override
    protected String getEndingName() {
        return "";
    }

    @Override
    protected void setUpNameViewIfExisting() {
        //dont do anything, as name is of no use for this class.
    }

    @Override
    protected List<Event> getStartingEvents() {

        return et.getEvents();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //month datepicker +1 == LocalDate.Month
        LocalDate ld = LocalDate.of(year, month + 1, dayOfMonth);
        setDateView(ld);
    }

    private void setDateView(LocalDate ld) {
        dateView.setText(DateTimeFormat.toTextViewFormat(ld));
    }
}
