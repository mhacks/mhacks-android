package org.mhacks.app.core

import android.content.SharedPreferences
import androidx.annotation.StringRes
import org.mhacks.app.core.ktx.isAtLeastQ
import javax.inject.Inject

enum class DarkModeType(val key: String, @StringRes val title: Int) {
    DARK("DARK", R.string.dark),
    LIGHT("LIGHT", R.string.light),
    SYSTEM_AUTO("SYSTEM_AUTO", R.string.system_auto);

    companion object {

        fun default() = if (isAtLeastQ()) SYSTEM_AUTO else LIGHT

        fun keyOf(key: String): DarkModeType {
            return values().find { it.key == key } ?: throw RuntimeException("Should exist in enum")
        }
    }
}

class ThemePrefProvider @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {

        const val DARK_MODE_PREF_KEY = "DARK_MODE_PREF_KEY"

    }

    val darkModeType: DarkModeType
        get() {
            val key = sharedPreferences.getString(DARK_MODE_PREF_KEY, "")?.takeUnless {
                it.isBlank()
            } ?: return DarkModeType.default()
            return DarkModeType.keyOf(key)
        }

}