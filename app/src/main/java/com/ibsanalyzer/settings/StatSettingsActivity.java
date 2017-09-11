package com.ibsanalyzer.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.SeekBarPreference;
import android.view.MenuItem;

import com.ibsanalyzer.inputday.R;

import static android.R.attr.id;


public class StatSettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new StatPreferenceFragment())
                .commit();
    }

    public static class StatPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
