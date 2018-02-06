package com.ibsanalyzer.statistics_settings;

import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.settings.AppCompatPreferenceActivity;


public class AvgRatingSettingsActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AvgRatingSettingsFragment())
                .commit();
    }

    public static class AvgRatingSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.avg_rating_preferences);
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