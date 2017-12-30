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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestingForManyMealItemIds {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity.class);

    @Test
    public void testingForManyMealItemIds() {
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
        appCompatEditText2.perform(replaceText("Honey"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.menu_done), withText("DONE"), withContentDescription("DONE"),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.addTagsBtn), withText("Add Tags"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.menu_add_new), withContentDescription("Add"), isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.name_box), isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.name_box), isDisplayed()));
        appCompatEditText4.perform(replaceText("Banana"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.menu_done), withText("DONE"), withContentDescription("DONE"),
                        isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.portions), withText("1"),
                        withParent(allOf(withId(R.id.mealContainer),
                                withParent(withId(R.id.appendingLayout)))),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.portions), withText("1"),
                        withParent(allOf(withId(R.id.mealContainer),
                                withParent(withId(R.id.appendingLayout)))),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.commentView),
                        withParent(withId(R.id.base_layout)),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("Espresso testing"), closeSoftKeyboard());

        ViewInteraction actionMenuItemView5 = onView(
                allOf(withId(R.id.menu_done), withText("DONE"), withContentDescription("DONE"),
                        isDisplayed()));
        actionMenuItemView5.perform(click());

        ViewInteraction imageView = onView(
                allOf(withId(R.id.item_icon),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout
                                                .class),
                                        0),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.firstLine), withText("18:23"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout
                                                .class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("18:23")));

        ViewInteraction textView2 = onView(
                allOf(withText("Honey"),
                        childAtPosition(
                                allOf(withId(R.id.tagNames),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view
                                                        .ViewGroup.class),
                                                3)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Honey")));

        ViewInteraction textView3 = onView(
                allOf(withText("Banana"),
                        childAtPosition(
                                allOf(withId(R.id.tagNames),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view
                                                        .ViewGroup.class),
                                                3)),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Banana")));

        ViewInteraction textView4 = onView(
                allOf(withText("X1.0"),
                        childAtPosition(
                                allOf(withId(R.id.tagQuantities),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                4)),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("X1.0")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.secondLine), withText("Portions: 2.0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                2),
                        isDisplayed()));
        textView5.check(matches(withText("Portions: 2.0")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.commentInItem), withText("Espresso testing"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                6),
                        isDisplayed()));
        textView6.check(matches(withText("Espresso testing")));

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
