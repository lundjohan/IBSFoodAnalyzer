package com.johanlund.screens.statistics.avg_stat.complete;

import android.os.Bundle;

import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Tag;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.statistics.avg_stat.common.BmsWrapper;
import com.johanlund.screens.statistics.avg_stat.bristol.BristolAvgStatActivity;
import com.johanlund.stat_backend.avg_scorewrapper.BristolAvgScoreWrapper;
import com.johanlund.stat_backend.avg_scorewrapper.CompleteAvgScoreWrapper;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class CompleteAvgStatActivity extends BristolAvgStatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected BristolAvgScoreWrapper getBMScoreWrapper(int furthest_distance_hours_before_bm_limit, int shortest_distance_hours_before_bm_limit, int quantLimit) {
        return new CompleteAvgScoreWrapper(furthest_distance_hours_before_bm_limit, shortest_distance_hours_before_bm_limit, quantLimit);
    }

    @Override
    public String getStringForTitle() {
        return "Complete Score";
    }

    @Override
    protected String getInfoStr() {
        return getResources().getString(R.string.complete_info_score);
    }

    @Override
    protected List<TagsWrapperBase> getTagsWrapperBase() {
        DBHandler dbHandler = new DBHandler(getApplicationContext());

        List<Tag>tags = dbHandler.getAllTags();
        List <ScoreTime> completeBms = dbHandler.getCompleteTimes();
        List<LocalDateTime>allBreaks = Break.getAllBreaks(getApplicationContext());
        return BmsWrapper.makeBmsWrappers(tags, completeBms, allBreaks);
    }
}
