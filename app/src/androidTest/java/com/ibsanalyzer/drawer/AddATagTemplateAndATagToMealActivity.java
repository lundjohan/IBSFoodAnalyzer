package com.ibsanalyzer.drawer;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ibsanalyzer.diary.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddATagTemplateAndATagToMealActivity {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity.class);

    @Test
    public void addATagTemplateAndATagToMealActivity() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.mealBtn), withContentDescription("Meal"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.addTagsBtn), withText("Add Tags"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_add_new), withContentDescription("Add"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name_box), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.name_box), isDisplayed()));
        appCompatEditText2.perform(replaceText("Socker"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.menu_done), withText("DONE"), withContentDescription("DONE"),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.stattagname), withText("Socker"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.addedTagsView),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Socker")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
