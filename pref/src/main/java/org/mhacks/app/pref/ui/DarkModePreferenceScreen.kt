package org.mhacks.app.pref.ui

import android.app.Activity
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceScreen
import org.mhacks.app.core.DarkModeType
import org.mhacks.app.core.R
import org.mhacks.app.core.ThemePrefProvider
import org.mhacks.app.core.ktx.isAtLeastQ
import org.mhacks.app.setDarkMode

object DarkModePreferenceScreenFactory {

    fun create(
            preferenceManager: PreferenceManager, activity: Activity): PreferenceScreen {
        val screen = preferenceManager.createPreferenceScreen(activity)
        val entries = if (isAtLeastQ()) {
            listOf(
                    R.string.system_auto,
                    R.string.light,
                    R.string.dark
            )
        } else {
            listOf(
                    R.string.light,
                    R.string.dark
            )
        }
                .map { activity.getString(it) }
                .toTypedArray()

        val keys = if (isAtLeastQ()) {
            listOf(
                    DarkModeType.SYSTEM_AUTO,
                    DarkModeType.LIGHT,
                    DarkModeType.DARK
            )
        } else {
            listOf(
                    DarkModeType.LIGHT,
                    DarkModeType.DARK
            )
        }
                .map { it.key }
                .toTypedArray()

        val darkModeListPreference = ListPreference(activity).apply {
            this.entries = entries
            entryValues = keys
            setTitle(org.mhacks.app.pref.R.string.pref_dark_mode)
            key = ThemePrefProvider.DARK_MODE_PREF_KEY
            val darkModeKey =
                    if (isAtLeastQ()) DarkModeType.SYSTEM_AUTO.key else DarkModeType.LIGHT.key
            setDefaultValue(darkModeKey)
            setOnPreferenceChangeListener { _, newValue ->
                activity.application.setDarkMode(
                        DarkModeType.keyOf(newValue as String),
                        activity = activity,
                        targetActivity = activity::class.java
                )
                true
            }
        }
        screen.addPreference(darkModeListPreference)
        return screen
    }
}