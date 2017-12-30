package com.ibsanalyzer.diary;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;

import com.ibsanalyzer.drawer.DrawerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by Johan on 2017-05-21.
 * Inspired by http://www.vogella.com/tutorials/AndroidTestingEspresso/article
 * .html#espresso_exercisesimple3
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventActivitiesAreStartedCorrectlyTests {
    @Rule
    public IntentsTestRule<DrawerActivity> mActivityRule =
            new IntentsTestRule<>(DrawerActivity.class);
    ViewPagerIdlingResource viewPagerIdlingResource;
    DrawerActivity activity;

    @Before
    public void registerIntentServiceIdlingResource() {
        activity = mActivityRule.getActivity();
        viewPagerIdlingResource = new ViewPagerIdlingResource((ViewPager) activity.findViewById(R
                .id.pager));
        Espresso.registerIdlingResources(viewPagerIdlingResource);

    }

    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(viewPagerIdlingResource);
    }

   /* @Test
    public void ensureThatPressingMealBtnLeadsToNewMealActivity() throws InterruptedException {
        onView(withId(R.id.mealBtn)).
                check(matches(isDisplayed())).perform(click());
        intended(
                hasComponent(hasShortClassName(".MealActivity")));
    }*/

    @Test
    public void ensureThatPressingOtherBtnLeadsToNewOtherActivity() throws InterruptedException {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.otherBtn), withContentDescription("Other"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        intended(hasComponent(OtherActivity.class.getName())); //only works if IntentTestRule
        // instead of ActivityTestRule
    }

    //====================================================================================
    //tests for the RecyclerView and eventList inside DiaryFragment
    //====================================================================================
    //TODO
    /* @Test
    public void checkThatBreaksAreHandledCorrectlyOnClickOfItem() throws InterruptedException {
        //1. Put in 3 events in eventsList. How???
        //1.5. Has item now a break? => should be no
        //2. Press longclick on one of them
        //3. Choose menuoption "break"
        //4. Has item now a break? => should be yes, the other ones should be no
        //5. Press again => unbreak
        //6. All items should have no break

        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }*/
}
