package com.johanlund.util;

import java.util.List;

abstract class ScoreTimesBase{
    private List<ScoreTime>scoreTimes;
    public ScoreTimesBase(List<ScoreTime> scoreTimes) {
        this.scoreTimes = scoreTimes;
    }
    public List<ScoreTime> getScoreTimes() {
        return scoreTimes;
    }
}
