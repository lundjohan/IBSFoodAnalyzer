package com.johanlund.util;

import java.util.List;

public abstract class ScoreTimesBase{
    private List<ScoreTime>scoreTimes;
    public ScoreTimesBase(List<ScoreTime> scoreTimes) {
        this.scoreTimes = scoreTimes;
    }
    public List<ScoreTime> getScoreTimes() {
        return scoreTimes;
    }
}
