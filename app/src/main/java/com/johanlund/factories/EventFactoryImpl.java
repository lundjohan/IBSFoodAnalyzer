package com.johanlund.factories;

import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Exercise;
import com.johanlund.base_classes.Meal;
import com.johanlund.base_classes.Other;
import com.johanlund.base_classes.Rating;
import com.johanlund.base_classes.TagWithoutTime;
import com.johanlund.exceptions.InvalidEventType;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;

import static com.johanlund.constants.Constants.BM;
import static com.johanlund.constants.Constants.EXERCISE;
import static com.johanlund.constants.Constants.MEAL;
import static com.johanlund.constants.Constants.OTHER;
import static com.johanlund.constants.Constants.RATING;

public class EventFactoryImpl implements EventFactory {
    @Override
    public Event makeDummyEventWithTime(LocalDateTime ldt, int eventType) {
        switch (eventType) {
            case MEAL: {
                return new Meal(ldt, new ArrayList<TagWithoutTime>(), 1.0);
            }
            case OTHER: {
                return new Other(ldt, new ArrayList<TagWithoutTime>());
            }
            case EXERCISE: {
                return new Exercise(ldt, new TagWithoutTime("exercise type", 1.0), 3);
            }
            case BM: {
                return new Bm(ldt, 1, 1);
            }
            case RATING: {
                return new Rating(ldt, "", 1);
            }
            default: {
                throw new InvalidEventType(Integer.toString(eventType));
            }
        }
    }
}
