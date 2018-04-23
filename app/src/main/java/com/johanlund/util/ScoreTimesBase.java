package com.johanlund.util;

import java.util.List;

abstract class ScoreTimesBase{
    private List<CompleteTime>scoreTimes;
    public ScoreTimesBase(List<CompleteTime> scoreTimes) {
        this.scoreTimes = scoreTimes;
    }
    public List<CompleteTime> getScoreTimes() {
        return scoreTimes;
    }
}
