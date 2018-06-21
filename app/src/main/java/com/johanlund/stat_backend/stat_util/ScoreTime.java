package com.johanlund.stat_backend.stat_util;

import org.threeten.bp.LocalDateTime;

public class ScoreTime {
    private LocalDateTime datetime;
    private int score;

    public ScoreTime(LocalDateTime datetime, int score) {
        this.datetime = datetime;
        this.score = score;
    }

    public LocalDateTime getTime() {
        return datetime;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return datetime.toString();
    }
}
