package org.mhacks.app.pref

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class PrefFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}