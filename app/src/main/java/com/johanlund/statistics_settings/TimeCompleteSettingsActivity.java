package com.johanlund.statistics_settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.johanlund.ibsfoodanalyzer.R;

/**
 * Created by Johan on 2018-03-13.
 */

public class TimeCompleteSettingsActivity extends SettingsBaseActivity{
    @Override
    protected PreferenceFragment getFragment() {
        return new BmTimeSettingsFragment();
    }

    public static class BmTimeSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.complete_time_preferences);
        }
    }
}
