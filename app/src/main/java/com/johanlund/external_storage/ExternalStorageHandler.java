package com.johanlund.external_storage;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.johanlund.dao.Dao;
import com.johanlund.dao.SqLiteDao;
import com.johanlund.database.entities.BmEntity;
import com.johanlund.database.entities.EntityBase;
import com.johanlund.database.entities.ExerciseEntity;
import com.johanlund.database.entities.MealEntity;
import com.johanlund.database.entities.OtherEntity;
import com.johanlund.database.entities.RatingEntity;
import com.johanlund.database.entities.TagEntity;
import com.johanlund.database.entities.TagTypeEntity;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.threeten.bp.LocalDateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.List;

import okio.BufferedSource;

import static com.johanlund.constants.Constants.DIRECTORY_IBSFOODANALYZER;
import static com.johanlund.constants.Constants.REQUEST_PERMISSION_READ_TO_EXTERNAL_STORAGE;
import static com.johanlund.constants.Constants.REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE;
import static com.johanlund.database.TablesAndStrings.DATABASE_NAME;
import static com.johanlund.database.TablesAndStrings.DATABASE_VERSION;

/**
 * Created by Johan on 2017-05-31.
 * <p>
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
                        .WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE,
                        activity);
            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE, activity);
            }
        } else {
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
                        .READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_READ_TO_EXTERNAL_STORAGE,
                        activity);
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        REQUEST_PERMISSION_READ_TO_EXTERNAL_STORAGE, activity);
            }
        } else {
            Toast.makeText(activity, "Permission (already) Granted!", Toast
                    .LENGTH_SHORT).show();
        }
    }

    private static void requestPermission(String permissionName, int permissionRequestCode,
                                          Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permissionName}, permissionRequestCode);
    }

    public static File getFolderToSaveIn() {
        return Environment.getExternalStoragePublicDirectory
                (DIRECTORY_IBSFOODANALYZER);
    }

    private static File makeFileToSaveTo(String nameOfFile) {
        File directoryToSaveIn = getFolderToSaveIn();
        if (!directoryToSaveIn.exists()) {
            directoryToSaveIn.mkdirs();
        }
        File file = new File(directoryToSaveIn, nameOfFile);
        if (!isExternalStorageAccessable()) {
            file = null;
        }
        return file;
    }

    //N.B! If this doesnt work for new Mime-types (that is, you cannot se file in windows),
    // it usually works if you restart mobile!!!
    //
    // mediascannerconnection is implemented for windows explorer to see file see =>
    /*https://stackoverflow
     .com/questions/32789157/how-to-write-files-to-external-public-storage-in
     -android-so-that-they-are-visibl*/
    //and
    /*https://stackoverflow
     .com/questions/4646913/android-how-to-use-mediascannerconnection-scanfile*/
    private static void scanFile(Context c, File f, final String mimeType) {
        MediaScannerConnection.scanFile(c, new String[]{f.getAbsolutePath()}, new
                String[]{mimeType}, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(final String path, final Uri uri) {
            }
        });
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
            File backupDB = makeFileToSaveTo("v_"+DATABASE_VERSION +"_"+LocalDateTime.now() + "_" + DATABASE_NAME);
            File currentDB = c.getDatabasePath(DATABASE_NAME);
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();

                scanFile(c, backupDB, "application/x-sqlite3");
                FileOutputStream fos = new FileOutputStream(backupDB);

                FileChannel dst = fos.getChannel();
                dst.transferFrom(src, 0, src.size());
                fos.flush();
                src.close();
                // dst.close();
                fos.close();

            } else {
            }

        } catch (
                Exception e)


        {
            Log.e("", e.getStackTrace().toString());
        }

    }
    public static String retrieveJson(List<? extends EntityBase>entities, Class clazz){
        List<? extends EntityBase> tagTypesEntities = entities;
        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, clazz);
        JsonAdapter<List<? extends EntityBase>> tagTypeAdapter = moshi.adapter(listMyData);
        return tagTypeAdapter.toJson(tagTypesEntities);
    }

    /**
     * Saves db to a json file. NB: eventstemplates are not saved.
     */
    public static void saveToJsonFile(Context context) {
        File outFile = makeFileToSaveTo("v_"+DATABASE_VERSION +"_"+LocalDateTime.now() + ".json");
        Dao dao = new SqLiteDao(context);

        String tagTypes = retrieveJson(dao.getTagTypesEntities(), TagTypeEntity.class);
        String tags = retrieveJson(dao.getTagsEntities(), TagEntity.class);
        String meals = retrieveJson(dao.getMealsEntities(), MealEntity.class);
        String others = retrieveJson(dao.getOthersEntities(), OtherEntity.class);
        String exercises = retrieveJson(dao.getExercisesEntities(), ExerciseEntity.class);
        String bms = retrieveJson(dao.getBmsEntities(), BmEntity.class);
        String ratings = retrieveJson(dao.getRatingsEntities(), RatingEntity.class);

        //TagTypes
        /*List<TagTypeEntity> tagTypesEntities = dao.getTagTypesEntities();
        Moshi moshi = new Moshi.Builder().build();
        Type listMyData = Types.newParameterizedType(List.class, TagTypeEntity.class);
        JsonAdapter<List<TagTypeEntity>> tagTypeAdapter = moshi.adapter(listMyData);
        String tagTypesJson = tagTypeAdapter.toJson(tagTypesEntities);

        List<TagEntity> tagEntities = dao.getTagEntities();
        Moshi moshi2 = new Moshi.Builder().build();
        Type listMyData2 = Types.newParameterizedType(List.class, TagEntity.class);
        JsonAdapter<List<TagEntity>> tagAdapter = moshi.adapter(listMyData2);
        String tagsJson = tagAdapter.toJson(tagEntities);

        List<MealEntity> mealEntities = dao.getMealEntities();
        Moshi moshi3 = new Moshi.Builder().build();
        Type mealData = Types.newParameterizedType(List.class, MealEntity.class);
        JsonAdapter<List<MealEntity>> mealAdapter = moshi.adapter(mealData);
        String mealsJson = mealAdapter.toJson(mealEntities);

        List<OtherEntity> mealEntities = dao.getOtherEntities();
        Moshi moshi4 = new Moshi.Builder().build();
        Type listMyData4 = Types.newParameterizedType(List.class, OtherEntity.class);
        JsonAdapter<List<MealEntity>> otherAdapter = moshi.adapter(listMyData4);
        String othersJson = otherAdapter.toJson(otherEntities);

        List<ExerciseEntity> exercisesEntities = dao.getExerciseEntities();
        Moshi moshi4 = new Moshi.Builder().build();
        Type exerciseData = Types.newParameterizedType(List.class, ExerciseEntity.class);
        JsonAdapter<List<ExerciseEntity>> exerciseAdapter = moshi.adapter(exerciseData);
        String exercisesJson = exerciseAdapter.toJson(exerciseEntities);

        List<MealEntity> mealEntities = dao.getMealEntities();
        Moshi moshi3 = new Moshi.Builder().build();
        Type listMyData3 = Types.newParameterizedType(List.class, MealEntity.class);
        JsonAdapter<List<MealEntity>> mealAdapter = moshi.adapter(listMyData3);
        String mealsJson = mealAdapter.toJson(mealEntities);

        List<MealEntity> mealEntities = dao.getMealEntities();
        Moshi moshi3 = new Moshi.Builder().build();
        Type listMyData3 = Types.newParameterizedType(List.class, MealEntity.class);
        JsonAdapter<List<MealEntity>> mealAdapter = moshi.adapter(listMyData3);
        String mealsJson = mealAdapter.toJson(mealEntities);*/



        try {
            PrintWriter out = new PrintWriter(outFile);
            scanFile(context, outFile, "application/json");
            out.println("{ \"tagTypes\": ");
            out.println(tagTypes);
            out.println(", \"tags\": ");
            out.println(tags);
            out.println(", \"meals\": ");
            out.println(meals);
            out.println(", \"others\": ");
            out.println(others);
            out.println(", \"exercises\": ");
            out.println(exercises);
            out.println(", \"bms\": ");
            out.println(bms);
            out.println(", \"ratings\": ");
            out.println(ratings);
            out.println('}');
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves all events to a csv file. NB: eventstemplates are not saved.
     */
  /*  public static void saveCSVFile(Context context) {
        File outFile = makeFileToSaveTo(LocalDateTime.now() + ".csv");
        DBHandler dbHandler = new DBHandler(context);
        List<Event>allEvents = dbHandler.getAllEventsMinusEventsTemplatesSorted();
        try {
            Exporter.saveToTxt(outFile, allEvents);
            scanFile(context, outFile, "text/csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void saveCSVForGraphFile(Context context) {
        File outFile = makeFileToSaveTo(LocalDateTime.now() + ".csv");
        DBHandler dbHandler = new DBHandler(context);
        List<Event>allEvents = dbHandler.getAllEventsMinusEventsTemplatesSorted();
        try {
            Exporter.saveTimeAndScoreToTxt(outFile, allEvents);
            scanFile(context, outFile, "text/csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
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
}



