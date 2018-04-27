package com.johanlund.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SeekBarPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.statistics_settings.SettingsBaseActivity;

import java.util.Map;

/**
 * Created by Johan on 2017-10-05.
 */

public class GeneralSettingsActivity extends SettingsBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected PreferenceFragment getFragment() {
        return new GeneralPreferenceFragment();
    }

    @Override
    protected void restoreDefaultForThesePref() {
        prefToDefault("hours_break");
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.general_preferences);
        }
    }
}