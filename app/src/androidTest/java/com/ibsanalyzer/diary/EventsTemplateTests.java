package com.ibsanalyzer.diary;

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

/**
 * Created by Johan on 2018-01-09.
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
    public void creatingAEventsTemplateTest() {
        //go to correct date.

        //save event to eventstemplates

        //ok inside templates view?

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
