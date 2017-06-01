package com.ibsanalyzer.database;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.ibsanalyzer.constants.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import static com.ibsanalyzer.constants.Constants.CURRENT_DB_PATH;
import static com.ibsanalyzer.constants.Constants.REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE;
import static com.ibsanalyzer.database.TablesAndStrings.DATABASE_NAME;

/**
 * Created by Johan on 2017-05-31.
 */

public class ExternalStorageHandler {

    protected static boolean hasPermissions(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private static void askPermissions(Activity thisActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                Manifest.permission.READ_CONTACTS)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    public static void askForPermissionIfNeeded(Activity activity) {
        // Check if we have write permission
        if (!hasPermissions(activity)) {
            askPermissions(activity);
        }
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);
    }

    //https://stackoverflow.com/questions/35484767/activitycompat-requestpermissions-not-showing
    // -dialog-box
    private static void showWritablePermission(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE,
                        activity);
            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE, activity);
            }
        } else {
            Log.d("Debug", "Permission is already granted for writing to external storage");
            Toast.makeText(activity, "Permission (already) Granted!", Toast
                    .LENGTH_SHORT).show();
        }
    }

    private static void showExplanation(String title,
                                        String message,
                                        final String permission,
                                        final int permissionRequestCode, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode, activity);
                    }
                });
        builder.create().show();
    }

    private static void requestPermission(String permissionName, int permissionRequestCode,
                                          Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permissionName}, permissionRequestCode);
    }

    public static void saveDBToExtStorage(Activity activity) {
        //from https://stackoverflow.com/questions/1995320/how-do-i-backup-a-database-file-to-the
        // -sd-card-on-android

        try {

            File sd = Environment.getExternalStoragePublicDirectory(Environment
                    .DIRECTORY_DOWNLOADS);
            File data = Environment.getDataDirectory();
            showWritablePermission(activity);
            String backupDBPath = DATABASE_NAME;
            File currentDB = new File(data, CURRENT_DB_PATH);
            if (!isExternalStorageWritable()){
                return;
            }

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                if (!sd.exists()) {
                    sd.mkdirs();
                }
                File backupDB = new File(sd, backupDBPath);
                FileOutputStream fos = new FileOutputStream(backupDB);

                FileChannel dst = fos.getChannel();
                dst.transferFrom(src, 0, src.size());
                fos.flush();
                src.close();
                // dst.close();
                fos.close();

            } else {
                Log.d("Debug", "There seem to be no database to save");
            }

        } catch (
                Exception e)


        {
            Log.e("", e.getStackTrace().toString());
            Log.d("Debug", "Problems in saving database to external storage");
        }

    }
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public static void replaceDBWithExtStorageFile(Activity activity) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }


}
