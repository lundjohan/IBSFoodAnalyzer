package com.johanlund.screens.statistics.portions_settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.screens.statistics_settings.SettingsBaseActivity;

public class PortionStatSettingsMoreActivity extends SettingsBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected PreferenceFragment getFragment() {
        return new PortionsRatingSettingsFragment();
    }

    @Override
    protected void restoreDefaultForThesePref() {
        prefToDefault(getResources().getString(R.string.portions_rating_pref_wait_hours_key));
        prefToDefault(getResources().getString(R.string.portions_rating_pref_valid_hours_key));
        prefToDefault(getResources().getString(R.string.portions_pref_min_hours_between_meals));
    }

    public static class PortionsRatingSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.portions_some_of_the_preferences);
        }
    }
}