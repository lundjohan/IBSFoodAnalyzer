package com.ibsanalyzer.external_storage;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Johan on 2018-01-16.
 */

public class SaveToCSVForGraphIntentService extends IntentService {
public SaveToCSVForGraphIntentService() {
    super("SaveToCSVForGraphIntentService");
}

        public SaveToCSVForGraphIntentService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            ExternalStorageHandler.saveCSVForGraphFile(getApplicationContext());
        }
}
