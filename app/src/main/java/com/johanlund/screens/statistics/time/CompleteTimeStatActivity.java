package com.johanlund.screens.statistics.time;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Break;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.stat_backend.stat_util.ScoreTime;
import com.johanlund.stat_backend.stat_util.ScoreTimesBase;
import com.johanlund.stat_backend.time_scorewrapper.BmTimeScoreWrapper;
import com.johanlund.stat_backend.time_scorewrapper.TimeScoreWrapper;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by Johan on 2018-03-13.
 */

public class CompleteTimeStatActivity extends TimeStatActivity {
    @Override
    protected String getInfoStr() {
        return "Complete Time Stat helps you go to the bowel movement periods of a certain score " +
                "interval. ";
    }

    @Override
    public String getStringForTitle() {
        return "Completeness Time Stat";
    }


    @Override
    public TimeScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int ratingStart = preferences.getInt(getResources().getString(R.string
                .time_complete_start), 4);
        int ratingEnd = preferences.getInt(getResources().getString(R.string.time_complete_end), 5);
        return new BmTimeScoreWrapper(ratingStart, ratingEnd);
    }

    @Override
    public List<ScoreTimesBase> getScoreTimesBases(List<LocalDateTime> allBreaks) {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        List<ScoreTime> sts = dbHandler.getCompleteTimes();
        return Break.getBmTimes(sts, allBreaks);
    }
}
