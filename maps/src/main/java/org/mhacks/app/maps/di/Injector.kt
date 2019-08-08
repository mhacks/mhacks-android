package org.mhacks.app.maps.di

import org.mhacks.app.coreComponent
import org.mhacks.app.maps.di.component.DaggerMapComponent
import org.mhacks.app.maps.widget.MapViewFragment

fun MapViewFragment.inject() {
    DaggerMapComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}