package org.mhacks.app.pref.di.module

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.ThemePrefProvider
import org.mhacks.app.core.di.scope.FeatureScope

@Module
class PrefModule {

    @Provides
    @FeatureScope
    fun providePrefRepository(sharedPreferences: SharedPreferences) =
            ThemePrefProvider(sharedPreferences)

}
