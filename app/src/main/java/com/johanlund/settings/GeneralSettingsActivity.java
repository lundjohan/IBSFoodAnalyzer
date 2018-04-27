package com.johanlund.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SeekBarPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.johanlund.ibsfoodanalyzer.R;

import java.util.Map;

/**
 * Created by Johan on 2017-10-05.
 */

public class GeneralSettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GeneralPreferenceFragment())
                .commit();
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.general_preferences);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.to_default_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.menu_to_default) {
            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(this);
            settings.edit().remove("hours_break").commit();
            //PreferenceManager.setDefaultValues(getApplicationContext(),R.xml.general_preferences,true);
           // int d =  settings.getInt("hours_break", 10);
            //SeekBarPreference yourSeekBar = (SeekBarPreference) findViewById(R.id.pref_hours_break);
            //yourSeekBar.setCurrentValue(20);

           // Log.d(getClass().getSimpleName(), Integer.toString(d));
        }
        return true;
    }
}