package com.mhacks.android.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.mhacks.android.R;

/**
 * Created by Omkar Moghe on 2/18/2016.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
