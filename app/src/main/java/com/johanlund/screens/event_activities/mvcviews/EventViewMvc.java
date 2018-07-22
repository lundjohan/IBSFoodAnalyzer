package com.johanlund.screens.event_activities.mvcviews;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.johanlund.base_classes.Event;
import com.johanlund.screens.common.mvcviews.ViewMvc;
import com.johanlund.screens.common.mvcviews.WithOptionsMenuViewMvc;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.List;

public interface EventViewMvc extends WithOptionsMenuViewMvc, DatePickerDialog.OnDateSetListener, TimePickerDialog
        .OnTimeSetListener {

    void bindEventToView(Event e);

    Event retrieveEventFromView();

    void bindAddedTagToView(String tagName);

    void setDateView(LocalDate ld);

    void setTimeView(LocalTime lt);

    LocalDateTime getLocalDateTime();

    void showEventAlreadyExistsPopUp(int eventType);

    void doneClicked(View view);

    void giveOptionToQuitOrCancel();

    void removeTagFromView(String tagName);

    List<String> getTagNames();

    /**
     * Set a listener that will be notified by this MVC view
     *
     * @param listener listener that should be notified; null to clear
     */
    void setListener(Listener listener);


    interface Listener extends ViewMvc.Listener {
        void startTimePicker(View view);

        void startDatePicker(View view);

        void completeSession(Event finalEvent);

        void finish();
    }
}
