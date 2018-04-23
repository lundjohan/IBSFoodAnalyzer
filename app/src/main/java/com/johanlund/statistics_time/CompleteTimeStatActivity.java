package com.johanlund.statistics_time;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Break;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_general.TimeStatAsyncTask;
import com.johanlund.statistics_time_scorewrapper.CompleteTimeScoreWrapper;
import com.johanlund.statistics_time_scorewrapper.TimeScoreWrapper;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.ScoreTimesBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by Johan on 2018-03-13.
 */

public class CompleteTimeStatActivity extends TimeStatActivity  {
    @Override
    protected String getInfoStr() {
        return "Complete Time Stat helps you go to the bowel movement periods of a certain score interval. ";
    }

    @Override
    public String getStringForTitle() {
        return "Completeness Time Stat";
    }


    @Override
    public TimeScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int ratingStart = preferences.getInt(getResources().getString(R.string.time_complete_start),4);
        int ratingEnd = preferences.getInt(getResources().getString(R.string.time_complete_end), 5);
        return new CompleteTimeScoreWrapper(ratingStart,ratingEnd);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public List<ScoreTimesBase> getScoreTimesBases(List<LocalDateTime> allBreaks) {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        List<ScoreTime> sts = dbHandler.getCompleteTimes();
        return Break.getCompleteTimes(sts, allBreaks);
    }
}
