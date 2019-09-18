package org.mhacks.app.pref.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.mhacks.app.pref.R
import org.mhacks.app.pref.inject

private val EMAIL_EMAIL_ADDRESS = arrayOf("recipient@example.com")
private const val EMAIL_HEADER = "MHacks Android Feedback"

private val EMAIL_TEXT = """
                    |
                    |
                    |Device: ${Build.MODEL}
                    |OS: ${Build.VERSION.RELEASE}""".trimMargin()

class PrefFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        inject()
        preferenceScreen = DarkModePreferenceScreenFactory.create(
                preferenceManager,
                requireActivity()
        )
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
}