package com.johanlund.statistics_settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.johanlund.settings.AppCompatPreferenceActivity;


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
        return true;
    }
    protected abstract PreferenceFragment getFragment();

    /**
     * Method can be overrided in case a smaller fraction (than the full) of screen should be
     * preference fragment.
     * (This is currently not used. It is frankly hell to mix like that with preferencefragment,
     * its not very flexible.)
     */
    protected int getFragmentContainer(){
        return android.R.id.content;
    }
}