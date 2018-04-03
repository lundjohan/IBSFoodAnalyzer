package com.johanlund.statistics_settings_portions;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.johanlund.ibsfoodanalyzer.R;

/**
 * Created by Johan on 2018-04-03.
 */
public class PortionsRatingSettingsFragment extends PreferenceFragment {
    public PortionsRatingSettingsFragment() {

    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.portions_some_of_the_preferences);
    }
}
