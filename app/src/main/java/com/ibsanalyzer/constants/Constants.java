package com.ibsanalyzer.constants;

import org.threeten.bp.ZoneId;

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
    public final static int REQUEST_PERMISSION_READ_TO_EXTERNAL_STORAGE = 9998;
    public final static int IMPORT_DATABASE = 1357;
    public final static int IMPORT_FROM_CSV_FILE = 1358;

    //Other

    //Swipe in Diary
    public final static String SWIPING_TO_DATE = "swipingToDate";


    public final static int TAGS_TO_ADD = 4444;
    public final static int TAGTEMPLATE_TO_ADD = 4445;
    public final static String RETURN_TAG_TEMPLATE_SERIALIZABLE = "returnTagTemplateSer";
    public final static String WHICH_TYPE = "whichOfTheTypes";
    public final static String PUT_TAG_TEMPLATE = "tagTemplateToBeSent";
    public final static String TAGNAME_SEARCH_STRING = "tagNameSearchString";
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
    public final static String NEW_EVENT = "newEvent";
    //position of event in list, same position but used at different places.
    public final static String EVENT_POSITION = "eventPosition";
    public final static String POS_OF_EVENT_RETURNED = "eventPositionReturned";
    //same id but used at different places.
    public final static String ID_OF_EVENT = "idOfEvent";
    public final static String ID_OF_EVENT_RETURNED = "idOfEventReturned";

    public final static String DATE_TO_START_NEW_EVENTACTIVITY = "dateToStartNewEventActivity";

    //For TagTemplateEditActivity
    public final static int TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED = 1357;
    public final static String TAG_TEMPLATE_POS = "posOfTagTemplateInList";
    public final static String TAG_TEMPLATE_TO_EDIT = "tagTemplateToEdit";
    public final static String TAG_TEMPLATE_ID = "tagTemplateId";

    //Other uses for TagTemplates
    public final static String TAG_TEMPLATE_MIGHT_HAVE_BEEN_EDITED_OR_DELETED = "delete_or_edited_tagTemplate";

    //note: IDS_OF_EDITED_TAG_TEMPLATES & IDS_OF_EARLIER_EDITED_TAG_TEMPLATES are closely related.
    // TagAdderActivity -> TagEventActivity, //TagTemplateEditActivity -> TagAdderActivity
    public final static String IDS_OF_EDITED_TAG_TEMPLATES = "idsOfEditedTagTemplates";

    //TagAdderActivity -> TagTemplateEditActivity
    public final static String IDS_OF_EARLIER_EDITED_TAG_TEMPLATES = "idsOfEditedTagTemplates";
 //sent to Activity handling of EventsTemplates
    public final static String EVENTSTEMPLATE_TO_CHANGE = "eventsTemplateToChange";

    //=>
    public final static String EVENTSTEMPLATE_TO_LOAD = "eventsTemplateToLoad";

    //<=
    public final static String EVENTS_TO_LOAD = "eventsToLoad";
    public final static int LOAD_EVENTS_FROM_EVENTSTEMPLATE = 9876;

    public final static String ID_OF_EVENTSTEMPLATE = "idOfEventsTemplate";
    public final static String LIST_OF_EVENTS = "listOfEvents";

    public final static String LOCALDATE = "localDateToStartDiaryIn";
    //used in various cases
    public static final int MEAL = 0, OTHER = 1, EXERCISE = 2, BM = 3, RATING = 4;
    //***************************************STATISTICS*********************************************
    //portion ranges
    public static final int NEW_PORTION_RANGES = 9876;
    public final static String CHOSEN_FROM_RANGE = "chosenFromRange";
    public final static String CHOSEN_TO_RANGE = "chosenToRange";


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

    //=================================INFO===================================
    public static String INFO_STR = "shouldHaveDate";
}
