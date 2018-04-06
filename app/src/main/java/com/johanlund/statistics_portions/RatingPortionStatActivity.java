package com.johanlund.statistics_portions;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.external_classes.TinyDB;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_adapters.AvgStatAdapter;
import com.johanlund.statistics_adapters.BmAvgStatAdapter;
import com.johanlund.statistics_adapters.PortionStatAdapter;
import com.johanlund.statistics_general.StatAdapter;
import com.johanlund.statistics_portion_scorewrapper.PortionScoreWrapper;
import com.johanlund.statistics_portion_scorewrapper.RatingPortionScoreWrapper;
import com.johanlund.statistics_settings_portions.PortionStatRange;

import java.util.List;

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
        TinyDB tinydb = new TinyDB(getApplicationContext());
        List<PortionStatRange> ranges= tinydb.getListPortionRange(getResources().getString(R.string.portions_ranges_key));
        //look at getScoreWrapper from Time... to see what to do here
        int waitHoursAfterMeal = preferences.getInt(getResources().getString(R.string.portions_rating_pref_wait_hours_key),0);
        int validHours = preferences.getInt(getResources().getString(R.string.portions_rating_pref_valid_hours_key), 24);
        int minHoursBetweenMeals = preferences.getInt(getResources().getString(R.string.portions_pref_min_hours_between_meals),0);
        return new RatingPortionScoreWrapper(ranges, waitHoursAfterMeal, validHours, minHoursBetweenMeals);
    }

    @Override
    public PortionStatAdapter getStatAdapter() {
        return new PortionStatAdapter(getScoreWrapper());
    }

}
