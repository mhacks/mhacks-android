package org.mhacks.app.ticket

import org.mhacks.app.coreComponent

fun TicketDialogFragment.inject() {
    DaggerTicketComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}