package com.ibsanalyzer.diary;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.drawer.DrawerActivity;
import com.ibsanalyzer.help_classes.RecyclerViewMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ibsanalyzer.diary.R.id.events_layout;
import static com.ibsanalyzer.diary.R.id.textView;
import static com.ibsanalyzer.drawer.TestFromEspressoRecordning.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Johan on 2017-12-31.
 */
@RunWith(AndroidJUnit4.class)
public class TagTemplateChangesEffectsTests {

        @Rule
        public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(DrawerActivity
                .class);

        @Test
        public void deletedTagTemplateBackBtnTest() throws InterruptedException {
            //create a new TagTemplate and add it to an OtherEvent and click done in TagAdderActivity and in OtherActivity so it is placed in diary.
            onView(allOf(withId(R.id.otherBtn), isDisplayed())).perform(click());
            onView(withId(R.id.addTagsBtn)).perform(click());
            onView(withId(R.id.menu_add_new)).perform(click());
            onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Butter"),
                    closeSoftKeyboard());
            onView(withId(R.id.menu_done)).perform(click());
            onView(withId(R.id.menu_done)).perform(click());

            //Inside DiaryFragment, check that the tag has been added.
            onView(allOf(withId(R.id.events_layout), isDisplayed())).check(matches(hasDescendant(withText(containsString("Butter")))));
            
            //go back into the event
            onView(allOf(isDisplayed(), withId(R.id.events_layout)))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            //go into add tags and delete the TagTemplate.
            onView(withId(R.id.addTagsBtn)).perform(click());

            onView(withId(R.id.three_dots_inside_listView)).perform(click());
            onView(withText("Delete")).perform(click());

            //check that you are still in TagAdderView and that the TagTemplate was deleted
            onView(allOf(withId(R.id.addTagsBtn))).check(matches(isDisplayed()));
            onView(withText("Butter")).check(doesNotExist());

            //press back btn
            pressBack();

            //now you should be inside OtherActivity and the tag should be removed in tagsList
            onView(allOf(withId(R.id.stattagname), withText("Butter"))).check(doesNotExist());

            //Press back btn again.
            pressBack();
            //You should now be inside DiaryFragment (check that it is the same date as before), and the tag should not be found anywhere.

        }
    @Test
    public void deletedTagTemplateDoneTest() {
        //create a new TagTemplate and add it to an OtherEvent and click done so it is placed in diary.

        //go back into the event

        //go into add tags and delete the TagTemplate.

        //check that you are still in TagAdderView and that the TagTemplate was deleted

        //create another tagTemplate

        //now you should be inside OtherActivity and the deleted tag should be removed in tagsList, but the new tag should be there

        //Press done.

        //You should now be inside DiaryFragment (check that it is the same date as before), and only the new tag should be found, not the deleted one..
    }
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
