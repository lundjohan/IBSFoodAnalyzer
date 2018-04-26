package com.johanlund.statistics_portions;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Tag;
import com.johanlund.database.DBHandler;
import com.johanlund.external_classes.TinyDB;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_adapters.PortionStatAdapter;
import com.johanlund.statistics_avg.TagsWrapper;
import com.johanlund.statistics_general.StatAsyncTask;
import com.johanlund.statistics_portion_scorewrapper.PortionScoreWrapper;
import com.johanlund.statistics_portion_scorewrapper.RatingPortionScoreWrapper;
import com.johanlund.statistics_settings_portions.PortionStatRange;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.ScoreTimesBase;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

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

    @Override
    protected void calculateStats() {
        List<TagsWrapperBase> twbs = getTagsWrapperBase();
        StatAsyncTask asyncThread = new StatAsyncTask(this, adapter, recyclerView);
        asyncThread.execute(getScoreWrapper(), twbs);
    }
    //copied from RatingAvgStatActivity
    protected List<TagsWrapperBase> getTagsWrapperBase() {
        DBHandler dbHandler = new DBHandler(getApplicationContext());

        List<Tag>tags = dbHandler.getAllTags();
        List <ScoreTime> ratings = dbHandler.getRatingTimes();
        List<LocalDateTime>allBreaks = Break.getAllBreaks(getApplicationContext());

        return TagsWrapper.makeTagsWrappers(tags, ratings,
                allBreaks);
    }
}
