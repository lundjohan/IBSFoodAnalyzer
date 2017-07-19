package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.LIST_OF_EVENTS;
import static com.ibsanalyzer.constants.Constants.REQUEST_PERMISSION_WRITE_TO_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements DiaryFragment.DiaryFragmentListener, StatFragment.StatFragmentListener{
    TabLayout tabLayout;
    ViewPager viewPager;
    TabPagerAdapter adapter;

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
                ImportDBAsyncTask asyncThread = new ImportDBAsyncTask();
                asyncThread.execute(0);
                return true;

            //THIS OPTION WILL BE REMOVED IN PRODUCTION CODE!!!
            //No effort is therefore to put it in thread etc
            case R.id.importFromTxtMenuItem:
                final DBHandler db = new DBHandler(this);
                List<Event> events = ExternalStorageHandler.importEventsFromTxt();
                db.deleteAllTablesRows();
                db.addEventsWithUnknownTagTemplates(events);

                try {
                    adapter.getDiaryFragment().fillEventListWithDatabase();

                } catch (Exception e) {
                    Log.d("Debug", "Adapter could not be updated after replacement of " +
                            "database");

                }


                return true;
            case R.id.exportMenuItem:
                //ok, write to file? Otherwise ask for permission
                ExternalStorageHandler.showWritablePermission(this);
                //IntentService
                Intent intent = new Intent(this, SaveDBIntentService.class);
                startService(intent);


                return true;
            case R.id.settingsMenuItem:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    @Override
    public ViewSwitcher getTabsLayoutSwitcher() {
        return (ViewSwitcher) findViewById(R.id.tabLayoutSwitcher);
    }

    public void changeToTemplateFragment() {
        viewPager.setCurrentItem(0);
    }
    //==============================================================================================

    //this method gets marked events from DiaryFragment and calls EventsTemplateAdder to store them.
    @Override
    public void doEventsTemplateAdder(List<Event> events) {
        Intent intent = new Intent(this, TemplateAdderActivity.class);
        //Gson gson = new Gson();
        //String objAsJSON = gson.toJson(events);
        intent.putExtra(LIST_OF_EVENTS, (Serializable) events);
        startActivity(intent);
    }


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

    @Override
    public List<Event> retrieveEvents() {
        List<Event>events = new ArrayList<>();
        //1. Access Diary Fragment
        DiaryFragment diary = (DiaryFragment)adapter.getRegisteredFragment(1);
        //2 Retrieve events from it
        events = diary.getEvents();
        //TODO 3. if this fails, retrieve events from database instead.
        return events;
    }

    private class ImportDBAsyncTask extends AsyncTask<Integer, Void, Void> {
        final String  TAG = this.getClass().getName();
        public ImportDBAsyncTask() {
        }

        @Override
        protected Void doInBackground(Integer... notUsedParams) {
            ExternalStorageHandler.replaceDBWithExtStorageFile();
            return null;
        }
        @Override
        protected void onPostExecute(Void notUsed) {
            try {
                adapter.getDiaryFragment().fillEventListWithDatabase();

            } catch (Exception e) {
                Log.d(TAG, "Adapter could not be updated after replacement of database");
            }
        }
    }
}

