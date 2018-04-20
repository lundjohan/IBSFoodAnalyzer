package com.johanlund.util;

import org.threeten.bp.LocalDateTime;

public class CompleteTime {
    private LocalDateTime datetime;
    private int complete;

    public CompleteTime(LocalDateTime datetime, int complete) {
        this.datetime = datetime;
        this.complete = complete;
    }

    public LocalDateTime getTime() {
        return datetime;
    }

    public int getComplete() {
        return complete;
    }
}
