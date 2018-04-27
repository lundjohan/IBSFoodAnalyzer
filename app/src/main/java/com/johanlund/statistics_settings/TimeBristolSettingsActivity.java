package com.johanlund.statistics_settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.johanlund.ibsfoodanalyzer.R;

/**
 * Created by Johan on 2018-03-13.
 */

public class TimeBristolSettingsActivity extends SettingsBaseActivity{
    @Override
    protected PreferenceFragment getFragment() {
        return new BristolTimeSettingsFragment();
    }

    @Override
    protected void restoreDefaultForThesePref() {
        prefToDefault(getResources().getString(R.string.time_bristol_start));
        prefToDefault(getResources().getString(R.string.time_bristol_end));
        prefToDefault(getResources().getString(R.string.time_bristol_duration_key));
    }

    public static class BristolTimeSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.bristol_time_preferences);
        }
    }
}
