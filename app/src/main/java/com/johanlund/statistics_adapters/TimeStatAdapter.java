package com.johanlund.statistics_adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johanlund.date_time.DateTimeFormat;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.statistics_time_scorewrapper.TimeScoreWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2018-03-20.
 *
 * This class is very close to AvgStatAdapter. Using generics? (setList(List<E>) etc...)
 */

public class TimeStatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<TimePoint> timePointsList = new ArrayList<>();
    protected TimeScoreWrapper timeScoreWrapper;
    public TimeStatAdapter(TimeScoreWrapper timeScoreWrapper) {
        this.timeScoreWrapper = timeScoreWrapper;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stat_time, parent,
                false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        TimePoint tp = timePointsList.get(position);
        viewHolder.startTime.setText(DateTimeFormat.toSpreadSheetDateTimeFormat(tp.getStartTime()));
        viewHolder.duration.setText(Long.toString(tp.getDurationInHours()));
    }

    @Override
    public int getItemCount() {
        return timePointsList.size();
    }

    public void setTimePointsList(List<TimePoint>timePointsList){
        this.timePointsList = timePointsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView startTime;
        public TextView duration;
        public TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView) itemView.findViewById(R.id.stat_time_start);
            duration = (TextView) itemView.findViewById(R.id.stat_time_duration);
        }
    }
}
