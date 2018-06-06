package com.johanlund.ibsfoodanalyzer;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Other;
import com.johanlund.base_classes.Tag;
import com.johanlund.database.DBHandler;
import com.johanlund.screens.main.DrawerActivity;
import com.johanlund.help_classes.AndroidTestUtil;
import com.johanlund.model.EventsTemplate;
import com.johanlund.model.TagType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

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
 * <p>
 * This test is not finished. Read up on testing in Android first!!!
 * (I want to use mocking etc)
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventsTemplateTests {
    Other other;
    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity
                    .class);

    @Before
    public void clearDatabase() {
        AndroidTestUtil.clearDatabaseByClickingAndInternally(mActivityTestRule.getActivity().getApplicationContext());
    }

    @Before
    public void createEventsInDatabase() {
        DBHandler dbHandler = new DBHandler(mActivityTestRule.getActivity().getApplicationContext
                ());

        //TagTemplates
        TagType butter = new TagType("Butter", null);
        TagType sugar = new TagType("Sugar", null);
        dbHandler.addTagTemplate(butter);
        dbHandler.addTagTemplate(sugar);

        //Create an Other Event
        LocalDateTime ldt = LocalDateTime.of(2018, Month.JANUARY, 9, 15, 0);
        /*TagType t1 = new TagType("Butter");
        TagType t2 = new TagType("Sugar");*/
        Tag tag1 = new Tag(ldt, "Butter", 1.0);
        Tag tag2 = new Tag(ldt, "Sugar", 1.0);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        other = new Other(ldt, tags);
        dbHandler.addOther(other);
    }

    private void createEventsTemplateInDatabase() {
        List<Event> events = new ArrayList<>();
        events.add(other);
        EventsTemplate et = new EventsTemplate(events, "anEventTemplate");
        DBHandler dbHandler = new DBHandler(mActivityTestRule.getActivity().getApplicationContext
                ());
        dbHandler.addEventsTemplate(et);
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
                allOf(withId(R.id.template_name), isDisplayed())).perform(replaceText
                ("Testtemplate"), closeSoftKeyboard());

        onView(
                allOf(withId(R.id.menu_done), withText("DONE"), withContentDescription("DONE"),
                        isDisplayed())).perform(click());

        //TODO here, check that one item is in eventstemplate list

        //ok inside diary view
    }

    /**
     * I had a problem before that when pressing back button after choosing 'Load into diary' that anyhow the event was stored in diary.
     * It is not stored like this in test environment for some reason, so this test can falsely pass.
     * Anyway the problem in the real app (avoid overloading finish()! )is fixed, but just so I don't feel for constructing it again, I let this stand...
     */
    /*@Test
    public void addingEventsTemplateBackBtnTest() {
        createEventsTemplateInDatabase();

        //Go to date with an other event added
        AndroidTestUtil.changeDate(mActivityTestRule, LocalDate.of(2018, Month.JANUARY, 9));

        //check that only one event is displayed
        onView(
                withText("Butter")).check(matches(isDisplayed()));


        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.template_btn), isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.three_dots),
                        withParent(allOf(withId(R.id.template_item), withChild(allOf(withId(R.id
                                .template_title), withText("anEventTemplate"))), isDisplayed()))));
        appCompatImageView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Load into Diary"), isDisplayed()));
        appCompatTextView2.perform(click());

        pressBack();
        AndroidTestUtil.changeDate(mActivityTestRule, LocalDate.of(2018, Month.JANUARY, 9));
        onView(
                withText("Butter")).check(matches(isDisplayed()));
    }*/
}
