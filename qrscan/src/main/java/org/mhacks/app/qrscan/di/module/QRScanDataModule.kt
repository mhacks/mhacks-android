package org.mhacks.app.qrscan.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import org.mhacks.app.core.di.module.FeatureScope
import org.mhacks.app.qrscan.CameraPreferencesManager

@Module
class QRScanDataModule {

    @Provides
    @FeatureScope
    fun provideSharedPreferences(context: Context) =
            PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @FeatureScope
    fun provideCameraPreferencesManager(sharedPreferences: SharedPreferences) =
            CameraPreferencesManager(sharedPreferences)

}