package org.mhacks.app.data.network.registration

import org.mhacks.app.coreComponent

fun RegistrationIntentService.inject() {
    DaggerRegistrationComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}