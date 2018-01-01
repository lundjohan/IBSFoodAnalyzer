package com.ibsanalyzer.diary;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ibsanalyzer.drawer.DrawerActivity;
import com.ibsanalyzer.help_classes.AndroidTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ibsanalyzer.diary.R.id.addedTagsView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by Johan on 2017-12-31.
 */
@RunWith(AndroidJUnit4.class)
public class TagTemplateChangesEffectsTests {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity
            .class);

    @Before
    public void clearDatabase(){
      AndroidTestUtil.clearDatabase();
    }

    private void startOfTest(){
        //create a new TagTemplate and add it to an OtherEvent and click done in TagAdderActivity
        // and in OtherActivity so it is placed in diary.
        onView(allOf(withId(R.id.otherBtn), isDisplayed())).perform(click());
        onView(withId(R.id.addTagsBtn)).perform(click());
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Butter"),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());
        onView(withId(R.id.menu_done)).perform(click());

        //Inside DiaryFragment, check that the tag has been added.
        onView(allOf(withId(R.id.tagNames), hasDescendant(withText(containsString("Butter"))))).check(matches(isDisplayed()));
        //go back into the event
        onView(allOf(isDisplayed(), withId(R.id.events_layout)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //inside OtherActivity, go into add tags and delete the TagTemplate.
        onView(withId(R.id.addTagsBtn)).perform(click());


    }
    @Test
    public void deletedTagTemplateBackBtnTest() throws InterruptedException {
        startOfTest();
        //inside TagAdderActivity, delete Butter TagTemplate
        onView(allOf(withId(R.id.three_dots_inside_listView), hasSibling(withText("Butter"))))
                .perform(click());

        onView(withText("Delete")).check(matches(isDisplayed())).perform(click());

        //check that you are still in TagAdderView and that the TagTemplate was deleted
        onView(allOf(withId(R.id.menu_add_new))).check(matches(isDisplayed()));
        onView(withText(containsString("Butter"))).check(doesNotExist());

        //remove soft keyboard
        pressBack();

        //go back to OtherActivity
        pressBack();
        //now you should be inside OtherActivity and the tag should be removed in tagsList
        onView(withText(containsString("Butter"))).check(doesNotExist());

        //Press back btn again.
        pressBack();
        //You should now be inside DiaryFragment, and
        // the tag should not be found anywhere.
        onView(allOf(withId(R.id.tagNames),isDisplayed(), hasDescendant(withText(containsString("Butter"))))).check(doesNotExist());


    }
//TODO Do this with Meal Activity instead of Other
    @Test
    public void deletedTagTemplateDoneTest() {
        startOfTest();

        //inside TagAdderActivity, add another TagTemplate
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Sugar"),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());

        //Now should be back in OtherActivity, check that there are Butter and Sugar tags in list.
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Butter"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Sugar"))))).check(matches(isDisplayed()));

        //go back to TagAdder and remove "Butter"
        onView(withId(R.id.addTagsBtn)).perform(click());
        onView(allOf(withId(R.id.three_dots_inside_listView), hasSibling(withText("Butter"))))
                .perform(click());
        onView(withText("Delete")).check(matches(isDisplayed())).perform(click());

        //add another TagTemplate, Honey.
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Honey"),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());

        //Now, inside OtherActivity again, there should only be two tags, Sugar and Honey. Butter should be gone.
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Honey"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Sugar"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Butter"))))).check(doesNotExist());

        //change the event to the one displayed in OtherActivity
        onView(withId(R.id.menu_done)).perform(click());

        //now inside DiaryFragment, do checks
        onView(allOf(withId(R.id.tagNames),isDisplayed(), hasDescendant(withText(containsString("Honey"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tagNames),isDisplayed(), hasDescendant(withText(containsString("Sugar"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tagNames),isDisplayed(), hasDescendant(withText(containsString("Butter"))))).check(doesNotExist());
    }
}
