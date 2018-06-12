package com.johanlund.factories;

import com.johanlund.base_classes.Event;

import org.threeten.bp.LocalDateTime;

public interface EventFactory {
    Event makeDummyEventWithTime(LocalDateTime ldt, int eventType);
}
