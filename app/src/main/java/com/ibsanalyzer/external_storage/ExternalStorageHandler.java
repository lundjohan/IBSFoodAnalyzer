package com.ibsanalyzer.external_storage;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.importer_and_exporter.Exporter;
import com.ibsanalyzer.importer_and_exporter.Importer;

import org.threeten.bp.LocalDateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.DIRECTORY_IBSFOODANALYZER;
import static com.ibsanalyzer.constants.Constants.REQUEST_PERMISSION_READ_TO_EXTERNAL_STORAGE;
import static com.ibsanalyzer.constants.Constants.REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE;
import static com.ibsanalyzer.database.TablesAndStrings.DATABASE_NAME;

/**
 * Created by Johan on 2017-05-31.
 *
 * To check if permissions work, disable permissions in Android Settings for app.
 */

public class ExternalStorageHandler {

    protected static boolean hasReadablePermissions(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    //https://stackoverflow.com/questions/35484767/activitycompat-requestpermissions-not-showing
    // -dialog-box
    public static void showWritablePermission(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermission(Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE, activity);
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
    public static void showReadablePermission(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermission(Manifest.permission
                        .READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_READ_TO_EXTERNAL_STORAGE, activity);
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        REQUEST_PERMISSION_READ_TO_EXTERNAL_STORAGE, activity);
            }
        } else {
            Log.d("Debug", "Permission is already granted for reading from external storage");
            Toast.makeText(activity, "Permission (already) Granted!", Toast
                    .LENGTH_SHORT).show();
        }
    }

    private static void requestPermission(String permissionName, int permissionRequestCode,
                                          Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permissionName}, permissionRequestCode);
    }

    private static File makeFileToSaveTo(String nameOfFile){
        File directoryToSaveIn = Environment.getExternalStoragePublicDirectory
                (DIRECTORY_IBSFOODANALYZER);
        if (!directoryToSaveIn.exists()) {
            directoryToSaveIn.mkdirs();
        }
        File file = new File(directoryToSaveIn, nameOfFile);
        if (!isExternalStorageAccessable()) {
            file = null;
        }
        return file;
    }
    //mediascannerconnection is implemented for windows explorer to see file see =>
    /*https://stackoverflow
     .com/questions/32789157/how-to-write-files-to-external-public-storage-in
     -android-so-that-they-are-visibl*/
    //and
    /*https://stackoverflow
     .com/questions/4646913/android-how-to-use-mediascannerconnection-scanfile*/
    private static void scanFile(Context c, File f, String mimeType) {
        SingleMediaScanner mediaScanner = new SingleMediaScanner(c, f,
                mimeType);
        mediaScanner.scan();
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
            File backupDB = makeFileToSaveTo(LocalDateTime.now() + "_" + DATABASE_NAME);
            File currentDB = c.getDatabasePath(DATABASE_NAME);
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();

                scanFile(c,backupDB, "application/x-sqlite3");
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
    public static void saveCSVFile(Context context) {
        File outFile = makeFileToSaveTo(LocalDateTime.now() + ".csv");
        scanFile(context, outFile, "text/csv");
        DBHandler dbHandler = new DBHandler(context);
        List<Event>allEvents = dbHandler.getAllEventsMinusEventsTemplateSorted();
        try {
            Exporter.saveToTxt(outFile, allEvents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
    public static List<Event> importEventsFromCsv(File file) {
        List<Event> importedEvents = new ArrayList<>();

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



