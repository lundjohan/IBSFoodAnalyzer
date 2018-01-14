package com.ibsanalyzer.diary;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.drawer.DrawerActivity;
import com.ibsanalyzer.help_classes.AndroidTestUtil;
import com.ibsanalyzer.model.EventsTemplate;
import com.ibsanalyzer.model.TagTemplate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Johan on 2018-01-09.
 *
 * This test is not finished. Read up on testing in Android first!!!
 * (I want to use mocking etc)
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventsTemplateTests {
    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity
                    .class);

    @Before
    public void clearDatabase() {
        AndroidTestUtil.clearDatabase(mActivityTestRule.getActivity().getApplicationContext());
    }

    @Before
    public void createEventsInDatabase() {
        DBHandler dbHandler = new DBHandler(mActivityTestRule.getActivity().getApplicationContext
                ());

        //TagTemplates
        TagTemplate butter = new TagTemplate("Butter", null);
        TagTemplate sugar = new TagTemplate("Sugar", null);
        dbHandler.addTagTemplate(butter);
        dbHandler.addTagTemplate(sugar);

        //Create an Other Event
        LocalDateTime ldt = LocalDateTime.of(2018, Month.JANUARY, 9, 15, 0);
        /*TagTemplate t1 = new TagTemplate("Butter");
        TagTemplate t2 = new TagTemplate("Sugar");*/
        Tag tag1 = new Tag(ldt, "Butter", 1.0);
        Tag tag2 = new Tag(ldt, "Sugar", 1.0);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        Other other = new Other(ldt, tags);
        dbHandler.addOther(other);
    }

    @Test
    public void creatingAnEventsTemplateTest() {
        //go to correct date.
        AndroidTestUtil.changeDate(mActivityTestRule, LocalDate.of(2018, Month.JANUARY, 9));
        //-----------------------------------------------------------------------------
        //save event to eventstemplates
        // (this is very sloppy, it is mostly copied from a record espresso test)
        //-----------------------------------------------------------------------------
        //long click on event in diary
        ViewInteraction linearLayout = onView(
                allOf(withClassName(is("android.widget.LinearLayout")),
                        withParent(withId(R.id.events_layout)),
                        isDisplayed()));
        linearLayout.perform(longClick());

        //mark it
        onView(
                allOf(withId(R.id.title), withText("Mark item"), isDisplayed())).perform(click());

        onView(
                allOf(withId(R.id.to_template_btn), isDisplayed())).perform(click());

        onView(
                allOf(withId(R.id.template_name), isDisplayed())).perform(click());


        onView(
                allOf(withId(R.id.template_name), isDisplayed())).perform(replaceText("Testtemplate"), closeSoftKeyboard());

        onView(
                allOf(withId(R.id.menu_done), withText("DONE"), withContentDescription("DONE"),
                        isDisplayed())).perform(click());


        onView(
                allOf(withId(R.id.cancel_btn), isDisplayed())).perform(click());
//ok inside templates view?
        onView(
                allOf(withId(R.id.template_btn), isDisplayed())).perform(click());

        //TODO here, check that one item is in eventstemplate list

        //ok inside diary view
    }

    @Test
    public void editingAEventsTemplateTest() {
        //go to correct date.
        AndroidTestUtil.changeDate(mActivityTestRule, LocalDate.of(2018, Month.JANUARY, 9));
        //edit an eventstemplates


        //ok inside templates view?

        //ok inside diary view (should not have been added there etc)
    }
}
