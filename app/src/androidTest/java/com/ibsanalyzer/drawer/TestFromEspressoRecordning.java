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
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestFromEspressoRecordning {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity.class);

    @Test
    public void testFromEspressoRecordning() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.otherBtn), withContentDescription("Other"),
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
        appCompatEditText2.perform(replaceText("Butter"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.menu_done), withText("DONE"), withContentDescription("DONE"),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.menu_done), withText("DONE"), withContentDescription("DONE"),
                        isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Butter"),
                        childAtPosition(
                                allOf(withId(R.id.tagNames),
                                        childAtPosition(
                                                withId(R.id.other_item_container),
                                                4)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Butter")));

        /* ViewInteraction recyclerView = onView(
                allOf(withId(R.id.events_layout), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.addTagsBtn), withText("Add Tags"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.three_dots_inside_listView),
                        withParent(childAtPosition(
                                withId(R.id.listOfTags),
                                0)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

      ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Delete"), isDisplayed()));
        appCompatTextView.perform(click());*/

    }

    public static Matcher<View> childAtPosition(
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
