package com.johanlund.screens.statistics_settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.johanlund.ibsfoodanalyzer.R;

public class AvgBmSettingsActivity extends SettingsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected PreferenceFragment getFragment() {
        return new AvgBMSettingsFragment();
    }

    @Override
    protected void restoreDefaultForThesePref() {
        prefToDefault(getResources().getString(R.string.hours_before_bm_closest_distance_limit));
        prefToDefault(getResources().getString(R.string.hours_before_bm_furthest_distance_limit));
        prefToDefault(getResources().getString(R.string.avg_bm_pref_quant_key));
    }

    public static class AvgBMSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.avg_bm_preferences);
        }
    }
}
