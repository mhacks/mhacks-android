package org.mhacks.app.pref.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.mhacks.app.core.DarkModeType
import org.mhacks.app.core.ThemePrefProvider
import org.mhacks.app.core.ktx.isAtLeastQ
import org.mhacks.app.pref.PrefRepository
import org.mhacks.app.pref.R
import org.mhacks.app.pref.inject
import org.mhacks.app.setDarkMode
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

private val EMAIL_EMAIL_ADDRESS = arrayOf("recipient@example.com")
private const val EMAIL_HEADER = "MHacks Android Feedback"

private val EMAIL_TEXT = """
                    |
                    |
                    |Device: ${Build.MODEL}
                    |OS: ${Build.VERSION.RELEASE}""".trimMargin()

class PrefFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var prefRepository: PrefRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        inject()
        createDarkModeListPreference()
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return when (preference?.key) {
            getString(R.string.pref_feedback) -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "message/rfc822"
                i.putExtra(Intent.EXTRA_EMAIL, EMAIL_EMAIL_ADDRESS)
                i.putExtra(Intent.EXTRA_SUBJECT, EMAIL_HEADER)
                i.putExtra(Intent.EXTRA_TEXT, EMAIL_TEXT)
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."))
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(
                            requireContext(),
                            "There are no emails installed.",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }
            else -> {
                super.onPreferenceTreeClick(preference)
            }
        }
    }

    private fun createDarkModeListPreference() {
        val screen = preferenceManager.createPreferenceScreen(requireContext())
        val entries = if (isAtLeastQ()) {
            listOf(
                    coreR.string.system_auto,
                    coreR.string.light,
                    coreR.string.dark
            )
        } else {
            listOf(
                    coreR.string.light,
                    coreR.string.dark
            )
        }
                .map { context?.getString(it) }
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

        val darkModeListPreference = ListPreference(requireContext()).apply {
            this.entries = entries
            entryValues = keys
            setTitle(R.string.pref_dark_mode)
            key = ThemePrefProvider.DARK_MODE_PREF_KEY
            val darkModeKey =
                    if (isAtLeastQ()) DarkModeType.SYSTEM_AUTO.key else DarkModeType.LIGHT.key
            setDefaultValue(darkModeKey)
            setOnPreferenceChangeListener { _, newValue ->
                setDarkMode(DarkModeType.keyOf(newValue as String), requireActivity()::class.java)
                true
            }
        }
        screen.addPreference(darkModeListPreference)
        preferenceScreen = screen
    }
}