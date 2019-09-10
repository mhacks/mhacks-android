package org.mhacks.app.pref

import org.mhacks.app.core.DarkModeType
import org.mhacks.app.core.ThemePrefProvider
import javax.inject.Inject

class PrefRepository @Inject constructor(
        private val themePrefProvider: ThemePrefProvider
) {

    val darkModeType: DarkModeType
        get() = themePrefProvider.darkModeType

}