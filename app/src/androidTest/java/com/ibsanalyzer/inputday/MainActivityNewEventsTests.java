package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ibsanalyzer.constants.Constants.RETURN_BM_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_OTHER_SERIALIZABLE;
import static com.ibsanalyzer.constants.Constants.RETURN_RATING_SERIALIZABLE;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Johan on 2017-05-23.
 * <p>
 * <p>
 * This Test class will test that behaviour of MainActivity with its child DiaryFragment is
 * behaving ok
 * relative to child Activities.
 * <p>
 * All other Activities (except for MainActivity) are stubbed out.
 * <p>
 * <p>
 * Based on end of https://guides.codepath.com/android/UI-Testing-with-Espresso
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityNewEventsTests {

    @Rule
    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);
    List<Tag> tags = new ArrayList<>();
    LocalDateTime ldt;

    public static Instrumentation.ActivityResult buildAnIntentResult(String putExtraStr, Event event) {
        Intent resultData = new Intent();
        resultData.putExtra(putExtraStr, event);
        return new Instrumentation.ActivityResult(Activity
                .RESULT_OK, resultData);
    }

    private void stubOutActivity(String className, Instrumentation.ActivityResult result) {
        intending(IntentMatchers.
                hasComponent(hasClassName(className))).respondWith(result);
    }

    @Test
    public void testReturnValueFromMealActivity() {
        ldt = LocalDateTime.of(2017, Month.MAY, 23, 16, 0);
        // Create a meal object
        Tag sugar = new Tag(ldt, "banana", 2.0);
        Tag honey = new Tag(ldt, "honey", 1.0);
        tags.add(sugar);
        tags.add(honey);
        Meal meal = new Meal(ldt, tags, 1.0);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_MEAL_SERIALIZABLE, meal);
        stubOutActivity(MealActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        onView(withId(R.id.mealBtn)).perform(click());

        //check that an item exists in RecyclerView that has Time 16:00 and portions 2.5
        onView(allOf(

                //all meal items has portions id
                withId(R.id.portions),

                //timeView
                hasSibling(withText("16:00")),

                //nr of portions
                hasSibling(withText("1.0")),

                //tags in item
                hasSibling(withChild(withText("banana"))), hasSibling(withChild(withText("honey")
                ))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testReturnValueFromOtherActivity() {
        ldt = LocalDateTime.of(2017, Month.MAY, 23, 23, 22);

        tags.add(new Tag(ldt, "happy", 1.0));
        Other other = new Other(ldt, tags);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_OTHER_SERIALIZABLE,
                other);
        stubOutActivity(OtherActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        onView(withId(R.id.otherBtn)).perform(click());

        //check that an item exists in RecyclerView that has Time 16:00 and portions 2.5
        onView(allOf(

                //all meal items has portions id
                withId(R.id.smallOtherIcon),

                //timeView
                hasSibling(withText("23:22")),

                //tags in item
                hasSibling(withChild(withText("happy"))
                )))
                .check(matches(isDisplayed()));
    }

    // TODO Othertest  (but it so similar to Meal so maybe I don't have to do it), but should
    // make Bm and Rating at least
    @Test
    public void testReturnValueFromExerciseActivity() {
        // Create a meal object with timeView 16:00
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 23, 17, 30);
        Tag running = new Tag(ldt, "running", 1.0);

        //a 4 means Intense
        Exercise exercise = new Exercise(ldt, running, 4);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_EXERCISE_SERIALIZABLE,
                exercise);
        stubOutActivity(ExerciseActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        onView(withId(R.id.exerciseBtn)).perform(click());

        //check that a relevant item exists in RecyclerView
        onView(allOf(
                //use the image
                withId(R.id.smallExerciseIcon),
                //withText("17:30"),
                //timeView
                hasSibling(withText("17:30")),

                //tag
                hasSibling(withText("running")),

                //intensity
                hasSibling(withText("Intense"))
        ))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testReturnValueFromBMActivity() {
        // Create a meal object with timeView 16:00
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 23, 18, 0);
        Bm bm = new Bm(ldt, 5, 2);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_BM_SERIALIZABLE,
                bm);
        stubOutActivity(BmActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        onView(withId(R.id.bmBtn)).perform(click());

        //check that a relevant item exists in RecyclerView
        onView(allOf(
                //use the image
                withId(R.id.smallBmIcon),
                //timeView
                hasSibling(withText("18:00")),

                //tag
                hasSibling(withText("Phenomenal")),

                //intensity
                hasSibling(withText("2"))
        ))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testReturnValueFromRatingActivity() {
        // Create a meal object with timeView 16:00
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 23, 19, 0);
        Rating rating = new Rating(ldt, 6);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_RATING_SERIALIZABLE,
                rating);
        stubOutActivity(RatingActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        onView(withId(R.id.ratingBtn)).perform(click());

        //check that a relevant item exists in RecyclerView
        onView(allOf(
                //use the image
                withId(R.id.smallRatingIcon),
                //timeView
                hasSibling(withText("19:00")),

                //rating/after
                hasSibling(withText("Great"))
        ))
                .check(matches(isDisplayed()));
    }
}
