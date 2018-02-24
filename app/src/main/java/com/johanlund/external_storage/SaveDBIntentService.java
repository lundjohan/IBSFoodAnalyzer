package com.johanlund.external_storage;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SaveDBIntentService extends IntentService {

    /**
     * Empty constructor is needed
     */
    public SaveDBIntentService() {
        super("SaveDBIntentService");
    }

    public SaveDBIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ExternalStorageHandler.saveDBToExtStorage(getApplicationContext());
    }
}
