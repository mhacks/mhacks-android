package com.mhacks.app.ui.events

import com.mhacks.app.ui.map.presenter.MapViewFragmentPresenter
import com.mhacks.app.ui.map.presenter.MapViewFragmentPresenterImpl
import com.mhacks.app.ui.map.view.MapView
import com.mhacks.app.ui.map.view.MapViewFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Provides dependencies for welcome module.
 */
@Module
abstract class MapViewFragmentModule {

    @Binds
    abstract fun provideMapView(mapViewFragment: MapViewFragment): MapView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideMapViewPresenter(mapView: MapView): MapViewFragmentPresenter =
                MapViewFragmentPresenterImpl(mapView)
    }
}