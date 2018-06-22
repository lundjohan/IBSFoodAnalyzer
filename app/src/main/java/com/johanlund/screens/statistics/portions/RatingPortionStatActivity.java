package com.johanlund.screens.statistics.portions;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Tag;
import com.johanlund.dao.Dao;
import com.johanlund.dao.SqLiteDao;
import com.johanlund.database.DBHandler;
import com.johanlund.external_classes.TinyDB;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.statistics.avg_stat.common.TagsWrapper;
import com.johanlund.screens.statistics.common.StatAsyncTask;
import com.johanlund.screens.statistics.portions.common.PortionStatActivity;
import com.johanlund.screens.statistics.portions.common.PortionStatAdapter;
import com.johanlund.screens.statistics.portions_settings.PortionStatRange;
import com.johanlund.stat_backend.portion_scorewrapper.PortionScoreWrapper;
import com.johanlund.stat_backend.portion_scorewrapper.RatingPortionScoreWrapper;
import com.johanlund.stat_backend.stat_util.ScoreTime;
import com.johanlund.stat_backend.stat_util.TagsWrapperBase;

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
        List<PortionStatRange> ranges = tinydb.getListPortionRange(getResources().getString(R
                .string.portions_ranges_key));
        //look at getScoreWrapper from Time... to see what to do here
        int waitHoursAfterMeal = preferences.getInt(getResources().getString(R.string
                .portions_rating_pref_wait_hours_key), 0);
        int validHours = preferences.getInt(getResources().getString(R.string
                .portions_rating_pref_valid_hours_key), 24);
        int minHoursBetweenMeals = preferences.getInt(getResources().getString(R.string
                .portions_pref_min_hours_between_meals), 0);
        return new RatingPortionScoreWrapper(ranges, waitHoursAfterMeal, validHours,
                minHoursBetweenMeals);
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
        Dao dao = new SqLiteDao(getApplicationContext());
        List<Tag> tags = dao.getAllTagsWithTime();
        List<ScoreTime> ratings = dbHandler.getRatingTimes();
        List<LocalDateTime> allBreaks = Break.getAllBreaks(getApplicationContext());

        return TagsWrapper.makeTagsWrappers(tags, ratings,
                allBreaks);
    }
}
