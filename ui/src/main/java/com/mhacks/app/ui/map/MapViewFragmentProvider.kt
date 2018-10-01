package com.mhacks.app.ui.map

import com.mhacks.app.ui.map.widget.MapViewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Provides dependencies into special blend fragment.
 */
@Module
abstract class MapViewFragmentProvider {

    @ContributesAndroidInjector(modules = [MapViewFragmentModule::class])
    abstract fun provideMapViewFragment(): MapViewFragment
}