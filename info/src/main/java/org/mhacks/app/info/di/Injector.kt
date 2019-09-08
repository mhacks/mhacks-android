package org.mhacks.app.info.di

import org.mhacks.app.coreComponent

import org.mhacks.app.info.di.component.DaggerInfoComponent
import org.mhacks.app.info.widget.InfoFragment

fun InfoFragment.inject() {
    DaggerInfoComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}