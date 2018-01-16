package com.ibsanalyzer.external_storage;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Johan on 2018-01-16.
 */

public class SaveToCSVIntentService extends IntentService {

    /**
     * Empty constructor is needed
     */
    public SaveToCSVIntentService() {
        super("SaveToCSVIntentService");
    }

    public SaveToCSVIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ExternalStorageHandler.saveCSVFile(getApplicationContext());
    }
}