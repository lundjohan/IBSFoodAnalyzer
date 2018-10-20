package com.johanlund.screens.main;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.external_storage.ExternalStorageHandler;
import com.johanlund.external_storage.SaveDBIntentService;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.about.AboutActivity;
import com.johanlund.screens.events_container_classes.DiaryContainerFragment;
import com.johanlund.screens.events_templates_dashboard.TemplateFragment;
import com.johanlund.screens.load_import.mvc_controllers.ImportOptionsFragment;
import com.johanlund.screens.load_import.mvc_controllers.ImportOptionsFragmentImpl;
import com.johanlund.screens.settings.GeneralSettingsActivity;
import com.johanlund.screens.statistics.options.StatOptionsFragment;

import org.threeten.bp.LocalDate;

import java.io.File;
import java.util.List;

import static com.johanlund.constants.Constants.DATE_TO_START_DIARY;
import static com.johanlund.constants.Constants.DIARY_CONTAINER;
import static com.johanlund.constants.Constants.EVENTS_TO_LOAD;
import static com.johanlund.constants.Constants.IMPORT_FROM_CSV_FILE;
import static com.johanlund.constants.Constants.LOAD_EVENTS_FROM_EVENTSTEMPLATE;
import static com.johanlund.constants.Constants.LOCALDATE;
import static com.johanlund.constants.Constants.RESTART_DATE_REQUEST;

/**
 * The Main Activity in the app.
 * <p>
 * For handling of dates: see comments in DiaryContainerFragment
 */
public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DiaryContainerFragment
        .DiaryContainerListener, TemplateFragment
        .TemplateFragmentListener, StatOptionsFragment.DiaryStarterActivity, ImportOptionsFragment.ImportOptionsFragmentListener {
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;

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

        if (savedInstanceState != null) {
            LocalDate ld = (LocalDate) savedInstanceState.getSerializable(LOCALDATE);
            restartContainerDiary(ld);

        } else {
            initiateDiary();
        }
    }

    private void initiateDiary() {
        Fragment fragment = new DiaryContainerFragment();
        FragmentManager transaction = getSupportFragmentManager();
        transaction.beginTransaction()
                .add(R.id.fragment_container, fragment, DIARY_CONTAINER)
                .commit();
    }

    @Override
    public void restartContainerDiary(LocalDate ld) {
        Fragment fragment = new DiaryContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOCALDATE, ld);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, DIARY_CONTAINER)
                .commit();
    }

    public void startContainerDiary(LocalDate ld) {
        Fragment fragment = reOrStartContainerDiaryHelper(ld);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, DIARY_CONTAINER)
                .commit();
    }

    private Fragment reOrStartContainerDiaryHelper(LocalDate ld) {
        Fragment fragment = new DiaryContainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOCALDATE, ld);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        DiaryContainerFragment diaryContainer = (DiaryContainerFragment)
                getSupportFragmentManager().findFragmentByTag(DIARY_CONTAINER);
        if (diaryContainer != null && diaryContainer.isVisible()) {
            savedInstanceState.putSerializable(LOCALDATE, diaryContainer.extractDateFromDiary());
        }
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager manager = getSupportFragmentManager();
        int sizeOfStack = manager.getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (sizeOfStack > 0) {
            //pop away TemplateFragment from BackStack
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            Intent i = new Intent(this, GeneralSettingsActivity.class);
            startActivity(i);
            return true;
        }*/
        //this is solely used from TemplateFragment
        if (id == android.R.id.home) {
            //pop away TemplateFragment from BackStack
            getSupportFragmentManager().popBackStackImmediate();

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
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                break;

            case R.id.importMenuItem:
                fragment = new ImportOptionsFragmentImpl();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
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

            /*case R.id.importFromCsvMenuItem:
                ExternalStorageHandler.showReadablePermission(this);
                showChooserForCSVImport();
                break;*/

            /*case R.id.exportToCsvMenuItem:
                //ok, write to file? Otherwise ask for permission
                ExternalStorageHandler.showWritablePermission(this);
                //IntentService
                Intent csvIntent = new Intent(this, SaveToCSVIntentService.class);
                startService(csvIntent);
                //show pop up that shows location where file was saved.
                showPopUpWithSavedFileLocationSavedFile(ExternalStorageHandler.getFolderToSaveIn());
                break;*/
            /*case R.id.exportCsvForGraph:
                ExternalStorageHandler.showWritablePermission(this);
                Intent csvGraphIntent = new Intent(this, SaveToCSVForGraphIntentService.class);
                startService(csvGraphIntent);
                showPopUpWithSavedFileLocationSavedFile(ExternalStorageHandler.getFolderToSaveIn());
                break;*/
            case R.id.advSettingsItem: {
                Intent i = new Intent(this, GeneralSettingsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.clearDBItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false).
                        setTitle("Clear Database?").
                        setMessage("Warning! Are you sure you want to erase all of the diary? " +
                                "(Manually saved files will not be erased.)").
                        setCancelable(true).
                        setNegativeButton(android.R.string.cancel, new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).
                        setPositiveButton(android.R.string.ok, new DialogInterface
                                .OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBHandler dbHandler = new DBHandler(getApplicationContext());
                                dbHandler.deleteAllTablesRows();
                                restartContainerDiary(null);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                break;

            case R.id.aboutItem:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                intentAbout.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
    public void startTemplateFragment() {
        //toggle.setHomeAsUpIndicator(null);
        Fragment fragment = new TemplateFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).addToBackStack(null)
                .commit();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
           /* case IMPORT_FROM_CSV_FILE:
                if (data != null) {
                    File csvFileToImport = getChosenFile(data);
                    if (csvFileToImport != null) {
                        ImportCsvAsyncTask asyncThread = new ImportCsvAsyncTask(csvFileToImport);
                        asyncThread.execute(0);
                    }
                }
                break;*/

            //TODO shoudln't this be transfered to Diary- or DiaryContainerFragment?
            case LOAD_EVENTS_FROM_EVENTSTEMPLATE: {
                if (data.hasExtra(EVENTS_TO_LOAD)) {
                    List<Event> eventsToReturn = (List<Event>) data.getSerializableExtra
                            (EVENTS_TO_LOAD);
                    if (!eventsToReturn.isEmpty()) {
                        LocalDate ld = addEventsToDatabase(eventsToReturn);
                        restartContainerDiary(ld);
                    }
                }
                break;
            }

            //used for clicking on date inside TimeStat
            case RESTART_DATE_REQUEST: {
                if (data.hasExtra(DATE_TO_START_DIARY)) {
                    LocalDate ld = (LocalDate) data.getSerializableExtra(DATE_TO_START_DIARY);
                    restartContainerDiary(ld);
                }
                break;
            }
        }
    }

    public LocalDate addEventsToDatabase(List<Event> events) {
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        dbHandler.addEvents(events);
        return events.get(events.size() - 1).getTime().toLocalDate();
    }

    @Override
    public void startDiaryAtLastDate() {
        final DBHandler dbImport = new DBHandler(getApplication());
        LocalDate lastDateOfEvents = dbImport.getDateOfLastEvent();
        lastDateOfEvents = lastDateOfEvents != null ? lastDateOfEvents : LocalDate.now();
        restartContainerDiary(lastDateOfEvents);
    }

    @Override
    public void startDiaryWithDate(LocalDate ld) {
        startContainerDiary(ld);
    }

    /**
     * Database will not be cleared, this is an adder function. However, conflicting events (
     * with same date and event type) will be ignored (no override or duplication).
     * Best ways to fix that? => probably in database (similar to "unique ignore" or something
     * like it)
     */
  /*  private class ImportCsvAsyncTask extends AsyncTask<Integer, Void, Void> {
        final String TAG = this.getClass().getName();
        File file = null;

        public ImportCsvAsyncTask(File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground(Integer... notUsedParams) {
            List<Event> importedEvents = ExternalStorageHandler.importEventsFromCsv(file);
            DBHandler db = new DBHandler(getApplicationContext());
            db.addEvents(importedEvents);
            return null;
        }

        @Override
        protected void onPostExecute(Void notUsed) {
            startDiaryAtLastDate();
        }


    }*/
}
