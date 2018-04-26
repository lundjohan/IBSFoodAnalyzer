package com.johanlund.statistics_time;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.johanlund.base_classes.Break;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_adapters.TimeStatAdapter;
import com.johanlund.statistics_general.StatBaseActivity;
import com.johanlund.statistics_general.TimeStatAsyncTask;
import com.johanlund.statistics_point_classes.TimePoint;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.ScoreTimesBase;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.List;

import static com.johanlund.constants.Constants.DATE_TO_START_DIARY;

/**
 * Created by Johan on 2018-03-17.
 */

public abstract class TimeStatActivity extends StatBaseActivity<TimePoint> implements TimeStatAdapter.TimeStatAdapterUser{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_stat);
        recyclerView = (RecyclerView) findViewById(R.id.time_stat_table);
        continueOnCreate();
    }

    @Override
    public TimeStatAdapter getStatAdapter(){
        return new TimeStatAdapter(getScoreWrapper(), this);
    }

    @Override
    public void restartDiaryAtDate(LocalDate ld){
        Intent intent = getIntent();
        intent.putExtra(DATE_TO_START_DIARY, ld);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    protected void calculateStats() {
        List<LocalDateTime>allBreaks = Break.getAllBreaks(getApplicationContext());
        List<ScoreTimesBase> stb = getScoreTimesBases(allBreaks);
        TimeStatAsyncTask asyncThread = new TimeStatAsyncTask(adapter, recyclerView);
        asyncThread.execute(getScoreWrapper(), stb);
    }

    protected abstract List<ScoreTimesBase> getScoreTimesBases(List<LocalDateTime> allBreaks);
}
