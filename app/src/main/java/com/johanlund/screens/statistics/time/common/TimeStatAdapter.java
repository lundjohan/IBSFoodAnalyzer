package com.johanlund.screens.statistics.time.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johanlund.date_time.DateTimeFormat;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.statistics.common.ScoreWrapperBase;
import com.johanlund.screens.statistics.common.StatAdapter;
import com.johanlund.stat_backend.point_classes.TimePoint;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * Created by Johan on 2018-03-20.
 * <p>
 * This class is very close to AvgStatAdapter. Using generics? (setList(List<E>) etc...)
 */

public class TimeStatAdapter extends StatAdapter<TimePoint> {
    protected ScoreWrapperBase<TimePoint> timeScoreWrapper;
    private TimeStatAdapterUser callBack;

    public TimeStatAdapter(ScoreWrapperBase<TimePoint> timeScoreWrapper, TimeStatAdapterUser
            callBack) {
        this.timeScoreWrapper = timeScoreWrapper;
        this.callBack = callBack;
    }

    //temp
    public void setTimePointsList(List<TimePoint> pointList) {
        this.pointList = pointList;
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
        TimePoint tp = pointList.get(position);
        viewHolder.startTime.setText(DateTimeFormat.toSpreadSheetDateTimeFormat(tp.getStartTime()));
        viewHolder.duration.setText(Long.toString(tp.getDurationInHours()));
    }

    @Override
    public int getItemCount() {
        return pointList.size();
    }

    public interface TimeStatAdapterUser {
        void restartDiaryAtDate(LocalDate ld);
    }

    /*@Override
    public void setPointsList(List<TimePoint>pointList){
        this.pointList = pointList;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView startTime;
        public TextView duration;
        public TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView) itemView.findViewById(R.id.stat_time_start);
            duration = (TextView) itemView.findViewById(R.id.stat_time_duration);

            //make startTime clickable
            startTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = startTime.getText().toString();
                    LocalDate ld = DateTimeFormat.fromSpreadSheetDateTimeFormat(str).toLocalDate();
                    callBack.restartDiaryAtDate(ld);
                }
            });
        }
    }
}
