package com.ibsanalyzer.drawer;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.ibsanalyzer.diary.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DrawerActivityTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity.class);

    @Test
    public void drawerActivityTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.otherBtn), withContentDescription("Other"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        pressBack();

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());

    }

}
