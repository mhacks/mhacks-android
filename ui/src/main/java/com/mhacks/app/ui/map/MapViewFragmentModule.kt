package com.mhacks.app.ui.map

import android.arch.lifecycle.ViewModel
import com.mhacks.app.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Provides dependencies for map fragment module.
 */
@Module
abstract class MapViewFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel

}