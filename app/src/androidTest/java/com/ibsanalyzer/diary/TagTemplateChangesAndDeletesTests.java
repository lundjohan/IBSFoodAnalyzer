package com.ibsanalyzer.diary;

import android.database.Cursor;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;

import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.drawer.DrawerActivity;
import com.ibsanalyzer.help_classes.AndroidTestUtil;
import com.ibsanalyzer.model.TagTemplate;

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
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

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
    public void clearDatabase(){
        AndroidTestUtil.clearDatabase(mActivityTestRule.getActivity().getApplicationContext());
    }

    private void goToTagAdderActivity(){
        onView(allOf(withId(R.id.otherBtn), isDisplayed())).perform(click());
        onView(withId(R.id.addTagsBtn)).perform(click());
    }

    //starts inside TagAdderActivity
    private void createATagTemplate(String nameOfTagTemplate){
        //create a new TagTemplate and add it to an OtherEvent and click done in TagAdderActivity
        // and in OtherActivity so it is placed in diary.
        onView(withId(R.id.menu_add_new)).perform(click());
        onView(withId(R.id.name_box)).perform(click()).perform(replaceText(nameOfTagTemplate),
                closeSoftKeyboard());
        onView(withId(R.id.menu_done)).perform(click());
        onView(withId(R.id.menu_done)).perform(click());

        //Inside DiaryFragment, check that the tag has been added.
        onView(allOf(withId(R.id.tagNames), isDisplayed(), hasDescendant(withText(containsString(nameOfTagTemplate))))).check(matches(isDisplayed()));
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
    @Test
    public void editedTagTemplateBackBtnTest() throws InterruptedException {
        goToTagAdderActivity();
        createATagTemplate("Butter");
        //inside TagAdderActivity, delete Butter TagTemplate
        onView(allOf(withId(R.id.three_dots_inside_listView), hasSibling(withText("Butter"))))
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
        //You should now be inside DiaryFragment, and
        // the tag should not be found anywhere.
        onView(allOf(withId(R.id.tagNames),isDisplayed(), hasDescendant(withText(containsString("Butter"))))).check(doesNotExist());
        onView(allOf(withId(R.id.tagNames),isDisplayed(), hasDescendant(withText(containsString("Cream"))))).check(matches(isDisplayed()));


    }
    @Test
    public void deletedTagTemplateDoneTest() {
        goToTagAdderActivity();
        createATagTemplate("Butter");

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

    /**
     * This test exists simply because there was an error when deleting a tagTemplate say in place 0 in ListView, than it was always the TagTemplate in last position that was deleted instead.
     */
    @Test
    public void correctTagTemplateIsDeletedTest() {
        //add 2 TagTemplates
        DBHandler dbHandler = new DBHandler(mActivityTestRule.getActivity().getApplicationContext());
        TagTemplate butter = new TagTemplate("Butter", null);
        TagTemplate sugar = new TagTemplate("Sugar", null);
        dbHandler.addTagTemplate(butter);
        dbHandler.addTagTemplate(sugar);

        goToTagAdderActivity();

        /*temporary test, right now the tagtemplate that was added first is placed first in listOfTags,
        this will probably change later though (to sort order by name etc). Change this sentence then. I don't know how to do it now.
        */
        onData(anything())
                .inAdapterView(withId(R.id.listOfTags))
                .atPosition(0)
                .check(matches(hasDescendant(
                        allOf(withId(R.id.name_of_tag), withText(containsString("Butter"))))));

        onData(anything())
                .inAdapterView(withId(R.id.listOfTags))
                .atPosition(1)
                .check(matches(hasDescendant(
                        allOf(withId(R.id.name_of_tag), withText(containsString("Sugar"))))));


        //delete the first TagTemplate in list, in this case "Butter"
        onView(allOf(withId(R.id.three_dots_inside_listView), hasSibling(withText("Butter"))))
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
  /*  @Test
    public void whenATagTemplateHasBeenAddedToOtherActivityItShouldntShowUpAnymoreInTagAdderList() {
        DBHandler dbHandler = new DBHandler(mActivityTestRule.getActivity().getApplicationContext());
        TagTemplate butter = new TagTemplate("Butter", null, null, null);
        dbHandler.addTagTemplate(butter);
        goToTagAdderActivity();

        //add TagTemplate to OtherActivity's  tagList
        onView(withText("Butter")).perform(click());

        //go back into TagAdder, where "Butter" TagTemplate should be no more displayed.
        onView(withId(R.id.addTagsBtn)).perform(click());
        onView(withText(containsString("Butter"))).check(doesNotExist());
    }*/
}
