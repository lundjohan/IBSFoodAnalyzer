package com.johanlund.screens.statistics.time;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Break;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.stat_backend.stat_util.ScoreTime;
import com.johanlund.stat_backend.stat_util.ScoreTimesBase;
import com.johanlund.stat_backend.time_scorewrapper.RatingTimeScoreWrapper;
import com.johanlund.stat_backend.time_scorewrapper.TimeScoreWrapper;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by Johan on 2018-03-13.
 */

public class RatingTimeStatActivity extends TimeStatActivity {
    @Override
    protected String getInfoStr() {
        return "This is info about Rating Time Stat";
    }

    @Override
    public String getStringForTitle() {
        return "Rating Time Stat";
    }

    @Override
    public TimeScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int ratingStart = preferences.getInt(getResources().getString(R.string.time_rating_start)
                , 5);
        int ratingEnd = preferences.getInt(getResources().getString(R.string.time_rating_end), 7);
        int durationLimit = preferences.getInt(getResources().getString(R.string
                .time_rating_duration_key), 0);
        return new RatingTimeScoreWrapper(ratingStart, ratingEnd, durationLimit);
    }

    @Override
    public List<ScoreTimesBase> getScoreTimesBases(List<LocalDateTime> allBreaks) {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        //we need the time of the last event (since breaks might not cover the very end)
        LocalDateTime lastTime = dbHandler.getTimeOfLastEvent();
        allBreaks.add(lastTime);

        List<ScoreTime> sts = dbHandler.getRatingTimes();

        return Break.getRatingTimes(sts, allBreaks);
    }
}
