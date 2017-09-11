package com.ibsanalyzer.settings;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.ibsanalyzer.inputday.R;

import static com.ibsanalyzer.constants.Constants.HOURS_AHEAD_FOR_AVG;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences
        .OnSharedPreferenceChangeListener {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //code for making calling Fragment invisible.
        getView().setBackgroundColor(Color.WHITE);
        getView().setClickable(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("Debug", "Inside onSharedPreferenceChanged before changes. HOURS_AHEAD_FOR_AVG = "
                + HOURS_AHEAD_FOR_AVG);

        Preference preference = findPreference(key);
        if (preference instanceof EditTextPreference) {
            String avgHoursAhead = ((EditTextPreference) preference).getText();
            HOURS_AHEAD_FOR_AVG = Integer.valueOf(avgHoursAhead);

        }
        Log.d("Debug", "Inside onSharedPreferenceChanged after changes. HOURS_AHEAD_FOR_AVG = "
                + HOURS_AHEAD_FOR_AVG);
    }
}