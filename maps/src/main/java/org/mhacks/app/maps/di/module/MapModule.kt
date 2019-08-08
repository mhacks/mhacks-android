package org.mhacks.app.maps.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.mhacks.app.core.di.ViewModelKey
import org.mhacks.app.maps.MapViewModel

/**
 * Provides dependencies for the maps module.
 */
@Module
abstract class MapModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel

}