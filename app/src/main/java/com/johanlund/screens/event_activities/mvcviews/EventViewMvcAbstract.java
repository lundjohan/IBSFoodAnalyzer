package com.johanlund.screens.event_activities.mvcviews;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.johanlund.base_classes.Event;
import com.johanlund.date_time.DateTimeFormat;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.common.mvcviews.ViewMvcAbstract;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

public abstract class EventViewMvcAbstract extends ViewMvcAbstract implements EventViewMvc {
    protected final Context context;
    TextView dateView;
    TextView timeView;
    TextView commentView;
    Button dateBtn;
    Button timeBtn;
    boolean eventHasBreak;
    EventViewMvc.Listener listener;

    public EventViewMvcAbstract(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.activity_event, container, false);
        ViewGroup content = rootView.findViewById(R.id.appendingLayout);
        context = inflater.getContext();
        inflater.inflate(getLayoutRes(), content, true);
        initialize();
    }

    private void initialize() {
        dateBtn = (Button) rootView.findViewById(R.id.dateBtn);
        timeBtn = (Button) rootView.findViewById(R.id.timeBtn);

        dateView = (TextView) rootView.findViewById(R.id.date);
        timeView = (TextView) rootView.findViewById(R.id.secondLine);
        commentView = (TextView) rootView.findViewById(R.id.commentView);
        initializeSpecViews();
    }

    protected abstract void initializeSpecViews();

    @Override
    public void setListener(EventViewMvc.Listener listener) {
        this.listener = listener;
    }

    @Override
public EventViewMvc.Listener getListener(){
        return listener;
    }
    @Override
    public void bindEventToView(Event e) {
        eventHasBreak = e.hasBreak();
        setDateView(e.getTime().toLocalDate());
        setTimeView(e.getTime().toLocalTime());
        commentView.setText(e.getComment());
        bindEventSpecsToView(e);
    }

    protected abstract void bindEventSpecsToView(Event e);

    protected abstract int getLayoutRes();

    protected abstract Event makeEventFromView(LocalDateTime ldt, String comment);

    @Override
    public void setDateView(LocalDate ld) {
        dateView.setText(DateTimeFormat.toTextViewFormat(ld));
    }

    @Override
    public void setTimeView(LocalTime lt) {
        timeView.setText(DateTimeFormat.toTextViewFormat(lt));
    }


    @Override
    public void doneClicked(View view) {
        Event e = retrieveEventFromView();
        listener.completeSession(e);
    }

    @Override
    public Event retrieveEventFromView() {
        return makeEventFromView(getLocalDateTime(), commentView.getText().toString());
    }

    //keep this method instead of local variables, it keeps it much less error prone
    public LocalDateTime getLocalDateTime() {
        String ldStr = (String) dateView.getText();
        String ltStr = (String) timeView.getText();
        LocalDate ld = DateTimeFormat.fromTextViewDateFormat(ldStr);
        LocalTime lt = DateTimeFormat.fromTextViewTimeFormat(ltStr);
        return LocalDateTime.of(ld, lt);
    }



    /**
     * DATE AND TIME PICKER
     */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //month datepicker +1 == LocalDate.Month
        LocalDate ld = LocalDate.of(year, month + 1, dayOfMonth);
        setDateView(ld);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        LocalTime lt = LocalTime.of(hourOfDay, minute);
        setTimeView(lt);
    }

    public void showEventAlreadyExistsPopUp(int eventType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false).
                setTitle("Event already exists").
                setMessage("A(n) " + Event.getEventTypeStr(eventType) + " at this date and time " +
                        "already exists in diary. Change the date or time of the event.").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //don't do anything
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton
                .getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    @Override
    public void giveOptionToQuitOrCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false).
                setTitle("Leave changes undone?").
                setCancelable(true).
                setNegativeButton(android.R.string.cancel, new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).
                setMessage("Are you sure you want to go back to diary without saving changes?").
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
