package com.johanlund.util;

import org.threeten.bp.LocalDateTime;

public class ScoreTime {
    private LocalDateTime datetime;
    private int complete;

    public ScoreTime(LocalDateTime datetime, int complete) {
        this.datetime = datetime;
        this.complete = complete;
    }

    public LocalDateTime getTime() {
        return datetime;
    }

    public int getScore() {
        return complete;
    }
}
