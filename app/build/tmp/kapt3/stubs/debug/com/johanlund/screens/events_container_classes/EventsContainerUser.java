package com.johanlund.screens.events_container_classes;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J(\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000fH&J\b\u0010\u0010\u001a\u00020\u0003H&\u00a8\u0006\u0011"}, d2 = {"Lcom/johanlund/screens/events_container_classes/EventsContainerUser;", "Lcom/johanlund/screens/events_container_classes/common/EventAdapter$EventAdapterUser;", "bindChangedEventToList", "", "event", "Lcom/johanlund/base_classes/Event;", "posInList", "", "bindEventToList", "changeEventActivity", "eventType", "valueToReturn", "executeChangedEvent", "requestCode", "data", "Landroid/content/Intent;", "updateTagsInListOfEventsAfterTagTemplateChange", "app_debug"})
public abstract interface EventsContainerUser extends com.johanlund.screens.events_container_classes.common.EventAdapter.EventAdapterUser {
    
    public abstract void bindEventToList(@org.jetbrains.annotations.NotNull()
    com.johanlund.base_classes.Event event);
    
    public abstract void bindChangedEventToList(@org.jetbrains.annotations.NotNull()
    com.johanlund.base_classes.Event event, int posInList);
    
    public abstract void changeEventActivity(@org.jetbrains.annotations.NotNull()
    com.johanlund.base_classes.Event event, int eventType, int valueToReturn, int posInList);
    
    public abstract void executeChangedEvent(int requestCode, @org.jetbrains.annotations.NotNull()
    android.content.Intent data);
    
    public abstract void updateTagsInListOfEventsAfterTagTemplateChange();
}