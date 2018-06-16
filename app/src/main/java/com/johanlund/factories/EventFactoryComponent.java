package com.johanlund.factories;

import com.johanlund.screens.event_activities.mvc_controllers.NewEventActivity;

import dagger.Component;

@Component(modules = EventFactoryModule.class)
public interface EventFactoryComponent {
    void inject(NewEventActivity obj);
}
