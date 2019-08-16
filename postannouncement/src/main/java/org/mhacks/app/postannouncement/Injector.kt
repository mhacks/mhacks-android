package org.mhacks.app.postannouncement

import org.mhacks.app.postannouncement.di.DaggerPostAnnouncementComponent
import org.mhacks.app.postannouncement.ui.PostAnnouncementDialogFragment
import org.mhacks.app.coreComponent

fun PostAnnouncementDialogFragment.inject() {
    DaggerPostAnnouncementComponent.builder()
            .coreComponent(coreComponent())
            .build()
            .inject(this)
}