package com.ibsanalyzer.inputday;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Created by Johan on 2017-05-21.
 * Inspired by http://www.vogella.com/tutorials/AndroidTestingEspresso/article.html#espresso_exercisesimple3
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTest {
    ViewPagerIdlingResource viewPagerIdlingResource;
    MainActivity activity;
    @Rule
   /* public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);*/

    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIntentServiceIdlingResource() {
       /* Intent intent = new Intent();
        mActivityRule.launchActivity(intent);*/
        activity = mActivityRule.getActivity();
        viewPagerIdlingResource = new ViewPagerIdlingResource((ViewPager) activity.findViewById(R.id.pager));
        Espresso.registerIdlingResources(viewPagerIdlingResource);

    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(viewPagerIdlingResource);
     //   mActivityRule.getActivity().finish();

    }

    @Test
    public void ensureThatPressingMealBtnLeadsToNewMealActivity() throws InterruptedException {
        //  onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.mealBtn)).
                check(matches(isDisplayed())).perform(click());
        intended(allOf(
                hasComponent(hasShortClassName(".MealActivity"))));
              //  toPackage(PACKAGE_NAME),




      //  intended(hasComponent(MealActivity.class.getName())); //only works if IntentTestRule instead of ActivityTestRule
        /*this id is the an id of MealActivity, this seems to be the
        best way of ascerting that another activity has been started.
        see http://stackoverflow.com/a/30894255/2965972*/
        //  onView(withId(R.id.mealContainer)).check(matches(isDisplayed()));
        //pressBack();
    }

    @Test
    public void ensureThatPressingOtherBtnLeadsToNewOtherActivity() throws InterruptedException {
        onView(withId(R.id.otherBtn)).
                check(matches(isDisplayed())).perform(click());
        intended(hasComponent(OtherActivity.class.getName())); //only works if IntentTestRule instead of ActivityTestRule
        /*this id is the an id of MealActivity, this seems to be the
        best way of asserting that another activity has been started.
        see http://stackoverflow.com/a/30894255/2965972*/
        // onView(withId(R.id.otherContainer)).check(matches(isDisplayed()));
       // pressBack();
    }
 /*   @Test
    public void ensureThatPressingExerciseBtnLeadsToNewExerciseActivity() throws InterruptedException {
        onView(withId(R.id.exerciseBtn)).
                check(matches(isDisplayed())).perform(click());*/
    // intended(hasComponent(MealActivity.class.getName())); only works if IntentTestRule instead of ActivityTestRule
        /*this id is the an id of MealActivity, this seems to be the
        best way of ascerting that another activity has been started.
        see http://stackoverflow.com/a/30894255/2965972*/
   /*     onView(withId(R.id.exerciseContainer)).check(matches(isDisplayed()));
    }*/
}
