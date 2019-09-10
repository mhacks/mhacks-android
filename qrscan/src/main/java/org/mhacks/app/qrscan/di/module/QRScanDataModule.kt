package org.mhacks.app.qrscan.di.module

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.scope.FeatureScope
import org.mhacks.app.qrscan.QRScanPrefProvider

@Module
class QRScanDataModule {

    @Provides
    @FeatureScope
    fun provideCameraPreferencesManager(sharedPreferences: SharedPreferences) =
            QRScanPrefProvider(sharedPreferences)

}