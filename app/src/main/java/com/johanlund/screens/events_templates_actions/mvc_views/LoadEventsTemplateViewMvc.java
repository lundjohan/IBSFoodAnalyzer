package com.johanlund.screens.events_templates_actions.mvc_views;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.johanlund.base_classes.Event;
import com.johanlund.date_time.DateTimeFormat;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.model.EventsTemplate;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;

import java.util.List;

public class LoadEventsTemplateViewMvc extends EventsTemplateViewMvcAbstract implements
        DatePickerDialog.OnDateSetListener {
    TextView dateView;

    public LoadEventsTemplateViewMvc(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
        dateView = rootView.findViewById(R.id.dateView);
    }

    @Override
    protected int getUpperPartOfLayout() {
        return R.layout.activity_load_events_templates;
    }

    @Override
    public void bindEventsTemplateToView(@NotNull EventsTemplate et) {
        ec.eventsOfDay.clear();
        ec.eventsOfDay.addAll(et.getEvents());
        ec.adapter.notifyDataSetChanged();
    }

    @Override
    public void bindDateToView(@NotNull LocalDate ld) {
        dateView.setText(DateTimeFormat.toTextViewFormat(ld));
    }

    @NotNull
    @Override
    public EventsTemplate createEventsTemplateFromView() {
        List<Event> events = ec.eventsOfDay;

        //change date for every event to correct one
        String ldStr = (String) dateView.getText();
        LocalDate ld = DateTimeFormat.fromTextViewDateFormat(ldStr);
        for (Event e : events) {
            e.changeDateKeepTime(ld);
        }
        return new EventsTemplate(events, "name irrelevant");
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.info_events_template;
    }

    @Override
    protected String getBarTitle() {
        return "Load EventsTemplate";
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //month datepicker +1 == LocalDate.Month
        LocalDate ld = LocalDate.of(year, month + 1, dayOfMonth);
        bindDateToView(ld);
    }
}
