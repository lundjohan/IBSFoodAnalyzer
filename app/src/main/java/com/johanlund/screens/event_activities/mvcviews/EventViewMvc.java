package com.johanlund.screens.event_activities.mvcviews;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.johanlund.base_classes.Event;
import com.johanlund.screens.common.mvcviews.ViewMvc;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.List;

public interface EventViewMvc extends ViewMvc, DatePickerDialog.OnDateSetListener, TimePickerDialog
        .OnTimeSetListener {

    boolean createOptionsMenu(Menu menu, MenuInflater menuInflater);

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
    void setListener(EventActivityViewMvcListener listener);


    interface EventActivityViewMvcListener {
        void startTimePicker(View view);

        void startDatePicker(View view);

        void completeSession(Event finalEvent);

        void showInfo(String titleStr, int infoLayout);

        void finish();
    }
}
