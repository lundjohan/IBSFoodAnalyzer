package com.ibsanalyzer.diary;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewTagBackButtonTest {
    /*
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity
            .class);

    @Test
    public void newTagBackButtonTest() {
        onView(withId(R.id.mealBtn)).perform(click());
        onView(withId(R.id.addTagsBtn)).perform(click());

        //inside TagAdderActivity, pressing back (up) button to remove keyboard
        pressBack();

        //inside TagAdderActivity, pressing back button to go back to MealActivity
        pressBack();

        //should be back in MealActivity, check for some id there
        onView(withId(R.id.mealContainer)).check(matches(isDisplayed()));
    }*/

}
