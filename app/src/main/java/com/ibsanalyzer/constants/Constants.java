package com.ibsanalyzer.constants;

import com.ibsanalyzer.inputday.BuildConfig;

import static com.ibsanalyzer.database.TablesAndStrings.DATABASE_NAME;

/**
 * Created by Johan on 2017-05-13.
 */

public class Constants {
    //reading from txt files
    public final static String DELIMETER = "|";


    //Database operations
    public final static String CURRENT_DB_PATH = "//data//" + BuildConfig.APPLICATION_ID + "//databases//" +
            DATABASE_NAME;
    public final static int REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE = 9999;
    public static String PACKAGE_NAME;

    //Other
    public final static int TAGS_TO_ADD = 4444;
   /* public final static String RETURN_MEAL_JSON = "returnMealJSON";
    public final static String RETURN_OTHER_JSON = "returnOtherJSON";
    public final static String RETURN_EXERCISE_JSON = "returnExerciseJSON";
    public final static String RETURN_BM_JSON = "returnBmJSON";
    public final static String RETURN_RATING_JSON = "returnRatingJSON";
    public final static String RETURN_TAGTEMPLATE_JSON = "returnTagTemplateJSON";*/

    public final static String RETURN_TAG_TEMPLATE_SERIALIZABLE = "returnTagTemplateSer";
    public final static String RETURN_MEAL_SERIALIZABLE = "returnMealSer";
    public final static String RETURN_OTHER_SERIALIZABLE = "returnOtherSer";
    public final static String RETURN_EXERCISE_SERIALIZABLE = "returnExerciseSer";
    public final static String RETURN_BM_SERIALIZABLE = "returnBmSer";
    public final static String RETURN_RATING_SERIALIZABLE = "returnRatingSer";

    public final static String MARKED_EVENTS_JSON = "markedEventsJson";


    public final static String LIST_OF_EVENTS = "listOfEvents";

    //this is saved, so that user can come back to same positition (=same date) in DiaryFragment
    // as when he left
    public final static String POSITION_IN_DIARY = "position_in_diary";

    //interface passed as arg to bundle
    public static final String LISTENER_AS_ARG = "listener_interface";

    //used for backstack
    public static final String TEMPLATE_ADDER_FRAGMENT = "template_adder_fragment_tag";

    //public static final int NEW_EVENTSTEMPLATE = 5555;

    //used in various cases
    public static final int MEAL = 0, OTHER = 1, EXERCISE = 2, BM = 3, RATING = 4, DATE_MARKER = 5;


}
