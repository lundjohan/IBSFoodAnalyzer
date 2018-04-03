package com.johanlund.statistics_settings_portions;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_settings.SettingsBaseActivity;

public class PortionStatSettingsMoreActivity extends SettingsBaseActivity {
    @Override
    protected PreferenceFragment getFragment() {
        return new PortionsRatingSettingsFragment();
    }

    public static class PortionsRatingSettingsFragment extends PreferenceFragment {
        public PortionsRatingSettingsFragment() {

        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.portions_some_of_the_preferences);
        }
    }
}