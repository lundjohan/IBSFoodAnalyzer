package com.johanlund.statistics_settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.johanlund.ibsfoodanalyzer.R;

/**
 * Created by Johan on 2018-03-13.
 */

public class TimeRatingSettingsActivity extends SettingsBaseActivity{
    @Override
    protected PreferenceFragment getFragment() {
        return new AvgTimeSettingsFragment();
    }

    @Override
    protected void restoreDefaultForThesePref() {
        prefToDefault(getResources().getString(R.string.time_rating_start));
        prefToDefault(getResources().getString(R.string.time_rating_end));
        prefToDefault(getResources().getString(R.string.time_rating_duration_key));
    }

    public static class AvgTimeSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.avg_time_preferences);
        }
    }
}
