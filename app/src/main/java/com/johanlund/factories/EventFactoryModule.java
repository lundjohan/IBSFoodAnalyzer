package com.johanlund.factories;

import dagger.Module;
import dagger.Provides;

@Module
public class EventFactoryModule {

    @Provides
    EventFactory providesEventFactory() {
        return new EventFactoryImpl();
    }
}
