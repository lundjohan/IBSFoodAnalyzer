package com.ibsanalyzer.drawer;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ibsanalyzer.about.AboutActivity;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.diary.DiaryContainerFragment;
import com.ibsanalyzer.diary.DiaryFragment;
import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.diary.TemplateFragment;
import com.ibsanalyzer.external_storage.ExternalStorageHandler;
import com.ibsanalyzer.external_storage.SaveDBIntentService;
import com.ibsanalyzer.external_storage.SaveToCSVForGraphIntentService;
import com.ibsanalyzer.external_storage.SaveToCSVIntentService;
import com.ibsanalyzer.settings.GeneralSettingsActivity;
import com.ibsanalyzer.statistics.StatOptionsFragment;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.threeten.bp.LocalDate;

import java.io.File;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.DIARY_CONTAINER;
import static com.ibsanalyzer.constants.Constants.EVENTS_TO_LOAD;
import static com.ibsanalyzer.constants.Constants.IMPORT_DATABASE;
import static com.ibsanalyzer.constants.Constants.IMPORT_FROM_CSV_FILE;
import static com.ibsanalyzer.constants.Constants.LOAD_EVENTS_FROM_EVENTSTEMPLATE;
import static com.ibsanalyzer.constants.Constants.LOCALDATE;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DiaryFragment
        .DiaryFragmentListener, TemplateFragment
        .TemplateFragmentListener {

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    //used for restablishing date when pressing backButton from TemplateFragment
    LocalDate dateBeforeTemplate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initiateFragment();

        //TODO problably could easily integrate below into initiateFragment or something like that.
        //Problably very inefficient right now, with fragment being drawn several times over
        //and nb: this is called all the time
        if(savedInstanceState!= null){
            dateBeforeTemplate = (LocalDate)savedInstanceState.getSerializable(LOCALDATE);
            startDiaryAtDate(dateBeforeTemplate);

        }
        else{
            //this is important because it is cleaned of screen behind. Otherwise double views on top of each other.
            startDiaryAtLastDate();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        DiaryContainerFragment diaryContainer= (DiaryContainerFragment) getSupportFragmentManager().findFragmentByTag(DIARY_CONTAINER);
        if (diaryContainer != null && diaryContainer.isVisible()) {
            savedInstanceState.putSerializable(LOCALDATE, diaryContainer.extractDateFromDiary());
        }
    }
    private void initiateFragment() {
        Fragment fragment = new DiaryContainerFragment();
        FragmentManager transaction = getSupportFragmentManager();
        transaction.beginTransaction()
                .add(R.id.fragment_container, fragment, DIARY_CONTAINER)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager manager = getSupportFragmentManager();
        int sizeOfStack = manager.getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (sizeOfStack > 0) {
            backToDiaryFragment();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.three_dots_menu_item_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, GeneralSettingsActivity.class);
            startActivity(i);
            return true;
        }
        //this is solely used from TemplateFragment
        if (id == android.R.id.home) {
            backToDiaryFragment();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.nav_diary:
                fragment = new DiaryContainerFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, DIARY_CONTAINER)
                        .commit();
                break;

            case R.id.nav_statistics:
                fragment = new StatOptionsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, DIARY_CONTAINER)
                        .commit();
                break;

            case R.id.importMenuItem:
                //ok, read from external file? Otherwise ask for permission
                ExternalStorageHandler.showReadablePermission(this);
                showChooser();
                break;

            case R.id.exportMenuItem:
                //ok, write to file? Otherwise ask for permission
                ExternalStorageHandler.showWritablePermission(this);
                //IntentService
                Intent intent = new Intent(this, SaveDBIntentService.class);
                startService(intent);

                //show pop up that shows location where file was saved.
                showPopUpWithSavedFileLocationSavedFile(ExternalStorageHandler.getFolderToSaveIn());
                break;

            case R.id.importFromCsvMenuItem:
                //ok, read from external file? Otherwise ask for permission
                ExternalStorageHandler.showReadablePermission(this);
                showChooserForCSVImport();
                break;

            case R.id.exportToCsvMenuItem:
                //ok, write to file? Otherwise ask for permission
                ExternalStorageHandler.showWritablePermission(this);
                //IntentService
                Intent csvIntent = new Intent(this, SaveToCSVIntentService.class);
                startService(csvIntent);
                //show pop up that shows location where file was saved.
                showPopUpWithSavedFileLocationSavedFile(ExternalStorageHandler.getFolderToSaveIn());
                break;
            case R.id.exportCsvForGraph:
                //ok, write to file? Otherwise ask for permission
                ExternalStorageHandler.showWritablePermission(this);
                //IntentService
                Intent csvGraphIntent = new Intent(this, SaveToCSVForGraphIntentService.class);
                startService(csvGraphIntent);
                showPopUpWithSavedFileLocationSavedFile(ExternalStorageHandler.getFolderToSaveIn());
                break;
            case R.id.clearDBItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false).
                        setTitle("Clear Database?").
                        setMessage("Warning! Are you sure you want to erase all of the diary?").
                        setCancelable(true).
                        setNegativeButton(android.R.string.cancel, new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).
                        setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBHandler dbHandler = new DBHandler(getApplicationContext());
                                dbHandler.deleteAllTablesRowsExceptTagTemplates();
                                startDiaryAtDate(LocalDate.now());
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                break;

            case R.id.aboutItem:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return super.

                onOptionsItemSelected(item);
    }

    private void showPopUpWithSavedFileLocationSavedFile(File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false).
                setTitle("Saved file location").
                setCancelable(false).
                setMessage("Files are saved in folder " + file.getPath() + ". The folders except " +
                        "the last might differ depending on your system.").
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //empty, only this pop-up should be closed
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        //center positive button
        final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton
                .getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }


    //code reused from aFileChooser example
    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, IMPORT_DATABASE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    //code reused from aFileChooser example
    private void showChooserForCSVImport() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, IMPORT_FROM_CSV_FILE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void startTemplateFragment(LocalDate date) {
        //toggle.setHomeAsUpIndicator(null);
        Fragment fragment = new TemplateFragment();
        //save old date in case backbutton is pressen (spaghetti-code, can I get backstack to
        // work properly is this unnecessary)
        this.dateBeforeTemplate = date;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, DIARY_CONTAINER).addToBackStack(null)
                .commit();
    }

    @Override
    public void changeDate(LocalDate date) {
        startDiaryAtDate(date);
    }

    @Override
    public void addEventsFromEventsTemplateToDiary(List<Event> events) {

    }

    @Override
    public void fixToolBarForTemplateFragment() {
        toolbar.setTitle("Templates");
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //without this hamburger button is shown instead of back button
        //see https://stackoverflow.com/questions/27742074/up-arrow-does-not-show-after-calling
        // -actionbardrawertoggle-setdrawerindicatorena
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();


    }

    //called after back button of TemplateFragment is pressed
    //this is not nice code but couldn't get backstack to work together with drawer
    private void backToDiaryFragment() {
        //pop away TemplateFragment from BackStack
        getSupportFragmentManager().popBackStackImmediate();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);// unshow back button
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Fragment fragment = new DiaryContainerFragment();
        //here place date that was before (this whole method stinks of spaghetti code, but...)
        Bundle args = new Bundle();
        args.putSerializable(LOCALDATE, dateBeforeTemplate);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, DIARY_CONTAINER)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case IMPORT_DATABASE:
                if (data != null) {
                    File dbFileToImport = getChosenFile(data);
                    if (dbFileToImport != null) {
                        ImportDBAsyncTask asyncThread = new ImportDBAsyncTask(dbFileToImport);
                        asyncThread.execute(0);
                    }
                }
                break;
            case IMPORT_FROM_CSV_FILE:
                if (data != null) {
                    File csvFileToImport = getChosenFile(data);
                    if (csvFileToImport != null) {
                        ImportCsvAsyncTask asyncThread = new ImportCsvAsyncTask(csvFileToImport);
                        asyncThread.execute(0);
                    }
                }
                break;
            case LOAD_EVENTS_FROM_EVENTSTEMPLATE:
                if (data.hasExtra(EVENTS_TO_LOAD)) {
                    List<Event> eventsToReturn = (List<Event>) data.getSerializableExtra
                            (EVENTS_TO_LOAD);
                    if (!eventsToReturn.isEmpty()) {
                        LocalDate ld = addEventsToDatabase(eventsToReturn);
                        startDiaryAtDate(ld);
                    }
                }
                break;
        }
    }

    private File getChosenFile(Intent data) {
        // Get the URI of the selected file
        final Uri uri = data.getData();
        File file = null;
        try {
            // Get the file path from the URI
            final String path = FileUtils.getPath(this, uri);
            Toast.makeText(getApplicationContext(),
                    "File Selected: " + path, Toast.LENGTH_LONG).show();

            if (path != null && FileUtils.isLocal(path)) {
                file = new File(path);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public LocalDate addEventsToDatabase(List<Event> events) {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        dbHandler.addEventsWithUnknownTagTemplates(events);
        return events.get(events.size() - 1).getTime().toLocalDate();
    }

    private void startDiaryAtDate(LocalDate ld) {
        Fragment fragment = new DiaryContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOCALDATE, ld);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, DIARY_CONTAINER)
                .commit();
    }

    private void startDiaryAtLastDate(){
        final DBHandler dbImport = new DBHandler(getApplication());
        LocalDate lastDateOfEvents = dbImport.getDateOfLastEvent();
        lastDateOfEvents = lastDateOfEvents != null ? lastDateOfEvents : LocalDate.now();
        startDiaryAtDate(lastDateOfEvents);
    }
    private class ImportDBAsyncTask extends AsyncTask<Integer, Void, Void> {
        final String TAG = this.getClass().getName();
        File file = null;

        public ImportDBAsyncTask(File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground(Integer... notUsedParams) {
            ExternalStorageHandler.replaceDBWithExtStorageFile(file, getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void notUsed) {
            //after db has been replaced, make the date shown for user the last date filled in
            // new db.
            startDiaryAtLastDate();
        }
    }

    /**
     * Database will not be cleared, this is an adder function. However, conflicting events (
     * with same date and event type) will be ignored (no override or duplication).
     * Best ways to fix that? => probably in database (similar to "unique ignore" or something
     * like it)
     */
    private class ImportCsvAsyncTask extends AsyncTask<Integer, Void, Void> {
        final String TAG = this.getClass().getName();
        File file = null;

        public ImportCsvAsyncTask(File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground(Integer... notUsedParams) {
            List<Event> importedEvents = ExternalStorageHandler.importEventsFromCsv(file);
            DBHandler db = new DBHandler(getApplicationContext());
            // db.deleteAllTablesRows(); This is not meant to be here,
            // better just to add events and cascade ignore on existing ones (same type, same time).
            db.addEventsWithUnknownTagTemplates(importedEvents);
            return null;
        }

        @Override
        protected void onPostExecute(Void notUsed) {
            //after db has been replaced, make the date shown for user the last date filled in
            // new db.
            final DBHandler dbImport = new DBHandler(getApplication());
            LocalDate lastDateOfEvents = dbImport.getDateOfLastEvent();
            lastDateOfEvents = lastDateOfEvents != null ? lastDateOfEvents : LocalDate.now();
            startDiaryAtDate(lastDateOfEvents);
        }


    }
}
