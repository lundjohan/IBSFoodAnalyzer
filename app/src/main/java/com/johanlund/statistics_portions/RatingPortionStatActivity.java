package com.johanlund.statistics_portions;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_portion_scorewrapper.PortionScoreWrapper;
import com.johanlund.statistics_time_scorewrapper.RatingTimeScoreWrapper;

public class RatingPortionStatActivity extends PortionStatActivity {
    @Override
    protected String getInfoStr() {
        return "This is info about Rating Portion Stat";
    }

    @Override
    public String getStringForTitle() {
        return "Rating Portion Stat";
    }

    @Override
    public PortionScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        //look at getScoreWrapper from Time... to see what to do here
        return new PortionScoreWrapper();
    }
}
