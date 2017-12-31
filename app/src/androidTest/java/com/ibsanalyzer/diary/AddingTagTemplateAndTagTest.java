package com.ibsanalyzer.diary;


import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.ibsanalyzer.drawer.DrawerActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddingTagTemplateAndTagTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(DrawerActivity
            .class);

    @Test
    public void addingTagTemplateAndTagTest() {
        //first, add TagTemplate "butter"

        onView(allOf(withId(R.id.mealBtn), isDisplayed())).perform(click());
        onView(withId(R.id.addTagsBtn)).perform(click());
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("butter"),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());

        //remove tag, by clicking 'x'
        onView(allOf(withId(R.id.stattagname), withText("butter"), isDisplayed()));
        onView(withId(R.id.deleteTag)).perform(click());
        //must confirm, through clicking in pop up window with id button1 (has text "Confirm" in
        // English)
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.stattagname)).check(doesNotExist());

        //add it again, this time it should be in listview in TagAdderActivity, so only to click
        onView(withId(R.id.addTagsBtn)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listOfTags))
                .atPosition(0)
                .perform(click());

        //last check, that tag_item is (again) correctly added in listView inside MealActivity
        onView(allOf(withId(R.id.stattagname), withText("butter"), isDisplayed()));
    }
}
