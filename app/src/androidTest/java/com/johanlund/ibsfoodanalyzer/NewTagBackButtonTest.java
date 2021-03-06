package com.johanlund.ibsfoodanalyzer;


import android.support.test.InstrumentationRegistry;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class NewTagBackButtonTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>
            (DrawerActivity
            .class);

    @Before
    public void clearDatabase() {
        AndroidTestUtil.clearDatabaseByClicking();
    }

    @Test
    public void newTagBackButtonWhenThereAreNoTagsAddedBeforeTest() {
        onView(allOf(withId(R.id.mealBtn), isDisplayed())).perform(click());
        onView(withId(R.id.addTagsBtn)).perform(click());

        //inside TagAdderActivity, there should now be an information text how to add tags.
        //pressing back button to go back to MealActivity
        pressBack();

        //should be back in MealActivity, check for some id there
        onView(withId(R.id.mealContainer)).check(matches(isDisplayed()));
    }

    @Test
    public void newTagBackButtonWhenThereAreTagsAddedTest() {
        //add a tag type...
        DBHandler dbHandler = new DBHandler(InstrumentationRegistry.getTargetContext());
        TagType tt = new TagType("yoghurt");
        dbHandler.addTagTemplate(tt);

        onView(allOf(withId(R.id.mealBtn), isDisplayed())).perform(click());
        onView(withId(R.id.addTagsBtn)).perform(click());

        //inside TagAdderActivity, pressing back (up) button to remove keyboard
        pressBack();

        //inside TagAdderActivity, pressing back button to go back to MealActivity
        pressBack();

        //should be back in MealActivity, check for some id there
        onView(withId(R.id.mealContainer)).check(matches(isDisplayed()));
    }

}
