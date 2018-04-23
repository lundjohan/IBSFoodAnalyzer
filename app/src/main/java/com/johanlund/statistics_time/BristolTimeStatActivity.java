package com.johanlund.statistics_time;

import com.johanlund.statistics_general.ScoreWrapperBase;
import com.johanlund.statistics_time_scorewrapper.TimeScoreWrapper;
import com.johanlund.util.ScoreTimesBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;

/**
 * Created by Johan on 2018-03-13.
 */

public class BristolTimeStatActivity extends TimeStatActivity  {
    @Override
    public List<ScoreTimesBase> getScoreTimesBases(List<LocalDateTime> allBreaks) {
        return null;
    }

    @Override
    protected String getInfoStr() {
        return null;
    }

    @Override
    public String getStringForTitle() {
        return null;
    }

    @Override
    public ScoreWrapperBase getScoreWrapper() {
        return null;
    }
}
