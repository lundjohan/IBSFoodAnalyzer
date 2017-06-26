package com.ibsanalyzer.constants;

import com.ibsanalyzer.inputday.BuildConfig;

import org.threeten.bp.ZoneId;

import static com.ibsanalyzer.database.TablesAndStrings.DATABASE_NAME;

/**
 * Created by Johan on 2017-05-13.
 */

public class Constants {
    //reading from txt files
    public final static String NAME_OF_TXT_FILE = "out.txt";
    public final static String DELIMETER = "|";


    //Database operations
    public final static String CURRENT_DB_PATH = "//data//" + BuildConfig.APPLICATION_ID +
            "//databases//" +
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

    //***************************************STATISTICS*********************************************
    //for StatFragment and its adapter
    public static final int AVG_SCORE = 0, BLUE_ZONE_SCORE = 1, COMPLETENESS_SCORE = 2,
            BRISTOL_SCORE = 3, UPDATE = 4;

    //Rating 6 == Great, Rating 7 == Phenomenal
    public static final int BLUE_ZONE_RATING_LIMIT_INCLUSIVE = 6;

    //==================== HEURISTIC FLAGS======================================
    static final boolean DO_RELATIONS = true;
    static final boolean DO_JUMP = true;
    static final boolean DO_BM_COMPLETES = true;
    //====================FOR HEURISTIC TOOLS=================================
    //Blue Zones

    public static final double SCORE_BLUEZONES_FROM = 6;
    public static final double MAX_RATING_SCORE = 7;
    //In future perhaps this is better
    static final int PERCENTS_THAT_ARE_BLUEZONES = 5;

    //Portions
    public static final long HOURS_COHERENT_TIME_FOR_PORTIONS = 30;
    public static final long ONE_PORTION = 1;
    public static final long TWO_PORTIONS = 2;
    public static final long THREE_PORTIONS = 3;
    public static final long FOUR_PORTIONS = 4;

    //BM
    static final long HOURS_AHEAD_FOR_BM = 30;

    public static final int HOURS_AHEAD_FOR_AVG = 24; //in hours
    public static final int HOURS_AHEAD_FOR_BLUEZONES = 24; //in hours
    public static final int HOURS_AHEAD_FOR_COMPLETE = 30; //in hours
    public static final int HOURS_AHEAD_FOR_BRISTOL = 30; //in hours

    public static final double PLUS_MINUS_QUOTIENT = 1;
    public static final double MAX_SCORE = 7;
    public static final double MIN_SCORE = 1;

    //=================================TIME ZONE===================================
    ZoneId zoneId = ZoneId.systemDefault();
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

}
