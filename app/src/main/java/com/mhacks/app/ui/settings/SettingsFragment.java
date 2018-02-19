package com.mhacks.app.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mhacks.app.R;


/**
 * Created by Omkar Moghe on 2/18/2016.
 */
public class SettingsFragment extends PreferenceFragment {
    public static final String USERNAME_KEY = "pref_key_username";
    public static final String PASSWORD_KEY = "pref_key_password";
    public static final String PUSH_NOTIFICATION_CHANNELS = "pref_key_notification_types";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
