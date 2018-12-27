package com.johanlund.screens.events_templates_actions.mvc_views;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003:\u0001\u0017J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0010\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\nH&J\b\u0010\f\u001a\u00020\rH&J \u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0010\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0016H&\u00a8\u0006\u0018"}, d2 = {"Lcom/johanlund/screens/events_templates_actions/mvc_views/EventsTemplateViewMvc;", "Lcom/johanlund/screens/common/mvcviews/WithOptionsMenuViewMvc;", "Lcom/johanlund/screens/events_container_classes/EventsContainerUser;", "Lcom/johanlund/screens/events_container_classes/common/mvcviews/EventButtonsViewMvc;", "bindDateToView", "", "date", "Lorg/threeten/bp/LocalDate;", "bindEventsTemplateToView", "et", "Lcom/johanlund/model/EventsTemplate;", "createEventsTemplateFromView", "getEventsContainer", "Lcom/johanlund/screens/events_container_classes/common/EventsContainer;", "handleEcOnActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "setListener", "listener", "Lcom/johanlund/screens/events_templates_actions/mvc_views/EventsTemplateViewMvc$Listener;", "Listener", "app_release"})
public abstract interface EventsTemplateViewMvc extends com.johanlund.screens.common.mvcviews.WithOptionsMenuViewMvc, com.johanlund.screens.events_container_classes.EventsContainerUser, com.johanlund.screens.events_container_classes.common.mvcviews.EventButtonsViewMvc {
    
    public abstract void bindEventsTemplateToView(@org.jetbrains.annotations.NotNull()
    com.johanlund.model.EventsTemplate et);
    
    public abstract void bindDateToView(@org.jetbrains.annotations.NotNull()
    org.threeten.bp.LocalDate date);
    
    public abstract void setListener(@org.jetbrains.annotations.NotNull()
    com.johanlund.screens.events_templates_actions.mvc_views.EventsTemplateViewMvc.Listener listener);
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.johanlund.model.EventsTemplate createEventsTemplateFromView();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.johanlund.screens.events_container_classes.common.EventsContainer getEventsContainer();
    
    public abstract void handleEcOnActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.NotNull()
    android.content.Intent data);
    
    @kotlin.Metadata(mv = {1, 1, 10}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0007H&J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\fH&J\u0018\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0010H&J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\fH&\u00a8\u0006\u0013"}, d2 = {"Lcom/johanlund/screens/events_templates_actions/mvc_views/EventsTemplateViewMvc$Listener;", "Lcom/johanlund/screens/common/mvcviews/ViewMvc$Listener;", "changeEventActivity", "", "event", "Lcom/johanlund/base_classes/Event;", "eventType", "", "valueToReturn", "posInList", "completeSession", "finalEventsTemplate", "Lcom/johanlund/model/EventsTemplate;", "executeChangedEvent", "requestCode", "data", "Landroid/content/Intent;", "removeTagTypesThatDontExist", "et", "app_release"})
    public static abstract interface Listener extends com.johanlund.screens.common.mvcviews.ViewMvc.Listener {
        
        public abstract void completeSession(@org.jetbrains.annotations.NotNull()
        com.johanlund.model.EventsTemplate finalEventsTemplate);
        
        @org.jetbrains.annotations.NotNull()
        public abstract com.johanlund.model.EventsTemplate removeTagTypesThatDontExist(@org.jetbrains.annotations.NotNull()
        com.johanlund.model.EventsTemplate et);
        
        public abstract void changeEventActivity(@org.jetbrains.annotations.NotNull()
        com.johanlund.base_classes.Event event, int eventType, int valueToReturn, int posInList);
        
        public abstract void executeChangedEvent(int requestCode, @org.jetbrains.annotations.NotNull()
        android.content.Intent data);
    }
}