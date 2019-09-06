package org.mhacks.app.pref

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class PrefFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return when (preference?.key) {
            getString(R.string.pref_feedback) -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "message/rfc822"
                i.putExtra(Intent.EXTRA_EMAIL, arrayOf("recipient@example.com"))
                i.putExtra(Intent.EXTRA_SUBJECT, "MHacks Android Feedback")
                i.putExtra(Intent.EXTRA_TEXT, """
                    
                    |
                    |
                    |Device: ${Build.MODEL}
                    |OS: ${Build.VERSION.RELEASE}""".trimMargin())
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