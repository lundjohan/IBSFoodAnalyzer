package com.johanlund.screens.events_templates_actions.mvc_views;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johanlund.base_classes.Event;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.model.EventsTemplate;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * This is applicable both for creating new and edit EventsTemplates
 */
public class EditEventsTemplateViewMvc extends EventsTemplateViewMvcAbstract {
    TextView nameView;

    public EditEventsTemplateViewMvc(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
        nameView = rootView.findViewById(R.id.template_name);
    }

    @Override
    protected int getUpperPartOfLayout() {
        return R.layout.activity_save_events_templates;
    }

    @Override
    public void bindEventsTemplateToView(@NotNull EventsTemplate et) {
        setNameView(et.getNameOfTemplate());
        ec.eventsOfDay = et.getEvents();
        ec.adapter.notifyDataSetChanged();
    }

    private void setNameView(String nameOfTemplate) {
        nameView.setText(nameOfTemplate);
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.info_events_template;
    }

    @Override
    protected String getBarTitle() {
        return "Edit EventsTemplate";
    }

    //not applicable
    @Override
    public void bindDateToView(@NotNull LocalDate date) {

    }

    @NotNull
    @Override
    public EventsTemplate createEventsTemplateFromView() {
        List<Event> events = ec.eventsOfDay;
        String name = nameView.getText().toString();
        return new EventsTemplate(events, name);
    }
}

