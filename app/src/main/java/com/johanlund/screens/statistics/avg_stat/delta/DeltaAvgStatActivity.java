package com.johanlund.screens.statistics.avg_stat.delta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Tag;
import com.johanlund.dao.Dao;
import com.johanlund.dao.SqLiteDao;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.statistics.avg_stat.common.AvgStatActivity;
import com.johanlund.screens.statistics.avg_stat.common.AvgStatAdapter;
import com.johanlund.screens.statistics.avg_stat.common.TagsWrapper;
import com.johanlund.stat_backend.avg_scorewrapper.AvgScoreWrapper;
import com.johanlund.stat_backend.avg_scorewrapper.DeltaAvgScoreWrapper;
import com.johanlund.stat_backend.stat_util.ScoreTime;
import com.johanlund.stat_backend.stat_util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import static com.johanlund.constants.Constants.HOURS_AHEAD_FOR_AVG;

public class DeltaAvgStatActivity extends AvgStatActivity {
    @Override
    protected String getInfoStr() {
        return "This Stat class takes the avg score - the avg score that was just before start. " +
                "\n Max value: +5, min value: -5";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    //copied from RatingAvgStatActivity
    @Override
    protected List<TagsWrapperBase> getTagsWrapperBase() {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        Dao dao = new SqLiteDao(getApplicationContext());
        List<Tag> tags = dao.getAllTagsWithTime();
        List<ScoreTime> ratings = dbHandler.getRatingTimes();
        List<LocalDateTime> allBreaks = Break.getAllBreaks(getApplicationContext());

        return TagsWrapper.makeTagsWrappers(tags, ratings,
                allBreaks);
    }

    @Override
    public AvgScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int wait_hours_after_event = preferences.getInt(getResources().getString(R.string
                .avg_rating_pref_wait_key), 0);
        int hours_ahead_for_av = preferences.getInt(getResources().getString(R.string
                .avg_rating_pref_stop_key), HOURS_AHEAD_FOR_AVG);
        int quantLimit = preferences.getInt(getResources().getString(R.string
                .avg_rating_pref_quant_key), 0);
        return new DeltaAvgScoreWrapper(wait_hours_after_event, hours_ahead_for_av, quantLimit);
    }

    @Override
    public AvgStatAdapter getStatAdapter() {
        return new AvgStatAdapter(getScoreWrapper());
    }

    @Override
    public String getStringForTitle() {
        return "Delta Score";
    }
}