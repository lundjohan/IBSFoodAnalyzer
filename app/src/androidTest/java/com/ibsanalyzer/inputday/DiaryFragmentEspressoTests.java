package com.ibsanalyzer.inputday;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Created by Johan on 2017-05-21.
 * Inspired by http://www.vogella.com/tutorials/AndroidTestingEspresso/article.html#espresso_exercisesimple3
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DiaryFragmentEspressoTests {
    ViewPagerIdlingResource viewPagerIdlingResource;
    MainActivity activity;
    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIntentServiceIdlingResource() {
        activity = mActivityRule.getActivity();
        viewPagerIdlingResource = new ViewPagerIdlingResource((ViewPager) activity.findViewById(R.id.pager));
        Espresso.registerIdlingResources(viewPagerIdlingResource);

    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(viewPagerIdlingResource);
    }

    @Test
    public void ensureThatPressingMealBtnLeadsToNewMealActivity() throws InterruptedException {
        onView(withId(R.id.mealBtn)).
                check(matches(isDisplayed())).perform(click());
        intended(allOf(
                hasComponent(hasShortClassName(".MealActivity"))));
    }

    @Test
    public void ensureThatPressingOtherBtnLeadsToNewOtherActivity() throws InterruptedException {
        onView(withId(R.id.otherBtn)).
                check(matches(isDisplayed())).perform(click());
        intended(hasComponent(OtherActivity.class.getName())); //only works if IntentTestRule instead of ActivityTestRule
    }
}
