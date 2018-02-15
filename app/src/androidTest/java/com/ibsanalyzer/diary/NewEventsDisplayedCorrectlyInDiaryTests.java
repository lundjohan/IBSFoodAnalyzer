package com.ibsanalyzer.diary;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.ibsanalyzer.base_classes.Bm;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.base_classes.Exercise;
import com.ibsanalyzer.base_classes.Meal;
import com.ibsanalyzer.base_classes.Other;
import com.ibsanalyzer.base_classes.Rating;
import com.ibsanalyzer.base_classes.Tag;
import com.ibsanalyzer.drawer.DrawerActivity;
import com.ibsanalyzer.help_classes.AndroidTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.ibsanalyzer.constants.Constants.NEW_EVENT;
import static com.ibsanalyzer.constants.Constants.RETURN_EVENT_SERIALIZABLE;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Johan on 2017-05-23.
 * <p>
 * <p>
 * This Test class will test that behaviour of DrawerActivity with its child DiaryFragment is
 * behaving ok
 * relative to child Activities.
 * <p>
 * All other Activities (except for DrawerActivity) are stubbed out.
 * <p>
 * <p>
 * Based on end of https://guides.codepath.com/android/UI-Testing-with-Espresso
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewEventsDisplayedCorrectlyInDiaryTests {

    @Rule
    public IntentsTestRule<DrawerActivity> intentsRule = new IntentsTestRule<>(DrawerActivity.class);
    List<Tag> tags = new ArrayList<>();
    LocalDateTime ldt;

    @Before
    public void clearDatabase(){
        AndroidTestUtil.clearDatabase();
    }

    public static Instrumentation.ActivityResult buildAnIntentResult(String putExtraStr, Event event) {
        Intent resultData = new Intent();
        resultData.putExtra(putExtraStr, event);
        resultData.putExtra(NEW_EVENT, true);
        return new Instrumentation.ActivityResult(Activity
                .RESULT_OK, resultData);
    }

    private void stubOutActivity(String className, Instrumentation.ActivityResult result) {
        intending(IntentMatchers.
                hasComponent(hasClassName(className))).respondWith(result);
    }

    @Test
    public void testReturnValueFromMealActivity() {
        ldt = LocalDateTime.of(2017, Month.MAY, 23, 16, 0);
        // Create a meal object
        Tag sugar = new Tag(ldt, "banana", 2.0);
        Tag honey = new Tag(ldt, "honey", 1.0);
        tags.add(sugar);
        tags.add(honey);
        Meal meal = new Meal(ldt, tags, 1.0);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_EVENT_SERIALIZABLE, meal);
        stubOutActivity(MealActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.mealBtn), withContentDescription("Meal"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //check that an item exists in RecyclerView that has Time 16:00 and portions 2.5
        onView(allOf(

                //all meal items has portions id
                withId(R.id.meal_item_container),
                //firstLine
                hasDescendant(withText("16:00")),
                //nr of portions, very sensitiv to changes of text ("Portions..."). Not so good, change in some way?
                hasDescendant(withText("Portions: 1.0")),

                //tags in item
                hasDescendant(withText("banana")),
                hasDescendant(withChild(withText("honey")))
        ))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testReturnValueFromOtherActivity() {
        ldt = LocalDateTime.of(2017, Month.MAY, 24, 23, 22);

        tags.add(new Tag(ldt, "happy", 1.0));
        Other other = new Other(ldt, tags);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_EVENT_SERIALIZABLE,
                other);
        stubOutActivity(OtherActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.otherBtn), withContentDescription("Other"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //check that an item exists in RecyclerView that has Time 16:00 and portions 2.5
        onView(allOf(

                //all meal items has portions id
                withId(R.id.other_item_container),
                //firstLine
                hasDescendant(withText("23:22")),

                //tags in item
                hasDescendant(withChild(withText("happy"))
                )))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testReturnValueFromExerciseActivity() {
        // Create a meal object with timeView 16:00
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 23, 17, 30);
        Tag running = new Tag(ldt, "running", 1.0);

        //a 4 means Intense
        Exercise exercise = new Exercise(ldt, running, 4);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_EVENT_SERIALIZABLE,
                exercise);
        stubOutActivity(ExerciseActivity.class.getName(), result);

        //Press Exercise Btn
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.exerciseBtn), withContentDescription("Exercise"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());



        //check that a relevant item exists in RecyclerView
        onView(allOf(
                //use the image
                withId(R.id.exercise_item_container),
                //withText("17:30"),
                //timeView
                hasDescendant(withText("17:30")),

                //tag
                hasDescendant(withText("running")),

                //intensity
                hasDescendant(withText("Intense"))
        ))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testReturnValueFromBMActivity() {
        // Create a meal object with timeView 16:00
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 23, 18, 0);
        Bm bm = new Bm(ldt, 5, 2);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_EVENT_SERIALIZABLE,
                bm);
        stubOutActivity(BmActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.bmBtn), withContentDescription("Bowel Movement"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //check that a relevant item exists in RecyclerView
        onView(allOf(
                withId(R.id.bm_item_container), //use the image
                //timeView
                hasDescendant(withText("18:00")),

                //tag
                hasDescendant(withText("Phenomenal")),

                //intensity
                hasDescendant(withText("Bristol: 2"))
        ))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testReturnValueFromRatingActivity() {
        // Create a meal object with timeView 16:00
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 23, 19, 0);
        Rating rating = new Rating(ldt, 6);

        Instrumentation.ActivityResult result = buildAnIntentResult(RETURN_EVENT_SERIALIZABLE,
                rating);
        stubOutActivity(RatingActivity.class.getName(), result);

        //now press click of MealBtn that makes us go to MealActivity stub above
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.ratingBtn), withContentDescription("Rating"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //check that a relevant item exists in RecyclerView
        onView(allOf(
                //use the image
                withId(R.id.rating_item_container),
                //timeView
                hasDescendant(withText("19:00")),

                //rating/after
                hasDescendant(withText("Great"))
        ))
                .check(matches(isDisplayed()));
    }
    @Test
    public void mealAddedAfterOtherMealHasOtherTime() {
        // Create a meal object with timeView 19:00
        LocalDateTime ldt = LocalDateTime.of(2017, Month.MAY, 23, 19, 0);
        Meal meal1 = new Meal(ldt, new ArrayList<Tag>(), 1.0);


        Instrumentation.ActivityResult result1 = buildAnIntentResult(RETURN_EVENT_SERIALIZABLE,
                meal1);
        stubOutActivity(MealActivity.class.getName(), result1);



        //now press click of MealBtn that makes us go to MealActivity stub above
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.mealBtn), withContentDescription("Meal"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        //do same thing again with another meal-object
        LocalDateTime ldt2 = LocalDateTime.of(2017, Month.MAY, 23, 20, 0);
        Meal meal2 = new Meal(ldt2, new ArrayList<Tag>(), 1.0);
        Instrumentation.ActivityResult result2 = buildAnIntentResult(RETURN_EVENT_SERIALIZABLE,
                meal2);
        stubOutActivity(MealActivity.class.getName(), result2);
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.mealBtn), withContentDescription("Meal"),
                        withParent(withId(R.id.buttons)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        //#1 check that a relevant item exists in RecyclerView
        onView(allOf(
                //use the image
                withId(R.id.meal_item_container),
                //timeView
                hasDescendant(withText("19:00"))
        ))
                .check(matches(isDisplayed()));

        //#2 check that a relevant item exists in RecyclerView
        onView(allOf(
                //use the image
                withId(R.id.meal_item_container),
                //timeView
                hasDescendant(withText("20:00"))
        ))
                .check(matches(isDisplayed()));
    }
}
