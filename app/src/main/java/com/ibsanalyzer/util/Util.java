package com.ibsanalyzer.util;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;

import static android.app.Activity.RESULT_OK;

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
    public static void jsonAndMoreFinishingData(Object obj, String putExtraString, Activity usingActivity){
        Gson gson = new Gson();
        String objAsJSON = gson.toJson(obj);

        Intent data = new Intent();
        data.putExtra(putExtraString, objAsJSON);
        usingActivity.setResult(RESULT_OK, data);
    }
    public static void dataComingBackFromTagAdder(){

    }
}
