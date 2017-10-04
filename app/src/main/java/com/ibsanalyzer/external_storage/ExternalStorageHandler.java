package com.ibsanalyzer.external_storage;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.importer.Importer;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.DIRECTORY_IBSFOODANALYZER;
import static com.ibsanalyzer.constants.Constants.NAME_OF_TXT_FILE;
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
    public static void showWritablePermission(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission
                                .WRITE_EXTERNAL_STORAGE,
                        REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE,
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

    /**
     * Nota Bene! If file or folder isn't showing up in Windows File Explorer - restart the
     * device (it flushes).
     * It has to do with Microsoft mtb.
     * MediaScanner is SUPPOSED to be solution to this but not fully (since a reboot of phone is
     * needed).
     *
     * @param c
     */
    public static void saveDBToExtStorage(Context c) {
        try {
            c.getDatabasePath(DATABASE_NAME);
            //Nota
            File directoryToSaveIn = Environment.getExternalStoragePublicDirectory
                    (DIRECTORY_IBSFOODANALYZER);
            File data = Environment.getDataDirectory();
            String backupDBPath = LocalDateTime.now() + "_" + DATABASE_NAME;
            File currentDB = c.getDatabasePath(DATABASE_NAME);
            if (!isExternalStorageAccessable()) {
                return;
            }

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                if (!directoryToSaveIn.exists()) {
                    directoryToSaveIn.mkdirs();
                }
                File backupDB = new File(directoryToSaveIn, backupDBPath);

                //mediascannerconnection is implemented for windows explorer to see file see =>
                //https://stackoverflow
                // .com/questions/32789157/how-to-write-files-to-external-public-storage-in
                // -android-so-that-they-are-visibl
                //and
                //https://stackoverflow
                // .com/questions/4646913/android-how-to-use-mediascannerconnection-scanfile
                SingleMediaScanner mediaScanner = new SingleMediaScanner(c, backupDB,
                        "application/x-sqlite3");
                mediaScanner.scan();
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

    private static boolean isExternalStorageAccessable() {

        //override the other upmost file with new
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static void replaceDBWithExtStorageFile(File backupDB, Context c) {
        //get current path to internal storage db file
        File data = Environment.getDataDirectory();
        File pathToCurrentDB = c.getDatabasePath(DATABASE_NAME);
        try {
            FileChannel src = new FileInputStream(backupDB).getChannel();
            FileOutputStream fos = new FileOutputStream(pathToCurrentDB);
            FileChannel dst = fos.getChannel();
            dst.transferFrom(src, 0, src.size());
            fos.flush();
            src.close();
            dst.close();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads events from a txt file
     * and store them (together with TagTemplates)
     * in database
     */
    public static List<Event> importEventsFromTxt() {
        List<Event> importedEvents = new ArrayList<>();

        //get permissions to area
        File sd = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS);
        String pathToTxtFile = NAME_OF_TXT_FILE;
        if (!isExternalStorageAccessable()) {
            //try to fix it
        }
        File file = new File(sd, pathToTxtFile);
        //read in each row
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (String line; (line = br.readLine()) != null; ) {
                try {
                    Event event = Importer.lineToEvent(line);
                    importedEvents.add(event);
                } catch (Exception e) {
                    Log.e("Error", "One event could not be imported, skipping row.");
                }
            }
            // line is not visible here.
        } catch (Exception e) {
            Log.e("Error", "Something went wrong when reading from file");
        }
        return importedEvents;
    }

}



