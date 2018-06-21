package com.johanlund.screens.statistics_settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.settings.AppCompatPreferenceActivity;


public abstract class SettingsBaseActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(getFragmentContainer(), getFragment())
                .commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.menu_to_default) {
            //1. let the classes above restore pref values to default
            restoreDefaultForThesePref();


            //2. Reload fragment (same as in onCreate above)
            getFragmentManager().beginTransaction()
                    .replace(getFragmentContainer(), getFragment())
                    .commit();

            //below didn't work (seekbar beccomes null)
            //SeekBarPreference seekBar = (SeekBarPreference) findPreference("hours_break");
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.to_default_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected abstract PreferenceFragment getFragment();

    /**
     * Method can be overrided in case a smaller fraction (than the full) of screen should be
     * preference fragment.
     * (This is currently not used. It is frankly hell to mix like that with preferencefragment,
     * its not very flexible.)
     */
    protected int getFragmentContainer() {
        return android.R.id.content;
    }

    protected void prefToDefault(String key) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().remove(key).commit();
    }

    protected abstract void restoreDefaultForThesePref();
}