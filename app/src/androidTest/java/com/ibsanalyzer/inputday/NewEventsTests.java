package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Tag;

import org.junit.Rule;
import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ibsanalyzer.constants.Constants.RETURN_MEAL_SERIALIZABLE;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Johan on 2017-05-23.
 * <p>
 * <p>
 * This Test class will test that behaviour of MainActivity with its child DiaryFragment is behaving ok
 * relative to child Activities.
 * <p>
 * All other Activities (except for MainActivity) are stubbed out.
 * <p>
 * <p>
 * Based on end of https://guides.codepath.com/android/UI-Testing-with-Espresso
 */

public class NewEventsTests {
    @Rule
    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);


    @Test
    public void testReturnValueFromMealActivity() {
        //first check that mealBtn is shown
        onView(withId(R.id.mealBtn)).check(matches(isDisplayed()));


        // Create a meal object
        List<Tag> tags = new ArrayList<>();
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 23, 16, 0);
        Tag sugar = new Tag(ldt, "banana", 2.0);
        Tag honey = new Tag(ldt, "honey", 1.0);
        tags.add(sugar);
        tags.add(honey);
        Meal meal = new Meal(ldt, tags, 2.5);

        // Build a result to return from the MealActivity
        Intent resultData = new Intent();
        resultData.putExtra(RETURN_MEAL_SERIALIZABLE, meal);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        //stub out MealActivity
        intending(IntentMatchers.hasComponent(hasClassName(MealActivity.class.getName()))).respondWith(result);


        //now press click of MealBtn that makes us go to MealActivity class (it will now be stubbed out for above)
        onView(withId(R.id.mealBtn)).perform(click());

        //onActivityResult in DiaryFragment (fragment child of MainActivity)
        //takes care of itself???

        //test here that item inside recycleView looks ok!
        onView(withId(R.id.mealBtn)).perform(click());

        onView(withId(R.id.events_layout)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //check that an item exists in RecyclerView that has Time 16:00 and portions 2.5
        onView(allOf(withId(R.id.portions), hasSibling(withText("16:00")), hasSibling(withText("2.5")))).check(matches(isDisplayed()));
    }
}
