package com.johanlund.screens.event_activities.mvcviews;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.johanlund.picker_views.DatePickerFragment;
import com.johanlund.picker_views.TimePickerFragment;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

public abstract class EventViewMvcAbstract implements EventViewMvc {
    protected final Context context;

    TextView dateView;
    TextView timeView;
    TextView commentView;
    Button dateBtn;
    Button timeBtn;

    boolean eventHasBreak;

    protected View rootView;
    EventActivityViewMvcListener listener;

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
    public void setListener(EventActivityViewMvcListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean createOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.info_menu, menu);
        inflater.inflate(R.menu.done_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                doneClicked(null);
                return true;
            }
        });
        menu.findItem(R.id.menu_info).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_done){
                    doneClicked(null);
                }
                else if (item.getItemId() == R.id.menu_info){
                    listener.showInfo(getBarTitle(), getInfoLayout());
                }
                return true;
            }
        });
        return true;
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
    protected abstract int getInfoLayout();
    protected abstract String getBarTitle();
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
    public Event retrieveEventFromView(){
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

    @Override
    public View getRootView() {
        return rootView;
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
}
