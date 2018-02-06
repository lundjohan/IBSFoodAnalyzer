package com.ibsanalyzer.statistics_settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.ibsanalyzer.settings.AppCompatPreferenceActivity;


public abstract class SettingsBaseActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, getFragment())
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
}