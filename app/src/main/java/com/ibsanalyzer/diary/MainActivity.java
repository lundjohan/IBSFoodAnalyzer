package com.ibsanalyzer.diary;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.ibsanalyzer.adapters.TabPagerAdapter;
import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.constants.Constants;
import com.ibsanalyzer.database.DBHandler;
import com.ibsanalyzer.external_storage.ExternalStorageHandler;
import com.ibsanalyzer.external_storage.SaveDBIntentService;
import com.ibsanalyzer.settings.GeneralSettingsActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.threeten.bp.LocalDate;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.EVENTS_TO_LOAD;
import static com.ibsanalyzer.constants.Constants.LIST_OF_EVENTS;
import static com.ibsanalyzer.constants.Constants.LOAD_EVENTS_FROM_EVENTSTEMPLATE;
import static com.ibsanalyzer.constants.Constants.REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {//, StatFragment.StatFragmentListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabPagerAdapter adapter;
    File dbFileToImport = null;

    private static final int REQUEST_CODE = 6384; // onActivityResult request

    // code
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.importMenuItem:
                showChooser();
                return true;

            //THIS OPTION WILL BE REMOVED IN PRODUCTION CODE!!!
            //No effort is therefore to put it in thread etc
           /* case R.id.importFromTxtMenuItem:
                final DBHandler db = new DBHandler(this);
                List<Event> events = ExternalStorageHandler.importEventsFromTxt();
                db.deleteAllTablesRows();
                db.addEventsWithUnknownTagTemplates(events);
                try {
                    adapter.getDiaryFragment().fillEventListWithDatabase();

                } catch (Exception e) {
                    Log.e("Error", "Adapter could not be updated after replacement of " +
                            "database");
                    Log.e("Error", e.getStackTrace().toString());

                }*/


            case R.id.exportMenuItem:
                //ok, write to file? Otherwise ask for permission
                ExternalStorageHandler.showWritablePermission(this);
                //IntentService
                Intent intent = new Intent(this, SaveDBIntentService.class);
                startService(intent);


                return true;
            case R.id.settingsMenuItem:
                Intent i = new Intent(this, GeneralSettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.clearDBItem:
                DBHandler dbHandler = new DBHandler(getApplicationContext());
                dbHandler.deleteAllTablesRowsExceptTagTemplates();
                try {
                    adapter.getDiaryFragment().fillEventListWithDatabase(LocalDate.now());

                } catch (Exception e) {
                    Log.e("Error", "Adapter could not be updated after replacement of " +
                            "database");
                    Log.e("Error", e.getStackTrace().toString());

                }
                return true;
            case R.id.aboutItem:
                //TODO: Apache 2.0 talk about the external libraries.
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //code reused from aFileChooser example
    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.PACKAGE_NAME = getApplicationContext().getPackageName();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //see Android Studio Development essentials p. 337
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //start from DiaryFragment
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }

    public void changeToTemplateFragment() {
        viewPager.setCurrentItem(0);
    }

    public DiaryFragment accessDiaryFragment() {
        return (DiaryFragment) adapter.getRegisteredFragment(1);
    }
    //==============================================================================================


    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT)
                            .show();
                }
        }
    }

   /* @Override
    public List<Event> retrieveEvents() {
        List<Event> events = new ArrayList<>();
        //1. Access Diary Fragment
        DiaryFragment diary = accessDiaryFragment();
        //2 Retrieve events from it
        events = diary.getEvents();
        //TODO 3. if this fails, retrieve events from database instead.
        return events;
    }*/

   /* @Override
    public void startNestedFragment(Fragment f) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.pager, f).commit();
    }*/

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
            try {
                adapter.getDiaryFragment().fillEventListWithDatabase(LocalDate.now());

            } catch (Exception e) {
                Log.d(TAG, "Adapter could not be updated after replacement of database");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE:
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i("Debug", "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Toast.makeText(getApplicationContext(),
                                    "File Selected: " + path, Toast.LENGTH_LONG).show();

                            if (path != null && FileUtils.isLocal(path)) {
                                dbFileToImport = new File(path);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (dbFileToImport != null) {
                            ImportDBAsyncTask asyncThread = new ImportDBAsyncTask(dbFileToImport);
                            asyncThread.execute(0);
                        }
                        dbFileToImport = null;
                    }
                break;
            case LOAD_EVENTS_FROM_EVENTSTEMPLATE:
                if (data.hasExtra(EVENTS_TO_LOAD)) {
                    List<Event> eventsToReturn = (List<Event>) data.getSerializableExtra(EVENTS_TO_LOAD);
                 //   addEventsFromEventsTemplateToDiary(eventsToReturn);
                }
                break;
        }
        //this must be here so that this activities fragments can use onActivityResult also. Its
        // strange...
        super.onActivityResult(requestCode, resultCode, data);
    }
}

