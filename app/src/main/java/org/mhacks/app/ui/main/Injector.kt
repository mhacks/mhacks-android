package org.mhacks.app.ui.main

import org.mhacks.app.coreComponent
import org.mhacks.app.ui.main.di.DaggerMainComponent

fun MainActivity.inject() {
    DaggerMainComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}