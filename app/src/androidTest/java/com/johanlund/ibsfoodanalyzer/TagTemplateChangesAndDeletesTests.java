package com.johanlund.ibsfoodanalyzer;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.johanlund.database.DBHandler;
import com.johanlund.help_classes.AndroidTestUtil;
import com.johanlund.model.TagType;
import com.johanlund.screens.main.DrawerActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.johanlund.ibsfoodanalyzer.R.id.name_of_stat_option;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by Johan on 2017-12-31.
 */
@RunWith(AndroidJUnit4.class)
public class TagTemplateChangesAndDeletesTests {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity
                    .class);

    @Before
    public void clearDatabase() {
        AndroidTestUtil.clearDatabaseByClickingAndInternally(mActivityTestRule.getActivity()
                .getApplicationContext());
    }

    private void goToTagAdderActivity() {
        onView(allOf(withId(R.id.otherBtn), isDisplayed())).perform(click());
        onView(withId(R.id.addTagsBtn)).perform(click());
    }

    //starts inside TagAdderActivity
    private void createATagTemplate(String nameOfTagTemplate) {
        //create a new TagType and add it to an OtherEvent and click done in TagAdderActivity
        // and in OtherActivity so it is placed in diary.
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText(nameOfTagTemplate),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());
        onView(withId(R.id.menu_done)).perform(click());

        //Inside DiaryFragment, check that the tag has been added.
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                (nameOfTagTemplate))))).check(matches(isDisplayed()));
        //go back into the event
        onView(allOf(isDisplayed(), withId(R.id.events_layout)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //inside OtherActivity, go into TagAdderActivity again
        onView(withId(R.id.addTagsBtn)).perform(click());


    }

    @Test
    public void deletedTagTemplateBackBtnTest() throws InterruptedException {
        goToTagAdderActivity();
        createATagTemplate("Butter");
        //inside TagAdderActivity, delete Butter TagType
        onView(allOf(withId(R.id.settings_btn_inside_listView), hasSibling(withText("Butter"))))
                .perform(click());

        onView(withText("Delete")).check(matches(isDisplayed())).perform(click());

        //check that you are still in TagAdderView and that the TagType was deleted
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
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Butter"))))).check(doesNotExist());


    }

    @Test
    public void editedTagTemplateBackBtnTest() throws InterruptedException {
        goToTagAdderActivity();
        createATagTemplate("Butter");
        //inside TagAdderActivity, delete Butter TagType
        onView(allOf(withId(R.id.settings_btn_inside_listView), hasSibling(withText("Butter"))))
                .perform(click());

        onView(withText("Edit")).check(matches(isDisplayed())).perform(click());

        //in TagEditActivity, change Butter to Cream
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Cream"),
                closeSoftKeyboard());

        //press done
        onView(withId(R.id.menu_done)).perform(click());

        //back in TagAdderActivity?
        onView(allOf(withId(R.id.menu_add_new))).check(matches(isDisplayed()));

        //changes has taken effect
        onView(withText(containsString("Butter"))).check(doesNotExist());
        onView(withText(containsString("Cream"))).check(matches(isDisplayed()));

        //remove soft keyboard
        pressBack();

        //go back to OtherActivity
        pressBack();
        //now you should be inside OtherActivity and the Cream should have replaced Butter
        onView(withText(containsString("Butter"))).check(doesNotExist());
        onView(withText(containsString("Cream"))).check(matches(isDisplayed()));

        //Press back btn again.
        pressBack();

        //press ok in pop up
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());

        //You should now be inside DiaryFragment, and
        // the tag should not be found anywhere.
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Butter"))))).check(doesNotExist());
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Cream"))))).check(matches(isDisplayed()));


    }

    @Test
    public void deletedTagTemplateDoneTest() {
        goToTagAdderActivity();
        createATagTemplate("Butter");

        //inside TagAdderActivity, add another TagType
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Sugar"),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());

        //Now should be back in OtherActivity, check that there are Butter and Sugar tags in list.
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Butter"))
        ))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Sugar")))
        )).check(matches(isDisplayed()));

        //go back to TagAdder and remove "Butter"
        onView(withId(R.id.addTagsBtn)).perform(click());
        onView(allOf(withId(R.id.settings_btn_inside_listView), hasSibling(withText("Butter"))))
                .perform(click());
        onView(withText("Delete")).check(matches(isDisplayed())).perform(click());

        //add another TagType, Honey.
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Honey"),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());

        //Now, inside OtherActivity again, there should only be two tags, Sugar and Honey. Butter
        // should be gone.
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Honey")))
        )).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Sugar")))
        )).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.addedTagsView), hasDescendant(withText(containsString("Butter"))
        ))).check(doesNotExist());

        //change the event to the one displayed in OtherActivity
        onView(withId(R.id.menu_done)).perform(click());

        //now inside DiaryFragment, do checks
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Honey"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Sugar"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Butter"))))).check(doesNotExist());
    }

    @Test
    public void editedTagTemplateDoneTest() {
        goToTagAdderActivity();
        createATagTemplate("Butter");
        //inside TagAdderActivity, edit Butter TagType
        onView(allOf(withId(R.id.settings_btn_inside_listView), hasSibling(withText("Butter"))))
                .perform(click());

        onView(withText("Edit")).check(matches(isDisplayed())).perform(click());

        //in TagEditActivity, change Butter to Cream
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Cream"),
                closeSoftKeyboard());

        //press done
        onView(withId(R.id.menu_done)).perform(click());

        //back in TagAdderActivity?
        onView(allOf(withId(R.id.menu_add_new))).check(matches(isDisplayed()));

        //changes has taken effect
        onView(withText(containsString("Butter"))).check(doesNotExist());
        onView(withText(containsString("Cream"))).check(matches(isDisplayed()));

        //remove soft keyboard
        pressBack();

        //go back to OtherActivity
        pressBack();
        //now you should be inside OtherActivity and the Cream should have replaced Butter
        onView(withText(containsString("Butter"))).check(doesNotExist());
        onView(withText(containsString("Cream"))).check(matches(isDisplayed()));

        //Press done btn.
        onView(withId(R.id.menu_done)).perform(click());
        //You should now be inside DiaryFragment, and
        // the tag should not be found anywhere.
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Butter"))))).check(doesNotExist());
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Cream"))))).check(matches(isDisplayed()));
    }

    /**
     * This test exists simply because there was an error when deleting a tagTemplate say in
     * place 0 in ListView, than it was always the TagType in last position that was deleted
     * instead.
     */
    @Test
    public void correctTagTemplateIsDeletedTest() {
        //add 2 TagTemplates
        DBHandler dbHandler = new DBHandler(mActivityTestRule.getActivity().getApplicationContext
                ());
        TagType butter = new TagType("Butter", null);
        TagType sugar = new TagType("Sugar", null);
        dbHandler.addTagTemplate(butter);
        dbHandler.addTagTemplate(sugar);

        goToTagAdderActivity();

        /*temporary test, right now the tagtemplate that was added first is placed first in
        listOfTags,
        this will probably change later though (to sort order by name etc). Change this sentence
        then. I don't know how to do it now.
        */
        onData(anything())
                .inAdapterView(withId(R.id.listOfTags))
                .atPosition(0)
                .check(matches(hasDescendant(
                        allOf(withId(name_of_stat_option), withText(containsString("Butter"))))));

        onData(anything())
                .inAdapterView(withId(R.id.listOfTags))
                .atPosition(1)
                .check(matches(hasDescendant(
                        allOf(withId(name_of_stat_option), withText(containsString("Sugar"))))));


        //delete the first TagType in list, in this case "Butter"
        onView(allOf(withId(R.id.settings_btn_inside_listView), hasSibling(withText("Butter"))))
                .perform(click());
        /*code below didn't work
        onData(withId(R.id.three_dots_inside_listView))
                .inAdapterView(withId(R.id.listOfTags))
                .atPosition(0).perform(click());*/

        onView(withText("Delete")).check(matches(isDisplayed())).perform(click());

        //now only Sugar should be displayed
        onView(withText(containsString("Butter"))).check(doesNotExist());
        onView(withText(containsString("Sugar"))).check(matches(isDisplayed()));

    }

    @Test
    public void whileInEditingTagTemplateAddingInheritance() {
        DBHandler dbHandler = new DBHandler(mActivityTestRule.getActivity().getApplicationContext
                ());
        TagType butter = new TagType("Butter", null);
        dbHandler.addTagTemplate(butter);

        goToTagAdderActivity();

        //inside TagAdderActivity, edit Butter TagType
        onView(allOf(withId(R.id.settings_btn_inside_listView), hasSibling(withText("Butter"))))
                .perform(click());

        onView(withText("Edit")).check(matches(isDisplayed())).perform(click());
        //need double click
        onView(withId(R.id.is_a_type_of)).perform(click());
        //back in TagAdderActivity?
        onView(allOf(withId(R.id.menu_add_new))).check(matches(isDisplayed()));

        //add another TagType, Lacteo.
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText("Lacteo"),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());

        //now back in Butter edittext view
        onView(withId(R.id.is_a_type_of)).check(matches(isDisplayed()));
        onView(withText("Butter")).check(matches(isDisplayed()));
        onView(withText("Lacteo")).check(matches(isDisplayed()));

        onView(withId(R.id.menu_done)).perform(click());

        //now in TagAdder again
        onView(allOf(withId(R.id.menu_add_new))).check(matches(isDisplayed()));
        onView(withText("Butter")).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.name_of_stat_option), withText("Lacteo"))).check(matches
                (isDisplayed()));

        //remove soft keyboard
        pressBack();

        //go back to OtherActivity
        pressBack();
        //now you should be inside OtherActivity and still only Butter should be seen
        onView(withText(containsString("Butter"))).check(matches(isDisplayed()));
        //this one might change in case inheritance is shown.
        onView(withText(containsString("Lacteo"))).check(doesNotExist());

        //change the event to the one displayed in OtherActivity
        onView(withId(R.id.menu_done)).perform(click());

        //You should now be in Diary
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Butter"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString
                ("Lacteo"))))).check(doesNotExist());
    }
}
