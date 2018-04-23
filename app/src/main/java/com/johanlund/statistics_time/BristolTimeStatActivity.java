package com.johanlund.statistics_time;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Break;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_time_scorewrapper.BmTimeScoreWrapper;
import com.johanlund.statistics_time_scorewrapper.TimeScoreWrapper;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.ScoreTimesBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by Johan on 2018-03-13.
 */

public class BristolTimeStatActivity extends TimeStatActivity  {
    @Override
    protected String getInfoStr() {
        return "Bristol Time Stat helps you see when your bowel movements have been in a certain interval on the Bristol Scale (= texture of poop).";
    }

    @Override
    public String getStringForTitle() {
        return "Bristol Time Stat";
    }


    @Override
    public TimeScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int ratingStart = preferences.getInt(getResources().getString(R.string.time_bristol_start),4);
        int ratingEnd = preferences.getInt(getResources().getString(R.string.time_bristol_end), 4);
        return new BmTimeScoreWrapper(ratingStart,ratingEnd);
    }

    @Override
    public List<ScoreTimesBase> getScoreTimesBases(List<LocalDateTime> allBreaks) {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        List<ScoreTime> sts = dbHandler.getBristolTimes();
        //this works for bristol too
        return Break.getBmTimes(sts, allBreaks);
    }
}
