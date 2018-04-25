package com.johanlund.statistics_avg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Tag;
import com.johanlund.database.DBHandler;
import com.johanlund.statistics_adapters.BmAvgStatAdapter;
import com.johanlund.statistics_adapters.AvgStatAdapter;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_avg_scorewrapper.AvgScoreWrapper;
import com.johanlund.statistics_avg_scorewrapper.BristolAvgScoreWrapper;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.ScoreTimesBase;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import static com.johanlund.constants.Constants.HOURS_AHEAD_FOR_BM;

public class BristolAvgStatActivity extends AvgStatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<TagsWrapperBase> getTagsWrapperBase() {
        DBHandler dbHandler = new DBHandler(getApplicationContext());

        List<Tag>tags = dbHandler.getAllTags();
        List <ScoreTime> bristolTimes = dbHandler.getBristolTimes();
        List<LocalDateTime>allBreaks = Break.getAllBreaks(getApplicationContext());
        return BmsWrapper.makeBmsWrappers(tags, bristolTimes, allBreaks);
    }

    @Override
    public AvgScoreWrapper getScoreWrapper() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        int furthest_distance_hours_before_bm_limit = preferences.getInt(getResources().getString(R.string
                .hours_before_bm_furthest_distance_limit), 0);
        int shortest_distance_hours_before_bm_limit = preferences.getInt(getResources().getString(R.string
                        .hours_before_bm_closest_distance_limit),

                HOURS_AHEAD_FOR_BM);
        int quantLimit = preferences.getInt(getResources().getString(R.string
                .avg_bm_pref_quant_key), 0);
        return getBMScoreWrapper(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
    }

    /**
     * Perhaps overkill, but reduces code for CompleteAvgStatActivity. No it doesnt anymore... Remove?
     */
    protected BristolAvgScoreWrapper getBMScoreWrapper(int furthest_distance_hours_before_bm_limit, int shortest_distance_hours_before_bm_limit, int quantLimit) {
        return new BristolAvgScoreWrapper(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
    }


    @Override
    public AvgStatAdapter getStatAdapter() {
        return new BmAvgStatAdapter(getScoreWrapper());
    }

    @Override
    public String getStringForTitle() {
        return "Bristol Type";
    }

    @Override
    protected String getInfoStr() {
        return getResources().getString(R.string.bristol_info_score);
    }

}
