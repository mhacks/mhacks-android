package org.mhacks.app.events.di

import org.mhacks.app.coreComponent
import org.mhacks.app.events.di.component.DaggerEventComponent
import org.mhacks.app.events.widget.EventFragment

fun EventFragment.inject() {
    DaggerEventComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}