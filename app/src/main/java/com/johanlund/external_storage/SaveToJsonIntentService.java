package com.johanlund.external_storage;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class SaveToJsonIntentService extends IntentService {

    /**
     * Empty constructor is needed
     */
    public SaveToJsonIntentService() {
        super("SaveToJsonIntentService");
    }

    public SaveToJsonIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ExternalStorageHandler.saveToJsonFile(getApplicationContext());
    }
}