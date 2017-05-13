package com.ibsanalyzer.application;

import android.app.Application;
import android.os.StrictMode;

import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * Created by Johan on 2017-04-06.
 */

public class MyCustomApplication extends Application {
    private static final boolean DEVELOPER_MODE = false;
    @Override
    public void onCreate() {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        super.onCreate();



        AndroidThreeTen.init(this);
    }
}
