package com.johanlund.statistics_time;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_time_scorewrapper.RatingTimeScoreWrapper;
import com.johanlund.statistics_time_scorewrapper.TimeScoreWrapper;

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
        int ratingStart = preferences.getInt(getResources().getString(R.string.time_rating_start),5);
        int ratingEnd = preferences.getInt(getResources().getString(R.string.time_rating_end), 7);
        int durationLimit = preferences.getInt(getResources().getString(R.string.time_rating_duration_key),0);
        return new RatingTimeScoreWrapper(ratingStart,ratingEnd, durationLimit);
    }
}
