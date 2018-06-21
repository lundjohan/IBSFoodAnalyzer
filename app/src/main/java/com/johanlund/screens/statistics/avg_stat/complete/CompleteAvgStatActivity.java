package com.johanlund.screens.statistics.avg_stat.complete;

import android.os.Bundle;

import com.johanlund.base_classes.Tag;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.model.EventManager;
import com.johanlund.screens.statistics.avg_stat.bristol.BristolAvgStatActivity;
import com.johanlund.screens.statistics.avg_stat.common.BmsWrapper;
import com.johanlund.stat_backend.avg_scorewrapper.BristolAvgScoreWrapper;
import com.johanlund.stat_backend.avg_scorewrapper.CompleteAvgScoreWrapper;
import com.johanlund.stat_backend.stat_util.ScoreTime;
import com.johanlund.stat_backend.stat_util.TagsWrapperBase;

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
        EventManager em = new EventManager(getApplicationContext());
        List<Tag>tags = em.getAllTagsWithTime();
        List <ScoreTime> completeBms = em.getCompleteTimes();
        List<LocalDateTime>allBreaks = em.getAllBreaks();
        return BmsWrapper.makeBmsWrappers(tags, completeBms, allBreaks);
    }
}
