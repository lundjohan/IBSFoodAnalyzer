package com.ibsanalyzer.help_classes;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.drawer.DrawerActivity;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Johan on 2018-01-01.
 */

public class AndroidTestUtil {
    public static void clearDatabase(Context context){
        DBHandler dbHandler = new DBHandler(context);
        dbHandler.deleteAllTablesRows();

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Clear Database"), isDisplayed
                        ()));
        appCompatCheckedTextView.perform(click());
    }

    /*
    In case context is not available
     */
    public static void clearDatabase() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText("Clear Database"), isDisplayed
                        ()));
        appCompatCheckedTextView.perform(click());
    }
    public static void changeDate (ActivityTestRule<DrawerActivity> activityTestRule, LocalDate date){
        activityTestRule.getActivity().changeDate(date);
    }
}
