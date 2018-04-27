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
            //a bit ugly solution but it works perfectly
            //1. Set preference values to default (default values is set in the pref xml file itself)
            SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(this);
            settings.edit().remove("hours_break").commit();

            //2. Reload fragment (same as in onCreate above)
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new GeneralPreferenceFragment())
                    .commit();

            //below didn't work (seekbar beccomes null)
            //SeekBarPreference seekBar = (SeekBarPreference) findPreference("hours_break");
        }
        return true;
    }
}