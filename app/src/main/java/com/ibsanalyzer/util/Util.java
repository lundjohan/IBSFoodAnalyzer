package com.ibsanalyzer.util;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.ibsanalyzer.base_classes.Event;

import static android.app.Activity.RESULT_OK;
import static com.ibsanalyzer.constants.Constants.RETURN_EXERCISE_JSON;

/**
 * Created by Johan on 2017-05-14.
 */

/**
 * Class is made for repeating usages through project
 */

public class Util {
    /**
     * Common usage for EventActivities inside the finish() method (Activity has been called from other Activity via startActivityForResult)
     */
    public static void jsonAndMoreFinishingData(Event event, String putExtraString, Activity usingActivity){
        Gson gson = new Gson();
        String eventAsJSON = gson.toJson(event);

        Intent data = new Intent();
        data.putExtra(putExtraString, eventAsJSON);
        usingActivity.setResult(RESULT_OK, data);
    }
    public static void dataComingBackFromTagAdder(){

    }
}
