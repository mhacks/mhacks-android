package org.mhacks.app.pref

import org.mhacks.app.coreComponent
import org.mhacks.app.pref.di.component.DaggerPrefComponent
import org.mhacks.app.pref.ui.PrefFragment

fun PrefFragment.inject() {
    DaggerPrefComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}