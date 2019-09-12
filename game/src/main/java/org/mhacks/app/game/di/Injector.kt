package org.mhacks.app.game.di

import org.mhacks.app.coreComponent
import org.mhacks.app.game.di.component.DaggerGameComponent
import org.mhacks.app.game.widget.GameActivity

fun GameActivity.inject() {
    DaggerGameComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}