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
    public final static String DIRECTORY_IBSFOODANALYZER = "ibsFoodAnalyzer";
    public final static int REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE = 9999;
    //Other
    public final static int TAGS_TO_ADD = 4444;
    public final static int TAGTEMPLATE_TO_ADD = 4445;
    public final static String RETURN_TAG_TEMPLATE_SERIALIZABLE = "returnTagTemplateSer";

    public final static String TAGTEMPLATE_ID = "id_of_tagtemplate";
    public final static int TYPE_OF_1 = 1;
    public final static int TYPE_OF_2 = 2;
    public final static int TYPE_OF_3 = 3;
    public final static String WHICH_TYPE_OF = "whichOfTheThreeTypes";
    public final static String WHICH_TYPE = "whichOfTheTypes";
    public final static String PUT_TAG_TEMPLATE = "tagTemplateToBeSent";
    public final static int NEW_TYPE_FOR_TAGTEMPLATE = 8888;
    public final static String RETURN_MEAL_SERIALIZABLE = "returnMealSer";
    public final static String RETURN_OTHER_SERIALIZABLE = "returnOtherSer";
    public final static String RETURN_EXERCISE_SERIALIZABLE = "returnExerciseSer";
    public final static String RETURN_BM_SERIALIZABLE = "returnBmSer";
    public final static String RETURN_RATING_SERIALIZABLE = "returnRatingSer";
    //sent to new Event activities if all you want to do is change an event
    public final static String EVENT_TO_CHANGE = "eventToChange";
    //changed event or new event?
    public final static String CHANGED_EVENT = "changedEvent";
    //position of event in list, same position but used at different places.
    public final static String EVENT_POSITION = "eventPosition";
    public final static String POS_OF_EVENT_RETURNED = "eventPositionReturned";
    //same id but used at different places.
    public final static String ID_OF_EVENT = "idOfEvent";
    public final static String ID_OF_EVENT_RETURNED = "idOfEventReturned";

 //sent to Activity handling of EventsTemplates
    public final static String EVENTSTEMPLATE_TO_CHANGE = "eventsTemplateToChange";

    //=>
    public final static String EVENTSTEMPLATE_TO_LOAD = "eventsTemplateToLoad";

    //<=
    public final static String EVENTS_TO_LOAD = "eventsToLoad";
    public final static int LOAD_EVENTS_FROM_EVENTSTEMPLATE = 9876;

    public final static String ID_OF_EVENTSTEMPLATE = "idOfEventsTemplate";
    public final static String LIST_OF_EVENTS = "listOfEvents";
    //used in various cases
    public static final int MEAL = 0, OTHER = 1, EXERCISE = 2, BM = 3, RATING = 4, DATE_MARKER = 5;
    //***************************************STATISTICS*********************************************
    //for StatFragment and its adapter
    public static final int AVG_SCORE = 0, BLUE_ZONE_SCORE = 1, COMPLETENESS_SCORE = 2,
            BRISTOL_SCORE = 3, UPDATE = 4, SETTINGS = 5;

    //Rating 6 == Great, Rating 7 == Phenomenal
    public static final int BLUE_ZONE_RATING_LIMIT_INCLUSIVE = 6;
    public static final double SCORE_BLUEZONES_FROM = 6;
    public static final double MAX_RATING_SCORE = 7;
    //Portions
    //====================FOR HEURISTIC TOOLS=================================
    public static int HOURS_AHEAD_FOR_AVG = 24; //in hours
    public static final int HOURS_AHEAD_FOR_BLUEZONES = 24; //in hours
    public static final int HOURS_AHEAD_FOR_COMPLETE = 30; //in hours
    public static final int HOURS_AHEAD_FOR_BRISTOL = 30; //in hours
    public static final ZoneId ZONE_ID = ZoneId.systemDefault();
    //==================== HEURISTIC FLAGS======================================
    public static String PACKAGE_NAME;
    public static String SHOULD_HAVE_DATE = "shouldHaveDate";
    //=================================TIME ZONE===================================

    //=================================SETTINGS VARIABLES APP===================================
    public static final int HOURS_AHEAD_FOR_BREAK_BACKUP = 20; //in hours


}
