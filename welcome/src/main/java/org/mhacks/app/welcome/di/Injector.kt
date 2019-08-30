package org.mhacks.app.welcome.di

import org.mhacks.app.coreComponent
import org.mhacks.app.welcome.di.component.DaggerWelcomeComponent
import org.mhacks.app.welcome.widget.WelcomeFragment

fun WelcomeFragment.inject() {
    DaggerWelcomeComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}