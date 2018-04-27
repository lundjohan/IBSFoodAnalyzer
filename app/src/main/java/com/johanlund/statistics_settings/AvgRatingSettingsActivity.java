package com.johanlund.statistics_settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.johanlund.ibsfoodanalyzer.R;

public class AvgRatingSettingsActivity extends SettingsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected PreferenceFragment getFragment() {
        return new AvgRatingSettingsFragment();
    }

    @Override
    protected void restoreDefaultForThesePref() {
        prefToDefault(getResources().getString(R.string.avg_rating_pref_wait_key));
        prefToDefault(getResources().getString(R.string.avg_rating_pref_stop_key));
        prefToDefault(getResources().getString(R.string.avg_rating_pref_quant_key));
    }

    public static class AvgRatingSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.avg_rating_preferences);
        }
    }
}