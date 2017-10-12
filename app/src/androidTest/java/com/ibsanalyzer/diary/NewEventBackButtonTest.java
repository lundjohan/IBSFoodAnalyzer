package com.ibsanalyzer.diary;


import android.support.test.espresso.intent.rule.IntentsTestRule;
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
public class NewEventBackButtonTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<MainActivity>
            (MainActivity.class);


    //create a rating event in beginning, it will later check that it remains in view after back
    // click.
    public void fillRecyclerViewWithAnRatingEvent() {
        onView(withId(R.id.ratingBtn)).perform(click());

        //properties of rating event doesn't matter
        onView(withId(R.id.menu_done)).perform(click());

    }

    private void checkThatBackActionCameBackToSameView(int id) {
        //Click "New Meal Button"
        onView(withId(id)).perform(click());

        //Inside NewMealActivity, press back button
        pressBack();

        //check that recyclerview in Diary still contains an event item (the Rating item created
        // before)
        onView(withId(R.id.time)).check(matches(isDisplayed()));

    }

    @Test
    public void newEventsBackButtonTest() {
        fillRecyclerViewWithAnRatingEvent();
        checkThatBackActionCameBackToSameView(R.id.mealBtn);
        checkThatBackActionCameBackToSameView(R.id.otherBtn);
        checkThatBackActionCameBackToSameView(R.id.exerciseBtn);
        checkThatBackActionCameBackToSameView(R.id.bmBtn);
        checkThatBackActionCameBackToSameView(R.id.bmBtn);
        checkThatBackActionCameBackToSameView(R.id.ratingBtn);

    }

}
