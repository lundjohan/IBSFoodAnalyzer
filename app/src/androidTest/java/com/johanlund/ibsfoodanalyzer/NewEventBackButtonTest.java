package com.johanlund.ibsfoodanalyzer;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.johanlund.screens.main.DrawerActivity;
import com.johanlund.help_classes.AndroidTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.johanlund.ibsfoodanalyzer.NewEventsDisplayedCorrectlyInDiaryTests.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewEventBackButtonTest {

    @Rule
    public IntentsTestRule<DrawerActivity> mActivityTestRule = new IntentsTestRule<DrawerActivity>
            (DrawerActivity.class);

    @Before
    public void clearDatabase(){
        AndroidTestUtil.clearDatabaseByClicking();
    }


    //create a rating event in beginning, it will later check that it remains in view after back
    // click.
    public void fillRecyclerViewWithAnRatingEvent() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.ratingBtn), withContentDescription("Rating"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttons),
                                        0),
                                4),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //properties of rating event doesn't matter
        onView(withId(R.id.menu_done)).perform(click());

    }

    private void checkThatBackActionCameBackToSameView(int id, String contentDesc, int notExistingId, int pos) {
        //Click "New Meal Button"
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(id),withContentDescription(contentDesc),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttons),
                                        0),
                                pos),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //Inside NewMealActivity, press back button
        pressBack();

        //press ok in pop up
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());

        //check that recyclerview in Diary still contains an event item (the Rating item created
        // before)
        onView(withId(R.id.rating_item_container)).check(matches(isDisplayed()));

        //also check that no other event item is displayed
        onView(withId(notExistingId))
                .check(doesNotExist());
    }
    //removed the rating that was added in beginning.
    //the code in method comes from espresso recording.
    public void cleanUp(){
        ViewInteraction linearLayout = onView(
                allOf(withClassName(is("android.widget.LinearLayout")),
                        withParent(withId(R.id.events_layout)),
                        isDisplayed()));
        linearLayout.perform(longClick());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Delete Event"), isDisplayed()));
        appCompatTextView.perform(click());   }
    @Test
    public void newEventsBackButtonTest() {
        fillRecyclerViewWithAnRatingEvent();
        checkThatBackActionCameBackToSameView(R.id.mealBtn, "Meal", R.id.meal_item_container, 0);
        checkThatBackActionCameBackToSameView(R.id.otherBtn, "Other",R.id.other_item_container,1);
        checkThatBackActionCameBackToSameView(R.id.exerciseBtn, "Exercise",R.id.exercise_item_container,2);
        checkThatBackActionCameBackToSameView(R.id.bmBtn, "Bowel Movement",R.id.bm_item_container,3);

        //cannot have rating as not existing, therefore meal
        checkThatBackActionCameBackToSameView(R.id.ratingBtn, "Rating", R.id.meal_item_container,4);
        cleanUp();
    }
}
